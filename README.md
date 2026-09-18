# Sales Network Secure

Aplicación Android Compose y catálogo Next.js para equipos de venta, con Supabase Auth, PostgreSQL/RLS y Edge Functions.

## Estado y plan

El estado del producto, lo implementado, lo pendiente, las prioridades, los límites y los criterios de aceptación se mantienen en un único documento maestro:

- [`docs/PLAN_IMPLEMENTACION.md`](docs/PLAN_IMPLEMENTACION.md)

No se debe crear un roadmap nuevo por cada implementación. Después de cada cambio se actualiza el documento maestro y su registro de cambios.

## Alcance del producto

Esta copia endurecida elimina cuentas demo, contraseñas maestras y escrituras financieras directas. La base segura está documentada en [`docs/SECURITY_HARDENING.md`](docs/SECURITY_HARDENING.md).

## Estructura

- `app/`: aplicación Android Compose.
- `web/`: catálogo Next.js y flujos de carrito, clientes y pedidos.
- `supabase/migrations/`: esquema y políticas RLS.
- `supabase/functions/`: endpoints autenticados.
- `docs/`: plan maestro y referencias técnicas.

## Desarrollo web

```powershell
cd web
npm ci
npm run build
```

Configura `NEXT_PUBLIC_SUPABASE_URL` y `NEXT_PUBLIC_SUPABASE_PUBLISHABLE_KEY`. Nunca pongas `SUPABASE_SERVICE_ROLE_KEY` en variables `NEXT_PUBLIC_*`.

## Android

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:lintDebug
.\gradlew.bat :app:assembleDebug
```

## Supabase

Aplica las migraciones en orden y configura Auth con verificación de correo y recuperación. Usa un proyecto de staging para validar el aislamiento entre dos equipos antes de producción.
