-- Operational ledger: payment history, campaign rules and inventory movement audit.
-- Apply after 0003_security_hardening.sql.

create type public.payment_status as enum ('PENDIENTE', 'CONFIRMADO', 'RECHAZADO', 'ANULADO');

create table public.campaign_rules (
  campaign_id uuid primary key references public.campaigns(id) on delete cascade,
  sales_goal_cents integer not null default 0 check (sales_goal_cents >= 0),
  minimum_order_cents integer not null default 0 check (minimum_order_cents >= 0),
  direct_commission_bps integer not null default 0 check (direct_commission_bps between 0 and 10000),
  network_commission_bps integer not null default 0 check (network_commission_bps between 0 and 10000),
  member_commission_bps integer not null default 0 check (member_commission_bps between 0 and 10000),
  updated_at timestamptz not null default now()
);

alter table public.products
  add column if not exists brand text not null default 'Sin marca',
  add column if not exists cost_cents integer not null default 0 check (cost_cents >= 0);

alter table public.orders
  add column if not exists due_at timestamptz,
  add column if not exists updated_at timestamptz not null default now();

create table public.payments (
  id uuid primary key default gen_random_uuid(),
  team_id uuid not null references public.teams(id) on delete cascade,
  order_id uuid not null references public.orders(id) on delete cascade,
  customer_id uuid references public.customers(id) on delete set null,
  recorded_by uuid not null references auth.users(id),
  amount_cents integer not null check (amount_cents > 0),
  method text not null check (method in ('YAPE', 'PLIN', 'EFECTIVO', 'TRANSFERENCIA', 'OTRO')),
  status public.payment_status not null default 'PENDIENTE',
  paid_at timestamptz not null default now(),
  due_at timestamptz,
  evidence_url text,
  notes text not null default '',
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table public.inventory_movements (
  id uuid primary key default gen_random_uuid(),
  team_id uuid not null references public.teams(id) on delete cascade,
  product_id uuid not null references public.products(id) on delete cascade,
  recorded_by uuid not null references auth.users(id),
  quantity_delta integer not null check (quantity_delta <> 0),
  reason text not null check (reason in ('COMPRA', 'VENTA', 'DEVOLUCION', 'AJUSTE', 'RESERVA', 'LIBERACION')),
  reference_id uuid,
  notes text not null default '',
  created_at timestamptz not null default now()
);

alter table public.team_members
  add column if not exists sponsor_user_id uuid references auth.users(id) on delete set null;

create index if not exists campaign_rules_updated_idx on public.campaign_rules (updated_at desc);
create index if not exists payments_team_created_idx on public.payments (team_id, created_at desc);
create index if not exists payments_order_status_idx on public.payments (order_id, status);
create index if not exists payments_customer_due_idx on public.payments (customer_id, due_at);
create index if not exists inventory_movements_product_created_idx on public.inventory_movements (product_id, created_at desc);
create index if not exists team_members_sponsor_idx on public.team_members (team_id, sponsor_user_id);

alter table public.campaign_rules enable row level security;
alter table public.payments enable row level security;
alter table public.inventory_movements enable row level security;

create policy campaign_rules_member_read on public.campaign_rules for select
  using ((select private.is_team_member((select c.team_id from public.campaigns c where c.id = campaign_id), (select auth.uid()))));
create policy campaign_rules_leader_manage on public.campaign_rules for all
  using ((select private.is_team_leader((select c.team_id from public.campaigns c where c.id = campaign_id), (select auth.uid()))))
  with check ((select private.is_team_leader((select c.team_id from public.campaigns c where c.id = campaign_id), (select auth.uid()))));

create policy payments_owner_or_leader_read on public.payments for select
  using (recorded_by = (select auth.uid()) or (select private.is_team_leader(team_id, (select auth.uid()))));
create policy payments_member_insert on public.payments for insert
  with check (recorded_by = (select auth.uid()) and (select private.is_team_member(team_id, (select auth.uid()))));
create policy payments_owner_or_leader_update on public.payments for update
  using (recorded_by = (select auth.uid()) or (select private.is_team_leader(team_id, (select auth.uid()))))
  with check (recorded_by = (select auth.uid()) or (select private.is_team_leader(team_id, (select auth.uid()))));

-- Payment writes go through record_payment so the order, team and balance are
-- checked in one transaction instead of trusting a client-provided row.
revoke insert, update, delete on public.payments from authenticated;
grant select on public.payments to authenticated;

create policy inventory_member_read on public.inventory_movements for select
  using ((select private.is_team_member(team_id, (select auth.uid()))));
create policy inventory_leader_manage on public.inventory_movements for all
  using ((select private.is_team_leader(team_id, (select auth.uid()))))
  with check ((select private.is_team_leader(team_id, (select auth.uid()))));

-- A payment can only be recorded against an order from the same team and a
-- customer belonging to that team. The database remains the final authority.
create or replace function private.record_payment(
  actor uuid,
  requested_order uuid,
  requested_amount integer,
  requested_method text,
  requested_status public.payment_status,
  requested_due_at timestamptz,
  requested_evidence_url text,
  requested_notes text
)
returns jsonb
language plpgsql security definer set search_path = ''
as $$
declare
  order_row public.orders%rowtype;
  payment_row public.payments%rowtype;
  already_paid integer;
begin
  if actor is null then raise exception 'unauthorized'; end if;
  if requested_amount is null or requested_amount <= 0 then raise exception 'invalid_payment_amount'; end if;
  if requested_method not in ('YAPE', 'PLIN', 'EFECTIVO', 'TRANSFERENCIA', 'OTRO') then raise exception 'invalid_payment_method'; end if;

  select * into order_row from public.orders where id = requested_order for update;
  if not found then raise exception 'order_not_found'; end if;
  if order_row.user_id <> actor and not private.is_team_leader(order_row.team_id, actor) then
    raise exception 'order_not_allowed';
  end if;

  select coalesce(sum(amount_cents), 0) into already_paid
  from public.payments
  where order_id = order_row.id and status = 'CONFIRMADO';
  if requested_status = 'CONFIRMADO' and already_paid + requested_amount > order_row.total_cents then
    raise exception 'payment_exceeds_order_balance';
  end if;

  insert into public.payments (
    team_id, order_id, customer_id, recorded_by, amount_cents, method,
    status, due_at, evidence_url, notes
  ) values (
    order_row.team_id, order_row.id, order_row.customer_id, actor,
    requested_amount, requested_method, requested_status, requested_due_at,
    nullif(trim(requested_evidence_url), ''), coalesce(requested_notes, '')
  ) returning * into payment_row;

  if requested_status = 'CONFIRMADO' and already_paid + requested_amount = order_row.total_cents then
    update public.orders set status = 'COBRADO', updated_at = now() where id = order_row.id;
  end if;
  return jsonb_build_object('payment', to_jsonb(payment_row));
end;
$$;

create or replace function public.record_payment(
  p_order_id uuid,
  p_amount_cents integer,
  p_method text,
  p_status public.payment_status default 'PENDIENTE',
  p_due_at timestamptz default null,
  p_evidence_url text default null,
  p_notes text default ''
)
returns jsonb
language plpgsql security invoker set search_path = public
as $$
begin
  if auth.uid() is null then raise exception 'unauthorized'; end if;
  return private.record_payment(auth.uid(), p_order_id, p_amount_cents, upper(trim(p_method)), p_status, p_due_at, p_evidence_url, p_notes);
end;
$$;

revoke all on function private.record_payment(uuid, uuid, integer, text, public.payment_status, timestamptz, text, text) from public;
revoke all on function public.record_payment(uuid, integer, text, public.payment_status, timestamptz, text, text) from public, anon;
grant execute on function public.record_payment(uuid, integer, text, public.payment_status, timestamptz, text, text) to authenticated;
