# Roadmap futuro, límites y prácticas correctas

## Propósito

Este documento separa las ideas válidas para el futuro de las funciones que requieren servicios externos, autorización legal, una política de privacidad específica o que no deben implementarse de forma insegura.

## No se implementa todavía

### IA y predicción de abandono

No se entrenará ni publicará un modelo de churn con datos demo. Primero se necesitan varias campañas reales, consentimiento, métricas consistentes y una explicación de por qué un usuario aparece en riesgo.

La primera versión será un sistema de reglas transparentes:

- no tiene pedido en la campaña;
- el pedido promedio bajó durante varios ciclos;
- tiene saldo vencido;
- no ha completado una tarea importante;
- no ha sido contactado recientemente.

### Automatización masiva de WhatsApp

Por ahora solo se permitirá compartir un mensaje preparado mediante la aplicación oficial de WhatsApp.

Quedan para después:

- bandeja multiagente;
- campañas automáticas;
- plantillas aprobadas;
- respuestas automáticas;
- chatbot;
- agente con IA;
- transferencia automática a un humano.

Estas funciones requieren una integración oficial, consentimiento, control de plantillas, límites de envío y un backend. No se hará scraping ni automatización de la aplicación personal de WhatsApp.

### Lectura automática de Yape o Plin

No se marcará un pago como verificado solo por leer una captura o una notificación. Android permite escuchar notificaciones mediante un servicio especial, pero ese acceso puede exponer información sensible y depende de que el usuario lo active explícitamente. [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService)

Si se estudia en el futuro, el flujo correcto será:

```text
evento detectado → coincidencia sugerida → revisión del usuario → pago confirmado
```

No se afirmará que una notificación u OCR prueba criptográficamente una transferencia. La función necesitará revisión de privacidad, permisos, política de Play y pruebas con las aplicaciones bancarias.

### Scoring de Infocorp o SBS

No se consultarán centrales de riesgo desde el APK ni se mostrará una clasificación oficial sin una base legal, autorización y proveedor habilitado. La aplicación solo manejará un riesgo interno de cobranza.

Los rangos regulatorios de atraso dependen del tipo de crédito; no deben copiarse automáticamente a una deuda informal entre vendedor y cliente. [Referencia SBS](https://www.sbs.gob.pe/Portals/0/INFORME%20CONJUNTO%20N%2000062-2024-SBS.pdf)

### Academia y gamificación avanzada

La academia puede agregarse después de estabilizar las ventas y pagos. No se implementarán por ahora:

- recompensas basadas únicamente en reclutar personas;
- promesas de ingresos;
- presión por comprar inventario;
- rankings que expongan públicamente deudas o bajo rendimiento;
- mensajes automáticos que exploten culpa o urgencia falsa.

La capacitación futura debe premiar aprendizaje, servicio, entregas, pagos ordenados y retención saludable.

### Inventario avanzado

El inventario multimarca completo requiere movimientos de stock, lotes, vencimientos, costos, devoluciones y reservas. No debe simularse con un simple campo `available`.

Se implementará después de estabilizar pedidos y pagos, comenzando por inventario propio del vendedor y no por integraciones simultáneas con todas las marcas.

## Funciones que no deben implementarse de forma incorrecta

- Contraseñas, tokens privados o claves administrativas dentro del APK.
- Verificación de pagos basada únicamente en capturas de pantalla.
- Lectura de notificaciones sin consentimiento claro.
- Acceso permanente a contactos cuando el selector de contactos sea suficiente.
- Scraping continuo de sitios de terceros sin revisar sus condiciones.
- Envío automático de mensajes sin consentimiento u opción de dejar de recibirlos.
- Cálculo o publicación de un supuesto score oficial de crédito.
- Comisiones fijas escritas en Kotlin.
- Pedidos que puedan duplicarse al reintentar la red.
- Estados de pedido cambiados solo desde la interfaz.
- Datos demo mezclados con datos reales.
- Árboles de red que expongan información a miembros sin autorización.
- Recomendaciones médicas o promesas terapéuticas sobre productos cosméticos.

## Limpieza necesaria del proyecto

### Código

- Eliminar datos demo de los repositorios cuando se conecten a Supabase.
- Corregir textos con mojibake como `MarÃ­a` y `comisiÃ³n`.
- Centralizar colores, tipografía, espaciado y formas en el sistema de diseño.
- Separar `domain`, `data`, `network`, `database` y `ui`.
- Evitar que las pantallas calculen comisiones o reglas financieras.
- Sustituir fechas fijas por `Instant` o timestamps reales.
- Unificar `OrderStatus` entre Kotlin, SQL y web.
- Agregar estados de carga, vacío, error y reintento a cada pantalla.
- Cubrir repositorios y transiciones de pedido con pruebas.

### Base de datos

- Aplicar las migraciones en un entorno de prueba antes de producción.
- Crear migraciones nuevas; no editar migraciones ya aplicadas.
- Añadir tabla de pagos y auditoría.
- Añadir reglas configurables de campaña y comisión.
- Añadir relación de patrocinador solo si el modelo de negocio la necesita.
- Crear índices para las consultas de campaña, cliente y pedido.
- Revisar RLS con usuarios de prueba de distintos equipos.
- Registrar errores de sincronización sin guardar secretos.

### Documentación

La documentación debe distinguir siempre:

- hecho comprobado;
- hipótesis de negocio;
- decisión del producto;
- requisito técnico;
- dependencia externa;
- riesgo legal o de privacidad;
- función futura.

Las estadísticas de mercado del informe original no deben convertirse en constantes de la aplicación. Deben conservar su fuente, fecha y nivel de confianza. Los testimonios de foros sirven para descubrir problemas y lenguaje del usuario, pero no prueban por sí solos una estadística ni una obligación legal.

## Criterios de aceptación para futuras funciones

Una función nueva solo pasará a producción si:

1. Tiene un caso de uso concreto.
2. Tiene propietario de datos y permisos definidos.
3. Funciona sin datos falsos.
4. Tiene manejo offline o explica por qué necesita conexión.
5. Tiene auditoría si afecta dinero, permisos o estados.
6. Tiene consentimiento si usa datos personales.
7. Tiene alternativa manual si falla una integración externa.
8. Tiene pruebas y documentación.
9. No depende de una pantalla web dentro del APK.
10. Puede desactivarse sin romper pedidos, clientes o pagos.

