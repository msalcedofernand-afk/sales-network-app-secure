# Alcance de implementación actual

## Propósito

Convertir la aplicación en un CRM nativo para venta directa que funcione con datos reales, conserve información en el dispositivo cuando no haya conexión y sincronice de forma segura con Supabase.

El APK no usará WebView como panel de control. La interfaz será Jetpack Compose y el backend será consumido mediante APIs protegidas.

## Qué se implementa ahora

### 1. Base de datos y sincronización

- Aplicar las migraciones pendientes en el proyecto real de Supabase.
- Mantener Supabase como fuente de verdad para usuarios, equipos, campañas, productos, clientes y pedidos.
- Añadir una cache local con Room para lectura offline.
- Añadir una cola de operaciones pendientes para crear o editar datos sin conexión.
- Resolver conflictos usando `updated_at`, versión de registro y reglas explícitas.
- Mostrar estados claros: sincronizado, pendiente, error y reintentando.

### 2. Campañas

Cada campaña tendrá:

- nombre;
- fecha de inicio;
- fecha de cierre;
- estado: borrador, publicada, cerrada;
- meta de ventas;
- pedido mínimo, si aplica;
- zona horaria;
- días restantes;
- resumen de ventas y deuda del ciclo.

La campaña activa no debe estar escrita como `C-01-2026` dentro del código. Debe venir de la base de datos.

### 3. Catálogo

- Productos reales sincronizados por equipo.
- SKU único por equipo.
- Marca y campaña de origen.
- Precio almacenado en centavos.
- Moneda explícita.
- Imagen, descripción y categoría.
- Disponibilidad visible.
- Búsqueda por nombre y SKU.
- Filtros por categoría y marca.
- Enlace compartible del catálogo.
- Registro de cada sincronización y sus errores.

La importación desde una web externa seguirá ejecutándose en backend y respetará los permisos y condiciones de la fuente. El APK no dependerá de raspar páginas para poder abrir el catálogo.

### 4. Clientes

- Crear, editar, archivar y consultar clientes.
- Teléfono y WhatsApp separados y validados.
- Varias direcciones por cliente.
- Notas y preferencias.
- Historial de pedidos.
- Saldo pendiente.
- Próxima acción de seguimiento.
- Botones para llamar, abrir WhatsApp y abrir una ruta.

### 5. Pedidos

Estados únicos en Android, backend y web:

```text
PENDIENTE → CONFIRMADO → COBRADO → ENTREGADO
                 └──────→ CANCELADO
```

Se implementará:

- creación idempotente;
- detalle de productos y cantidades;
- precio congelado en el momento de la compra;
- cliente asociado;
- campaña asociada;
- estado validado en backend;
- resumen para WhatsApp;
- historial de cambios;
- permisos por propietario, miembro y líder.

### 6. Pagos y cobranza básica

Se añadirá un libro de pagos separado del pedido:

- pago total o parcial;
- método: Yape, Plin, efectivo o transferencia;
- fecha de pago;
- fecha de vencimiento;
- monto recibido;
- saldo restante;
- comprobante opcional;
- usuario que confirmó;
- estado: pendiente, confirmado, rechazado o anulado.

La aplicación calculará el riesgo interno de cobranza mediante días de atraso y saldo pendiente. No mostrará una clasificación oficial de SBS o Infocorp.

### 7. Red de vendedores

- Miembros de un equipo.
- Líder responsable.
- Patrocinador directo, si corresponde.
- Estado en la campaña.
- Ventas del ciclo.
- Pedidos pendientes.
- Saldo vencido.
- Último contacto.
- Lista de atención prioritaria.

Primero se implementará una lista accionable. El árbol visual de genealogía se dejará para una etapa posterior.

### 8. Seguridad y calidad

- RLS aplicado en producción.
- Ninguna `service_role` dentro del APK.
- Operaciones críticas mediante funciones protegidas.
- Validación de permisos en backend y no solamente en la interfaz.
- Dinero en enteros de centavos.
- Fechas en UTC en backend y formato local solamente en la pantalla.
- Idempotencia para pedidos, pagos e importaciones.
- Auditoría de cambios críticos.
- Mensajes de error sin exponer secretos o SQL.
- Textos guardados en UTF-8.

## Orden de trabajo

1. Aplicar y verificar migraciones de Supabase.
2. Sustituir repositorios en memoria por repositorios remotos con cache local.
3. Conectar campañas y catálogo.
4. Conectar clientes y direcciones.
5. Conectar pedidos y transiciones de estado.
6. Añadir pagos, saldos y vencimientos.
7. Añadir panel de campaña y seguimiento de la red.
8. Ejecutar pruebas, lint, build y pruebas de sincronización.

## Definición de terminado

La fase actual estará terminada cuando:

- un usuario pueda iniciar sesión desde un dispositivo nuevo;
- pueda ver los datos reales de su equipo;
- pueda trabajar temporalmente sin conexión;
- pueda crear un pedido y verlo después en otro dispositivo autorizado;
- un pedido no pueda duplicarse por reintentos;
- un pago parcial actualice correctamente el saldo;
- un miembro no pueda leer datos de otro equipo;
- los estados inválidos sean rechazados por el backend;
- no existan datos demo en el flujo real;
- `test`, `lint` y `assembleDebug` pasen correctamente.

