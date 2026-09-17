# Revisión y correcciones de seguridad

## Alcance

Esta rama endurece `sales-network-app` sin modificar la repo original. El objetivo es conservar Android Compose, Next.js y Supabase, eliminando accesos demo y escrituras financieras no validadas.

## Correcciones aplicadas

### Autenticación Android

- Se eliminó el login local con contraseñas maestras y usuarios demo.
- Se eliminó el almacenamiento local de hashes SHA-256.
- Registro, login y recuperación usan Supabase Auth.
- `allowBackup` está desactivado para no copiar datos locales mediante backup del sistema.
- La clave incluida en el cliente es únicamente la publishable/anon key; `service_role` no aparece en Android.

### RLS y privilegios

- Los helpers de pertenencia se movieron a `private` y reciben `actor` explícito.
- Se revocó la ejecución de los helpers antiguos del esquema público.
- Se agregaron índices para las columnas usadas por RLS y claves foráneas.
- `authenticated` ya no puede escribir directamente pedidos ni líneas.
- Un carrito no acepta productos de otro equipo.

### Operaciones atómicas

- `checkout_cart` bloquea el carrito, valida cliente/productos/precios, crea pedido y líneas, y cierra el carrito en una sola transacción.
- `update_order_status` limita las transiciones y evita editar totales o comisiones.
- `create_team` evita equipos huérfanos.
- `accept_invitation` usa bloqueo de fila para respetar `max_uses` bajo concurrencia.

### Datos y catálogo

- El parser acepta `19.90`, `19,90` y separadores de miles sin multiplicar el precio incorrectamente.
- Si `available` no viene en el payload, se considera disponible; una fuente puede enviar `false` explícito para desactivar un producto.
- Se normalizan SKU con espacios al reconstruir imágenes.

## Verificación antes de producción

1. Aplicar las tres migraciones en un proyecto Supabase de staging.
2. Crear dos usuarios y dos equipos.
3. Confirmar que cada usuario solo ve su equipo.
4. Intentar modificar `orders.total_cents`, `orders.status` y `order_items` directamente: debe fallar.
5. Ejecutar checkout dos veces con la misma idempotency key: debe devolver un solo pedido.
6. Ejecutar aceptación concurrente de una invitación con `max_uses=1`: solo un usuario debe entrar.
7. Ejecutar `supabase db lint`/advisors y `./gradlew :app:testDebugUnitTest :app:lintDebug`.
