# Sales Network Secure — documentación única

**Proyecto:** Sales Network Secure  
**Última revisión:** 2026-09-18  
**Repositorio:** https://github.com/msalcedofernand-afk/sales-network-app-secure

Este es el único documento humano de referencia del proyecto. Aquí se mantienen el estado, la arquitectura, la seguridad, el plan, el diseño, el despliegue y el historial. No se debe crear otro `.md` para cada implementación.

Los archivos `AGENTS.md`, `web/AGENTS.md`, `web/CLAUDE.md` y `.github/pull_request_template.md` se conservan porque son instrucciones operativas para herramientas o Pull Requests, no documentación duplicada del producto.

## 1. Resumen y objetivo

Sales Network Secure es una aplicación Android nativa para equipos de venta directa, acompañada por un catálogo web y servicios Supabase.

El producto permite administrar:

- autenticación y roles;
- equipos y códigos de invitación;
- clientes y contactos;
- catálogo y disponibilidad;
- pedidos y estados;
- pagos, saldos y cobranza;
- campañas y comisiones;
- perfil y rendimiento de la red.

El flujo principal debe funcionar desde el APK sin abrir un panel web ni depender de un WebView. Supabase es la fuente de verdad remota; Room y una cola local serán la base de continuidad offline cuando se complete esa fase.

## 2. Estado actual

### Hecho

- Android Compose con pantallas de login, red, catálogo, clientes, pedidos y perfil.
- Checkout nativo de pedidos en tres pasos: cliente, productos y pago.
- Búsqueda de clientes por nombre/teléfono y productos por nombre/SKU.
- Carrito con cantidades, categorías y stock visible.
- Pagos completos, parciales, Yape, Plin, efectivo y fiado.
- Validación de cliente, productos, monto y saldo.
- Libro de pagos y transiciones de pedidos en el dominio y migración SQL.
- Previews de las pantallas principales, menú inferior, menú de campañas, banner de actualización y pasos del checkout.
- RLS, Auth y funciones SQL protegidas documentadas en el código y las migraciones.
- Pruebas Android, lint y generación de APK verificadas en la rama estable.

### Parcial

- El flujo Android de pedidos todavía usa un repositorio local; falta conectarlo por completo a Supabase y Room.
- El pago parcial se registra localmente; falta auditoría remota e idempotencia en el flujo Android.
- El catálogo tiene búsqueda y categorías, pero la sincronización externa todavía aparece como acción de la aplicación.
- El perfil muestra identidad y cuenta, pero falta el resumen completo de campaña y soporte.
- La red tiene KPI y estados, pero falta búsqueda y filtros para equipos grandes.
- Algunas pantallas todavía contienen textos antiguos con codificación incorrecta.

### Pendiente

- Aplicar y verificar `0004_operational_ledger.sql` en staging.
- Conectar repositorios remotos y cache local Room.
- Crear cola offline, reintentos y resolución de conflictos.
- Añadir borrador recuperable del pedido.
- Implementar idempotencia remota de pedidos y pagos.
- Mostrar deuda e historial directamente en la ficha de cliente.
- Mover la sincronización web a una acción administrativa/backend.
- Retirar porcentajes de comisión escritos directamente en la interfaz.

### No implementar ahora

- Automatización masiva o no oficial de WhatsApp.
- Confirmación automática de Yape/Plin mediante captura, OCR o notificaciones.
- Consulta de score oficial de Infocorp o SBS.
- IA de abandono con datos demo.
- Gamificación basada en reclutamiento, presión o exposición de deudas.
- Inventario multimarca avanzado antes de estabilizar pedidos y pagos.

## 3. Arquitectura

```text
Android Compose
    ↓
ViewModel / estado de pantalla
    ↓
Repository
    ├── Room / cache local y cola offline
    └── Supabase Auth, PostgreSQL, Storage, Realtime y Edge Functions

Next.js web ───────────────→ Supabase
```

### Responsabilidades

- **UI Compose:** muestra estado, recoge acciones y no decide permisos ni reglas financieras.
- **ViewModel:** coordina estado, validación de entrada y eventos de pantalla.
- **Repository:** abstrae cache, red, reintentos y persistencia.
- **Supabase/PostgreSQL:** fuente de verdad, autorización, precios, totales, comisiones y transiciones.
- **Edge Functions:** operaciones protegidas que no deben exponerse directamente al cliente.
- **Web:** catálogo y flujos web complementarios; no es el panel obligatorio del APK.

## 4. Flujo funcional

### Autenticación

- Login y registro por rol mediante Supabase Auth.
- Roles: `ROOT_ADMIN`, `LIDER` y `MIEMBRO`.
- El miembro necesita un código de invitación válido.
- La contraseña puede mostrarse u ocultarse y debe validarse cerca del campo.
- El botón debe bloquearse durante una operación asíncrona.
- No se guardan contraseñas, hashes maestros ni tokens privados en el APK.

### Clientes

- Crear, editar, archivar y buscar por nombre o teléfono.
- Separar teléfono y WhatsApp cuando el modelo lo requiera.
- Guardar dirección, notas, preferencias e historial.
- Mostrar deuda, pedidos pendientes y próxima acción de seguimiento.
- La dirección no equivale automáticamente a una coordenada.
- Llamar, abrir WhatsApp y abrir una ruta solo mediante acción explícita.

### Catálogo

- Buscar por nombre y SKU.
- Filtrar por categoría y marca cuando exista ese dato.
- Mostrar imagen, descripción, precio, disponibilidad y campaña.
- No bloquear ventas porque falle una sincronización externa.
- La importación externa se ejecuta en backend con permisos y auditoría.
- El APK debe poder consultar la última versión local válida.

### Pedidos

El checkout Android activo usa tres pasos:

1. **Cliente:** búsqueda y selección.
2. **Productos:** búsqueda, categorías, stock y carrito.
3. **Pago:** método, monto recibido, saldo y confirmación.

Estados permitidos:

```text
PENDIENTE → CONFIRMADO → COBRADO → ENTREGADO
                 └──────────────→ CANCELADO
```

El backend debe validar propietario, equipo, campaña, precios, disponibilidad, stock, estado e idempotencia.

### Pagos

- Libro separado del pedido.
- Métodos actuales: Yape, Plin, efectivo y pendiente/fiado.
- Pago total o parcial.
- Saldo restante, fecha, usuario y estado de confirmación.
- Comprobante opcional.
- Una captura o notificación no confirma criptográficamente una transferencia.

## 5. Base de datos y API

### Migraciones

Se aplican en orden:

1. `0001_sales_network.sql`: esquema base y RLS inicial.
2. `0002_app_flow_policies.sql`: políticas del carrito y flujo web.
3. `0003_security_hardening.sql`: helpers privados, índices, grants y funciones transaccionales.
4. `0004_operational_ledger.sql`: pagos, movimientos de inventario, reglas de campaña y relaciones operativas.

Antes de producción se debe probar todo en staging con usuarios de dos equipos.

### Separación de datos

- `team_id` separa los equipos.
- `user_id` identifica propietarios y actores.
- RLS limita lecturas y escrituras por pertenencia.
- Los líderes administran miembros, invitaciones, campañas y productos según permisos.
- Los pedidos y líneas no reciben escrituras financieras directas desde el cliente.

### Funciones y Edge Functions

Funciones protegidas existentes o previstas:

- `create-team` / `create_team`;
- `create-invitation`;
- `accept-invitation` / `accept_invitation`;
- `calculate-cart`;
- `checkout-cart` / `checkout_cart`;
- `update_order_status`;
- `publish-campaign`;
- `sync-catalog`.

`checkout_cart` valida cliente, productos, precios y disponibilidad, crea las líneas y cierra el carrito en una transacción. La misma `idempotency_key` no debe crear dos pedidos.

## 6. Seguridad y privacidad

- RLS habilitado en todas las tablas expuestas.
- `service_role`, `sb_secret`, keystores y claves administrativas solo en servidor.
- La clave pública/anon del cliente no sustituye las políticas RLS.
- Las decisiones de autorización se basan en membresías de base de datos, no en `user_metadata` editable.
- Helpers `SECURITY DEFINER` en esquema privado, con actor explícito y grants mínimos.
- No registrar tokens, contraseñas, teléfonos completos ni notas privadas.
- `allowBackup=false` mientras existan datos sensibles locales.
- No enviar mensajes automáticamente sin consentimiento.
- Revisar permisos de ubicación, contactos, notificaciones y archivos antes de publicar.

### Verificación de seguridad antes de producción

1. Crear dos usuarios y dos equipos en staging.
2. Confirmar aislamiento de lecturas entre equipos.
3. Intentar modificar directamente totales, estados y líneas: debe fallar.
4. Repetir checkout con la misma idempotency key: debe devolver un solo pedido.
5. Probar invitaciones concurrentes con `max_uses=1`.
6. Ejecutar lint, advisors de Supabase y pruebas Android.

## 7. Diseño y accesibilidad

### Identidad aprobada

| Token | Valor | Uso |
|---|---|---|
| Petróleo oscuro | `#123D49` | marca, títulos y énfasis |
| Petróleo principal | `#165C59` | acciones y navegación activa |
| Menta | `#D5EEE3` | selección y apoyo |
| Fondo papel | `#F3F6F5` | fondo de aplicación |
| Superficie | `#FFFFFF` | tarjetas y paneles |
| Texto principal | `#192D2C` | contenido principal |
| Texto secundario | `#405552` | metadatos |
| Error | `#B42318` | errores y acciones destructivas |

Reglas principales:

- radios de 10–14 dp para campos y 20–24 dp para tarjetas;
- espaciado de 8, 12, 16, 24 y 32 dp;
- targets táctiles mínimos de 48 dp;
- encabezado breve y una acción principal por región;
- estados de carga, vacío, error, éxito y desconexión;
- texto e icono acompañan al color en estados importantes;
- contenido nunca queda debajo de la barra inferior o del teclado;
- Compose usa barra inferior en teléfono y debe adaptarse a rail en tabletas;
- web usa columnas adaptativas desde 640 y 1024 px;
- probar modo oscuro, letra grande, orientación horizontal y ausencia de Maps/WhatsApp;
- respetar `prefers-reduced-motion` en web.

La firma visual “ruta de venta” se usa en cabeceras, navegación activa, sincronización, estados y pasos del checkout. No se deben mezclar los tokens azules genéricos del antiguo master de diseño con la identidad petróleo/menta aprobada.

## 8. Desarrollo, pruebas y despliegue

### Android

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:lintDebug
.\gradlew.bat :app:assembleDebug
```

APK debug:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### Web

```powershell
cd web
npm ci
npm run build
```

Variables permitidas en cliente:

- `NEXT_PUBLIC_SUPABASE_URL`;
- `NEXT_PUBLIC_SUPABASE_PUBLISHABLE_KEY`.

Nunca colocar `SUPABASE_SERVICE_ROLE_KEY` en una variable `NEXT_PUBLIC_*`.

### Supabase

1. Crear o seleccionar proyecto de staging.
2. Aplicar migraciones con Supabase CLI en orden.
3. Configurar correo, verificación y recuperación de Auth.
4. Ejecutar pruebas de RLS con dos equipos.
5. Revisar grants y advisors.
6. Aplicar a producción solo después de validar staging.

### GitHub y ramas

- `main` es la rama estable publicada.
- `beta` se reserva para validación cuando el flujo exista.
- Usar `feature/*`, `fix/*` y `chore/*` para cambios aislados.
- Ejecutar pruebas antes de hacer Pull Request.
- No incluir `.env`, keystores, claves administrativas ni datos reales.
- El repositorio actual es `msalcedofernand-afk/sales-network-app-secure`.

## 9. Actualizaciones del APK

La app no descarga ni ejecuta código desde GitHub. Consulta un manifiesto público de actualización y abre el APK publicado si el `versionCode` es superior.

- `main` usa `updates/stable.json` para producción.
- `beta` usa `updates/beta.json` para validación.
- El manifiesto contiene versión, canal, URL, notas y si la actualización es obligatoria.
- La app usa timeouts cortos y no bloquea el acceso si no hay conexión.

Para publicar:

1. Generar APK.
2. Ejecutar pruebas, lint y build web.
3. Actualizar manifiesto, versión y notas.
4. Validar en beta.
5. Promover a main después de la revisión.

## 10. Catálogo de referencia

El archivo `supabase/seed/vive_catalog.json` es una captura de un catálogo público y no debe tratarse como inventario real.

Para regenerarlo:

```bash
python supabase/seed/scrape_vive.py
```

Antes de importar:

- validar precios y disponibilidad;
- confirmar permisos de imágenes y contenido;
- conservar URL de origen para auditoría;
- cargar mediante función administrativa protegida;
- no insertar la captura directamente en producción.

## 11. Plan de implementación

### Fase 0 — Limpieza

- [ ] Normalizar textos a UTF-8.
- [ ] Aislar o eliminar datos demo del flujo real.
- [ ] Centralizar componentes visuales.
- [ ] Unificar estados de pantalla.

### Fase 1 — Checkout

- [x] Cliente con búsqueda completa.
- [x] Productos con búsqueda, categorías y stock.
- [x] Carrito y cantidades.
- [x] Pago total, parcial y fiado.
- [x] Validaciones y previews.
- [ ] Borrador recuperable fuera del diálogo.
- [ ] Carga asíncrona e idempotencia remota.

### Fase 2 — Datos reales y offline

- [ ] Repositorios Supabase para Android.
- [ ] Room como cache local.
- [ ] Cola de operaciones pendientes.
- [ ] Reintentos y conflictos.
- [ ] Indicador de sincronización.

### Fase 3 — Clientes y cobranza

- [ ] Deuda e historial en ficha de cliente.
- [ ] Seguimiento y próxima acción.
- [ ] Dirección libre y ubicación opcional.
- [ ] Abonos desde pedido y ficha de cliente.

### Fase 4 — Catálogo y administración

- [ ] Sincronización externa solo en backend/administración.
- [ ] Catálogo local disponible sin web.
- [ ] Auditoría de sincronizaciones.
- [ ] Promociones y disponibilidad semántica.

### Fase 5 — Red y perfil

- [ ] Búsqueda y filtros de integrantes.
- [ ] Acciones de contacto por integrante.
- [ ] Meta, progreso y comisión proyectada.
- [ ] Soporte, versión y preferencias.

## 12. Criterios de aceptación

Una funcionalidad está lista solo si:

1. tiene caso de uso, permisos y propietario de datos;
2. usa datos reales o marca claramente el estado de preview;
3. tiene carga, vacío, error y reintento;
4. explica qué ocurre sin conexión;
5. no duplica pedidos ni pagos al reintentar;
6. protege dinero, permisos, stock y estados en backend;
7. no depende de WebView para el flujo principal;
8. respeta privacidad y consentimiento;
9. pasa pruebas, lint y build;
10. actualiza esta documentación única.

## 13. Historial resumido

- **2026-09-18:** documentación consolidada; checkout nativo de tres pasos; previews de pantallas y menús; APK debug validado.
- **2026-09-17:** endurecimiento de Auth, RLS, operaciones SQL y eliminación de cuentas demo.
- **2026-09-08:** rediseño Android, navegación flotante, catálogo, clientes y pedidos.
- **2026-09-08:** unificación visual entre web y Android y primeros flujos Supabase.

Los detalles antiguos se conservan en el historial de Git, no en documentos duplicados.

## 14. Regla de mantenimiento

Después de cada cambio:

1. actualizar el estado correspondiente en este archivo;
2. agregar una línea al historial resumido si el cambio es relevante;
3. registrar pruebas ejecutadas;
4. no crear otro roadmap, guía o changelog paralelo.

