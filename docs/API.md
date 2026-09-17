# API

Las Edge Functions aceptan JSON y devuelven `{ error: string }` en fallos. Las funciones protegidas exigen `Authorization: Bearer <supabase_access_token>`.

Implementadas:

- `create-invitation`: líder → `{ team_id, expires_at, max_uses }`.
- `create-team`: usuario autenticado → `{ name }`.
- `accept-invitation`: usuario → `{ code }`.
- `calculate-cart`: usuario → `{ cart_id }`.
- `checkout-cart`: usuario → `{ cart_id, customer_id, idempotency_key }`.

`checkout-cart` delega en `checkout_cart`, que valida propietario, equipo, cliente, disponibilidad y precios, y realiza todas las escrituras de forma atómica. Repetir la misma `idempotency_key` devuelve el pedido existente.

La pantalla de pedidos usa `supabase.rpc("update_order_status", { p_order_id, p_status })`; la base rechaza transiciones inválidas y cambios de totales.

`create-team` y `accept-invitation` usan funciones SQL transaccionales. La invitación se bloquea con `FOR UPDATE`, por lo que `max_uses` no se puede superar con solicitudes concurrentes.

Los conectores de rutas y mensajería devuelven `501` hasta configurar un proveedor autorizado.
