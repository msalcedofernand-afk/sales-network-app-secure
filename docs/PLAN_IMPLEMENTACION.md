# Plan maestro de implementación

**Proyecto:** Sales Network Secure  
**Última revisión:** 2026-09-18  
**Documento de referencia:** este archivo es la única fuente de verdad para el estado del producto, las prioridades y las decisiones de implementación.

## Cómo se usa este documento

No se debe crear un documento nuevo por cada cambio. Después de cada implementación se actualizan aquí:

1. el estado de la funcionalidad;
2. la evidencia técnica o los archivos afectados;
3. los criterios de aceptación;
4. la fecha y el resultado de las pruebas;
5. el registro de cambios al final del documento.

Los documentos `API.md`, `DATABASE.md`, `ARCHITECTURE.md`, `DEPLOYMENT.md`, `SECURITY_HARDENING.md` y `UI_UX_GUIA.md` contienen referencias técnicas específicas. No deben duplicar este roadmap.

### Estados

- **Hecho:** implementado y verificado en el código o en una prueba.
- **Parcial:** existe una primera versión, pero todavía no cumple el flujo completo.
- **Pendiente:** definido, pero aún no implementado.
- **Bloqueado:** necesita una decisión, credencial, servicio externo o validación que no está disponible.
- **No implementar ahora:** se conserva como límite de seguridad, negocio o alcance.

## Objetivo del producto

Crear una aplicación Android nativa para venta directa que permita administrar clientes, catálogo, pedidos, pagos y equipos desde el mismo teléfono.

El uso normal del APK debe funcionar sin abrir un panel web ni depender de una página externa. Supabase será la fuente de verdad remota; la aplicación podrá usar una base local y sincronizar cuando vuelva la conexión.

## Decisiones y límites permanentes

- La interfaz Android se construye con Jetpack Compose.
- No se usará WebView como panel de control.
- La sincronización de un catálogo externo, si se conserva, se ejecutará en backend y será una acción administrativa; no será necesaria para vender.
- Nunca se incluirá `service_role`, contraseñas maestras ni claves administrativas en el APK.
- El backend valida permisos, dinero, stock, estados e idempotencia; la interfaz no es una barrera de seguridad.
- El dinero se guarda en centavos o en un tipo exacto definido por el backend, no en cálculos financieros repartidos por las pantallas.
- Los pagos de Yape, Plin y efectivo se registran como operaciones. Una captura, OCR o notificación no prueba por sí sola que una transferencia fue confirmada.
- WhatsApp, teléfono y Maps se abrirán mediante acciones explícitas del usuario. No se automatizarán mensajes masivos desde la aplicación personal.
- Los textos del proyecto deben conservarse en UTF-8 y revisarse antes de publicar.

## Estado actual

### Base de aplicación y seguridad

| Área | Estado | Situación actual | Siguiente acción |
|---|---|---|---|
| Android Compose | Hecho | Existe el flujo principal nativo y las pantallas de catálogo, clientes, pedidos, red y perfil. | Mantener separación entre UI, dominio y datos. |
| Autenticación | Parcial | Hay login, registro por rol, código de invitación, recuperación y mostrar/ocultar contraseña. | Conectar estados de carga, validación de código y errores de forma asíncrona en la UI. |
| Seguridad base | Parcial | Se documentaron RLS, funciones protegidas y límites de secretos. | Aplicar y probar migraciones en staging con usuarios de equipos distintos. |
| Codificación | Pendiente | Hay textos con mojibake como `Ã©`, `Ã³` y `Ã±` en varias pantallas y documentos. | Normalizar archivos a UTF-8 y revisar textos visibles. |
| Pruebas Android | Hecho | Las pruebas unitarias, compilación, lint y APK debug pasan en la última verificación. | Repetirlas después de cada fase de cambios. |

### Datos y sincronización

| Área | Estado | Situación actual | Siguiente acción |
|---|---|---|---|
| Supabase | Parcial | Hay esquema y migraciones, incluida la migración operativa de pagos y pedidos. | Aplicar en staging; producción requiere revisión y credenciales del proyecto. |
| Repositorios remotos | Pendiente | Parte del flujo Android todavía usa repositorios en memoria o datos de ejemplo. | Sustituirlos por repositorios remotos con cache local. |
| Cache offline | Pendiente | No existe todavía un flujo completo con Room y cola de operaciones. | Diseñar sincronización, reintentos, conflictos e idempotencia. |
| Catálogo sin web | Parcial | Android tiene búsqueda y categorías, pero todavía muestra una acción de sincronización web. | Mover la sincronización a administración/backend y permitir vender con datos cacheados. |
| Migración de producción | Bloqueado | La migración está en el repositorio, pero no se aplicó al proyecto real. | Ejecutar primero en staging y validar RLS. |

### Experiencia de usuario

| Área | Estado | Situación actual | Siguiente acción |
|---|---|---|---|
| Registro | Parcial | El formulario funciona, pero concentra roles, datos y código en una sola tarjeta. | Usar tarjetas de rol, explicar cada opción y validar el código en vivo. |
| Red de vendedores | Parcial | Hay KPI, equipos expandibles, estados e invitación por WhatsApp. | Añadir buscador, filtros y acciones de contacto por integrante. |
| Clientes | Parcial | Hay alta, listado, estado vacío, llamada, WhatsApp y navegación. | Añadir búsqueda, deuda, pedidos pendientes y dirección flexible. |
| Catálogo | Parcial | Hay búsqueda, SKU, categorías, imágenes y acción de carrito. | Añadir botón de borrado, conteos, stock/oferta visible y sacar sincronización web de la búsqueda. |
| Pedidos | Parcial | El checkout nativo de tres pasos ya está activo para cliente, productos y pago; todavía usa el repositorio local y no tiene sincronización remota. | Conectar borradores, carga asíncrona, idempotencia y backend. |
| Perfil | Parcial | Muestra identidad, correo, rol, código, fecha y cerrar sesión. | Añadir resumen de campaña, soporte, configuración y versión. |
| Estados de pantalla | Parcial | Algunas pantallas tienen estados vacíos, pero no existe una convención completa. | Unificar carga, vacío, error, éxito, desconexión y reintento. |

### Operación comercial

| Área | Estado | Situación actual | Siguiente acción |
|---|---|---|---|
| Estados de pedido | Hecho en dominio | Se definieron transiciones válidas y pruebas. | Hacer que backend y base de datos sean la autoridad final. |
| Libro de pagos | Parcial | Existe modelo, migración, registro de abonos y saldo pendiente. | Integrarlo al checkout, auditoría y repositorio remoto. |
| Comisiones | Parcial | Hay reglas y campos iniciales, pero algunas pantallas todavía usan porcentajes fijos. | Leer reglas de campaña desde datos y retirar cálculos financieros de Compose. |
| Stock | Parcial | El producto tiene disponibilidad básica y existe movimiento de inventario en la migración. | Validar stock al confirmar y definir reservas, devoluciones y ajustes. |
| Ticket de WhatsApp | Parcial | Se puede compartir información básica del pedido. | Generar un formato profesional con detalle, total, pago y saldo. |
| Idempotencia | Pendiente | Está definida como requisito, pero debe estar integrada en la operación real. | Usar una clave única por intento de creación y reintentar sin duplicar. |

## Plan de implementación por fases

### Fase 0 — Orden y base verificable

**Objetivo:** dejar el proyecto legible y evitar que las mejoras se construyan sobre estados falsos.

- [ ] Normalizar textos y archivos a UTF-8.
- [ ] Eliminar o aislar datos demo del flujo real.
- [ ] Confirmar que los documentos técnicos describan el comportamiento actual.
- [ ] Centralizar colores, tipografías, espaciado y componentes comunes.
- [ ] Definir una convención única para carga, vacío, error, éxito y desconexión.
- [ ] Ejecutar `test`, `lint` y `assembleDebug`.

**Terminado cuando:** no haya mojibake visible, el build pase y cada pantalla indique qué ocurre cuando no hay datos o conexión.

### Fase 1 — Pedidos nativos tipo checkout

**Objetivo:** resolver el problema más importante de uso diario.

#### Paso 1: cliente

- [x] Reemplazar la lista limitada por `take(4)` en el flujo activo por búsqueda y lista completa.
- [ ] Mostrar nombre, teléfono, deuda y pedidos pendientes.
- [ ] Permitir crear un cliente sin perder el pedido en curso.
- [ ] Guardar la selección en un borrador recuperable.

#### Paso 2: productos

- [x] Buscar por nombre y SKU.
- [x] Filtrar por categoría.
- [x] Mostrar precio y disponibilidad.
- [x] Agregar y quitar unidades desde la tarjeta.
- [x] Mostrar carrito y total durante el flujo.

#### Paso 3: pago y confirmación

- [x] Mostrar total, monto recibido y saldo.
- [x] Permitir pago completo, parcial o pendiente.
- [x] Seleccionar Yape, Plin, efectivo o fiado.
- [ ] Calcular la ganancia desde reglas de campaña.
- [x] Bloquear confirmación si no hay cliente, productos o monto válido.
- [ ] Mostrar carga y evitar doble pulsación.
- [ ] Confirmar el pedido una sola vez mediante idempotencia.
- [ ] Generar ticket listo para compartir.

**Terminado cuando:** un vendedor pueda crear un pedido real desde el teléfono con listas grandes, pago parcial, saldo, validación, reintento seguro y sin abrir una web.

### Fase 2 — Datos reales y continuidad offline

- [ ] Aplicar `0004_operational_ledger.sql` en staging.
- [ ] Validar RLS con administrador, líder y miembro de equipos distintos.
- [ ] Crear repositorios remotos para clientes, productos, pedidos y pagos.
- [ ] Añadir Room como cache local.
- [ ] Crear cola de operaciones pendientes.
- [ ] Resolver conflictos usando `updated_at` y reglas explícitas.
- [ ] Mostrar última sincronización y errores recuperables.

**Terminado cuando:** el APK pueda consultar datos cacheados sin conexión y sincronizar cambios sin duplicar pedidos ni pagos.

### Fase 3 — Clientes y cobranza

- [ ] Añadir buscador por nombre, teléfono y zona.
- [ ] Mostrar saldo pendiente, atraso y pedidos activos.
- [ ] Añadir historial del cliente.
- [ ] Permitir registrar seguimiento y próxima acción.
- [ ] Permitir dirección libre y ubicación opcional, sin obligar a usar una zona fija.
- [ ] Confirmar abonos desde el pedido y desde la ficha del cliente.

**Terminado cuando:** una vendedora pueda decidir si entregar o cobrar consultando la ficha completa del cliente.

### Fase 4 — Catálogo local y administración

- [ ] Mantener catálogo local disponible aunque falle la fuente externa.
- [ ] Mover “Sincronizar catálogo externo” a una acción administrativa.
- [ ] Registrar fecha, resultado y error de cada sincronización.
- [ ] Mostrar stock y promociones con texto, color e icono.
- [ ] No bloquear pedidos por una sincronización pendiente.

**Terminado cuando:** el catálogo sea útil para vender sin WebView ni scraping desde el teléfono.

### Fase 5 — Red y perfil

- [ ] Añadir buscador y filtros por estado en la red.
- [ ] Añadir llamada y WhatsApp por integrante, respetando permisos.
- [ ] Mostrar ventas, pedidos pendientes y último contacto.
- [ ] Añadir en perfil campaña activa, meta, progreso y comisión proyectada.
- [ ] Añadir soporte, versión y configuración de tema.

**Terminado cuando:** líder y miembro vean solamente los datos que les corresponden y tengan acciones claras para operar.

### Fase 6 — Pulido y escalabilidad

- [ ] Adaptar navegación a tabletas y plegables.
- [ ] Revisar accesibilidad, foco, contraste y tamaños táctiles.
- [ ] Añadir animaciones solo donde ayuden a entender el cambio.
- [ ] Preparar componentes de diseño reutilizables.
- [ ] Medir rendimiento con listas grandes.

## Funciones que no se implementarán ahora

- Automatización masiva de WhatsApp o uso no oficial de la aplicación personal.
- Confirmación automática de Yape/Plin mediante captura, OCR o notificación.
- Score oficial de Infocorp o SBS.
- IA de abandono o predicción con datos demo.
- Gamificación basada en reclutamiento, presión o exposición de deudas.
- Inventario multimarca avanzado antes de estabilizar pedidos y pagos.
- Datos demo mezclados con cuentas reales.
- Comisiones o estados definidos únicamente en Kotlin.
- Acceso a contactos o ubicación permanente sin consentimiento.

## Criterios comunes de aceptación

Una funcionalidad se considera lista solo si:

1. Tiene un caso de uso claro y permisos definidos.
2. Funciona con datos reales y no con valores inventados.
3. Tiene estados de carga, vacío, error y reintento.
4. Explica qué ocurre sin conexión o tiene una alternativa local.
5. Tiene pruebas si afecta dinero, permisos, stock o estados.
6. No duplica operaciones al reintentar.
7. No depende de WebView para el flujo principal.
8. Respeta privacidad, consentimiento y límites de las integraciones externas.
9. Se verifica con `test`, `lint` y `assembleDebug` cuando corresponda.
10. Se actualiza este documento y no se crea otro roadmap paralelo.

## Próxima implementación recomendada

La siguiente tarea de código debe ser la **Fase 1: checkout nativo de pedidos**. Es el punto con mayor impacto porque actualmente el modal es largo, limita clientes y productos a cuatro elementos y no integra correctamente los pagos parciales durante la creación.

El orden sugerido es:

1. Extraer el formulario a una pantalla o modal de pantalla completa.
2. Crear el estado de borrador del pedido.
3. Implementar el paso Cliente con búsqueda.
4. Implementar el paso Productos con carrito.
5. Implementar Pago y confirmación.
6. Conectar validación, carga, error e idempotencia.
7. Ejecutar pruebas y generar APK.

## Registro de cambios

| Fecha | Cambio | Estado |
|---|---|---|
| 2026-09-18 | Se consolidó el estado del producto, el roadmap UX, los límites y la limpieza en este documento maestro. | Hecho |
| 2026-09-18 | Se dejó documentado el libro de pagos y el flujo de transiciones de pedido existentes en la rama `main`. | Hecho |
| 2026-09-18 | Se identificó como siguiente implementación el checkout nativo de tres pasos. | Hecho |
| 2026-09-18 | Se activó el checkout nativo con búsqueda, carrito, stock visible, pagos parciales y validaciones. | Parcial: falta backend, borrador persistente e idempotencia |
