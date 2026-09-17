-- Security hardening: tenant-safe RLS, transactional checkout and least privilege.
-- Apply after 0001_sales_network.sql and 0002_app_flow_policies.sql.

create schema if not exists private;
revoke all on schema private from public;

create or replace function private.is_team_member(target_team uuid, actor uuid)
returns boolean
language sql stable security definer set search_path = ''
as $$
  select actor is not null and exists (
    select 1 from public.team_members
    where team_id = target_team and user_id = actor
  );
$$;

create or replace function private.is_team_leader(target_team uuid, actor uuid)
returns boolean
language sql stable security definer set search_path = ''
as $$
  select actor is not null and exists (
    select 1 from public.team_members
    where team_id = target_team and user_id = actor and role = 'LIDER'
  );
$$;

revoke all on function private.is_team_member(uuid, uuid) from public;
revoke all on function private.is_team_leader(uuid, uuid) from public;
grant usage on schema private to authenticated;
grant execute on function private.is_team_member(uuid, uuid) to authenticated;
grant execute on function private.is_team_leader(uuid, uuid) to authenticated;

-- The old public helpers were security-definer functions in an exposed schema.
-- Policies below no longer use them, and clients must not call them directly.
revoke all on function public.is_team_member(uuid) from public, anon, authenticated, service_role;
revoke all on function public.is_team_leader(uuid) from public, anon, authenticated, service_role;
revoke all on function public.handle_new_user() from public, anon, authenticated, service_role;

-- Indexes used by RLS predicates and foreign-key joins.
create index if not exists team_members_user_team_idx on public.team_members (user_id, team_id);
create index if not exists invitations_team_status_idx on public.invitations (team_id, status);
create index if not exists campaigns_team_idx on public.campaigns (team_id);
create index if not exists products_team_idx on public.products (team_id);
create index if not exists product_images_product_idx on public.product_images (product_id);
create index if not exists customers_owner_team_idx on public.customers (owner_user_id, team_id);
create index if not exists addresses_customer_idx on public.addresses (customer_id);
create index if not exists carts_user_team_status_idx on public.carts (user_id, team_id, status);
create index if not exists cart_items_cart_idx on public.cart_items (cart_id);
create index if not exists orders_user_created_idx on public.orders (user_id, created_at desc);
create index if not exists orders_team_idx on public.orders (team_id);
create index if not exists order_items_order_idx on public.order_items (order_id);
create index if not exists sync_events_team_created_idx on public.sync_events (team_id, created_at desc);

-- Replace policies that referenced public security-definer helpers.
alter policy teams_member_read on public.teams
  using ((select private.is_team_member(id, (select auth.uid()))));
drop policy if exists profiles_team_member_read on public.profiles;
create policy profiles_team_member_read on public.profiles for select
  using (exists (
    select 1 from public.team_members viewer
    join public.team_members target on target.team_id = viewer.team_id
    where viewer.user_id = (select auth.uid()) and target.user_id = profiles.id
  ));
alter policy teams_leader_manage on public.teams
  using ((select private.is_team_leader(id, (select auth.uid()))))
  with check ((select private.is_team_leader(id, (select auth.uid()))));
alter policy team_members_read on public.team_members
  using ((select private.is_team_member(team_id, (select auth.uid()))));
alter policy team_members_leader_manage on public.team_members
  using ((select private.is_team_leader(team_id, (select auth.uid()))))
  with check ((select private.is_team_leader(team_id, (select auth.uid()))));
drop policy if exists invitations_member_read on public.invitations;
create policy invitations_leader_read on public.invitations for select
  using ((select private.is_team_leader(team_id, (select auth.uid()))));
alter policy invitations_leader_manage on public.invitations
  using ((select private.is_team_leader(team_id, (select auth.uid()))))
  with check ((select private.is_team_leader(team_id, (select auth.uid()))));
alter policy campaigns_member_read on public.campaigns
  using ((select private.is_team_member(team_id, (select auth.uid()))));
alter policy campaigns_leader_manage on public.campaigns
  using ((select private.is_team_leader(team_id, (select auth.uid()))))
  with check ((select private.is_team_leader(team_id, (select auth.uid()))));
alter policy products_member_read on public.products
  using ((select private.is_team_member(team_id, (select auth.uid()))));
alter policy products_leader_manage on public.products
  using ((select private.is_team_leader(team_id, (select auth.uid()))))
  with check ((select private.is_team_leader(team_id, (select auth.uid()))));
alter policy images_member_read on public.product_images
  using (exists (
    select 1 from public.products p
    where p.id = product_id and (select private.is_team_member(p.team_id, (select auth.uid())))
  ));
alter policy customers_owner_or_leader on public.customers
  using (owner_user_id = (select auth.uid()) or (select private.is_team_leader(team_id, (select auth.uid()))))
  with check ((select private.is_team_member(team_id, (select auth.uid()))) and
    (owner_user_id = (select auth.uid()) or (select private.is_team_leader(team_id, (select auth.uid())))));
alter policy sync_leader_read on public.sync_events
  using ((select private.is_team_leader(team_id, (select auth.uid()))));

-- A cart may only contain products from its own team.
drop policy if exists cart_items_owner on public.cart_items;
create policy cart_items_owner on public.cart_items for all
  using (exists (
    select 1 from public.carts c
    join public.products p on p.id = cart_items.product_id and p.team_id = c.team_id
    where c.id = cart_id and c.user_id = (select auth.uid())
  ))
  with check (exists (
    select 1 from public.carts c
    join public.products p on p.id = cart_items.product_id and p.team_id = c.team_id
    where c.id = cart_id and c.user_id = (select auth.uid())
  ));

drop policy if exists carts_owner on public.carts;
create policy carts_owner_select on public.carts for select
  using (user_id = (select auth.uid()) and (select private.is_team_member(team_id, (select auth.uid()))));
create policy carts_owner_insert on public.carts for insert
  with check (user_id = (select auth.uid()) and (select private.is_team_member(team_id, (select auth.uid()))) and status = 'ACTIVE');

-- Orders become append-only from the client. Only the functions below can create
-- or transition them, so totals and commissions cannot be forged by the app.
drop policy if exists orders_owner_insert on public.orders;
drop policy if exists orders_owner_update on public.orders;
drop policy if exists order_items_owner_insert on public.order_items;
alter policy orders_owner_or_leader on public.orders
  using (user_id = (select auth.uid()) or (select private.is_team_leader(team_id, (select auth.uid()))));
alter policy order_items_owner_or_leader on public.order_items
  using (exists (
    select 1 from public.orders o
    where o.id = order_id and (o.user_id = (select auth.uid()) or (select private.is_team_leader(o.team_id, (select auth.uid()))))
  ));

revoke insert, update, delete on public.orders, public.order_items from authenticated;
grant select on public.orders, public.order_items to authenticated;
revoke update, delete on public.carts from authenticated;
grant select, insert on public.carts to authenticated;

-- All checkout writes happen in one short database transaction. The user id is
-- passed from the public wrapper and checked again inside this definer function.
create or replace function private.checkout_cart(
  actor uuid,
  requested_cart uuid,
  requested_customer uuid,
  request_key text
)
returns jsonb
language plpgsql security definer set search_path = ''
as $$
declare
  cart_row public.carts%rowtype;
  order_row public.orders%rowtype;
  total integer;
begin
  if actor is null then raise exception 'unauthorized'; end if;
  if request_key is null or length(trim(request_key)) not between 8 and 128 then
    raise exception 'invalid_idempotency_key';
  end if;

  select * into order_row from public.orders
    where user_id = actor and idempotency_key = trim(request_key)
    limit 1;
  if found then
    return jsonb_build_object('order', to_jsonb(order_row), 'existing', true);
  end if;

  select * into cart_row from public.carts
    where id = requested_cart and user_id = actor and status = 'ACTIVE'
    for update;
  if not found then raise exception 'active_cart_not_found'; end if;

  if requested_customer is not null and not exists (
    select 1 from public.customers c
    where c.id = requested_customer and c.team_id = cart_row.team_id
      and (c.owner_user_id = actor or private.is_team_leader(c.team_id, actor))
  ) then
    raise exception 'customer_not_allowed';
  end if;

  if exists (
    select 1 from public.cart_items ci
    left join public.products p on p.id = ci.product_id
    where ci.cart_id = cart_row.id
      and (p.id is null or p.team_id <> cart_row.team_id or not p.available)
  ) then
    raise exception 'cart_contains_invalid_or_unavailable_product';
  end if;

  select coalesce(sum(ci.quantity * p.price_cents), 0) into total
    from public.cart_items ci
    join public.products p on p.id = ci.product_id
    where ci.cart_id = cart_row.id and p.team_id = cart_row.team_id and p.available;
  if total <= 0 then raise exception 'cart_empty'; end if;

  insert into public.orders (team_id, user_id, customer_id, total_cents, commission_cents, idempotency_key)
    values (cart_row.team_id, actor, requested_customer, total, 0, trim(request_key))
    on conflict (user_id, idempotency_key) do nothing
    returning * into order_row;
  if not found then
    select * into order_row from public.orders
      where user_id = actor and idempotency_key = trim(request_key) limit 1;
    return jsonb_build_object('order', to_jsonb(order_row), 'existing', true);
  end if;

  insert into public.order_items (order_id, product_id, sku, product_name, quantity, unit_price_cents)
    select order_row.id, p.id, p.sku, p.name, ci.quantity, p.price_cents
    from public.cart_items ci
    join public.products p on p.id = ci.product_id
    where ci.cart_id = cart_row.id and p.team_id = cart_row.team_id and p.available;
  update public.carts set status = 'CONVERTED', updated_at = now() where id = cart_row.id;
  return jsonb_build_object('order', to_jsonb(order_row), 'existing', false);
end;
$$;

create or replace function public.checkout_cart(p_cart_id uuid, p_customer_id uuid, p_idempotency_key text)
returns jsonb
language plpgsql security invoker set search_path = public
as $$
begin
  if auth.uid() is null then raise exception 'unauthorized'; end if;
  return private.checkout_cart(auth.uid(), p_cart_id, p_customer_id, p_idempotency_key);
end;
$$;

-- Status transitions are also server-controlled; clients cannot edit totals or
-- move orders between teams with a generic update request.
create or replace function private.update_order_status(actor uuid, requested_order uuid, next_status public.order_status)
returns jsonb
language plpgsql security definer set search_path = ''
as $$
declare
  order_row public.orders%rowtype;
  leader boolean;
begin
  if actor is null then raise exception 'unauthorized'; end if;
  select * into order_row from public.orders where id = requested_order for update;
  if not found then raise exception 'order_not_found'; end if;
  leader := private.is_team_leader(order_row.team_id, actor);
  if order_row.user_id <> actor and not leader then raise exception 'order_not_allowed'; end if;
  if next_status = 'CONFIRMADO' and order_row.status <> 'PENDIENTE' then raise exception 'invalid_status_transition'; end if;
  if next_status = 'COBRADO' and order_row.status <> 'CONFIRMADO' then raise exception 'invalid_status_transition'; end if;
  if next_status = 'ENTREGADO' and order_row.status <> 'COBRADO' then raise exception 'invalid_status_transition'; end if;
  if next_status = 'CANCELADO' and order_row.status in ('ENTREGADO', 'CANCELADO') then raise exception 'invalid_status_transition'; end if;
  update public.orders set status = next_status where id = order_row.id returning * into order_row;
  return to_jsonb(order_row);
end;
$$;

create or replace function public.update_order_status(p_order_id uuid, p_status public.order_status)
returns jsonb
language plpgsql security invoker set search_path = public
as $$
begin
  if auth.uid() is null then raise exception 'unauthorized'; end if;
  return private.update_order_status(auth.uid(), p_order_id, p_status);
end;
$$;

revoke all on function private.checkout_cart(uuid, uuid, text) from public;
revoke all on function private.update_order_status(uuid, uuid, public.order_status) from public;
revoke all on function public.checkout_cart(uuid, uuid, text) from public, anon;
revoke all on function public.update_order_status(uuid, public.order_status) from public, anon;
grant execute on function public.checkout_cart(uuid, uuid, text) to authenticated;
grant execute on function public.update_order_status(uuid, public.order_status) to authenticated;

create or replace function private.create_team(actor uuid, team_name text)
returns jsonb
language plpgsql security definer set search_path = ''
as $$
declare
  team_row public.teams%rowtype;
begin
  if actor is null then raise exception 'unauthorized'; end if;
  if length(trim(team_name)) not between 2 and 120 then raise exception 'invalid_team_name'; end if;
  insert into public.teams (name, created_by) values (trim(team_name), actor) returning * into team_row;
  insert into public.team_members (team_id, user_id, role) values (team_row.id, actor, 'LIDER');
  return jsonb_build_object('team', to_jsonb(team_row));
end;
$$;

create or replace function public.create_team(p_name text)
returns jsonb
language plpgsql security invoker set search_path = public
as $$
begin
  if auth.uid() is null then raise exception 'unauthorized'; end if;
  return private.create_team(auth.uid(), p_name);
end;
$$;

create or replace function private.accept_invitation(actor uuid, invitation_code text)
returns jsonb
language plpgsql security definer set search_path = ''
as $$
declare
  invitation_row public.invitations%rowtype;
  already_member boolean;
begin
  if actor is null then raise exception 'unauthorized'; end if;
  select * into invitation_row from public.invitations
    where code = upper(trim(invitation_code)) and status = 'ACTIVE'
    for update;
  if not found or invitation_row.expires_at <= now() or invitation_row.uses >= invitation_row.max_uses then
    raise exception 'invitation_invalid_or_expired';
  end if;
  select exists (
    select 1 from public.team_members where team_id = invitation_row.team_id and user_id = actor
  ) into already_member;
  if already_member then
    return jsonb_build_object('team_id', invitation_row.team_id, 'existing', true);
  end if;
  insert into public.team_members (team_id, user_id, role) values (invitation_row.team_id, actor, 'MIEMBRO');
  update public.invitations
    set uses = uses + 1,
        status = case when uses + 1 >= max_uses then 'USED'::public.invitation_status else status end
    where id = invitation_row.id;
  return jsonb_build_object('team_id', invitation_row.team_id, 'existing', false);
end;
$$;

create or replace function public.accept_invitation(p_code text)
returns jsonb
language plpgsql security invoker set search_path = public
as $$
begin
  if auth.uid() is null then raise exception 'unauthorized'; end if;
  return private.accept_invitation(auth.uid(), p_code);
end;
$$;

revoke all on function private.create_team(uuid, text) from public;
revoke all on function private.accept_invitation(uuid, text) from public;
revoke all on function public.create_team(text) from public, anon;
revoke all on function public.accept_invitation(text) from public, anon;
grant execute on function public.create_team(text) to authenticated;
grant execute on function public.accept_invitation(text) to authenticated;
