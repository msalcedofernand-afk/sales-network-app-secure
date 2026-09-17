# Base de datos

Las migraciones se aplican en orden:

1. `supabase/migrations/0001_sales_network.sql`: esquema base y RLS inicial.
2. `supabase/migrations/0002_app_flow_policies.sql`: políticas mínimas del carrito.
3. `supabase/migrations/0003_security_hardening.sql`: helpers privados, índices, privilegio mínimo y funciones transaccionales.

## Modelo de acceso

- `team_id` separa los datos de cada equipo.
- `user_id` identifica al propietario de perfiles, carritos y pedidos.
- Solo los líderes administran miembros, invitaciones, campañas y productos.
- Solo los líderes pueden leer códigos de invitación; aceptar un código se hace mediante una función atómica.
- Los pedidos son de solo lectura para el cliente. Checkout y cambios de estado pasan por funciones SQL validadas.
- Un `cart_item` solo puede apuntar a un producto del mismo equipo del carrito.

## Funciones públicas permitidas

- `create_team(p_name)` crea equipo y membresía de líder en una transacción.
- `accept_invitation(p_code)` bloquea la invitación y consume un uso de forma atómica.
- `checkout_cart(p_cart_id, p_customer_id, p_idempotency_key)` calcula precios desde la base y crea pedido, líneas y cierre del carrito en una transacción.
- `update_order_status(p_order_id, p_status)` aplica únicamente transiciones permitidas.

No se debe conceder `INSERT`, `UPDATE` o `DELETE` directo sobre `orders` ni `order_items` a `authenticated`. Después de desplegar, ejecutar pruebas de aislamiento con usuarios de dos equipos y revisar advisors de Supabase.
