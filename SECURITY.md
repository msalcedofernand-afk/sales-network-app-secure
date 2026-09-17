# Seguridad

- Todas las tablas expuestas tienen RLS habilitado.
- `service_role` y `sb_secret` solo pueden vivir en servidor; nunca se compilan en Android o Next.js.
- La APK no contiene cuentas demo, contraseñas maestras ni hashes de contraseñas locales.
- Android usa Supabase Auth; la recuperación envía un enlace por correo y no cambia contraseñas desde preferencias locales.
- Las decisiones de autorización se basan en membresías de base de datos, no en `user_metadata` editable.
- `orders` y `order_items` son append-only para el cliente; los precios, totales, comisiones y transiciones se calculan o validan en SQL.
- Checkout, creación de equipos y aceptación de invitaciones son transacciones atómicas.
- Los helpers `SECURITY DEFINER` viven en `private`, reciben el actor explícitamente y no están expuestos por Data API.
- No registrar tokens, contraseñas, teléfonos completos ni notas privadas.
- Mantener `allowBackup=false` mientras existan datos de sesión o clientes en el almacenamiento local.
- Después de cada cambio SQL: ejecutar pruebas de aislamiento con dos equipos, `supabase db lint`/advisors y revisión de grants.
