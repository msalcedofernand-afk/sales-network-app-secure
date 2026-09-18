# Reglas del proyecto — Sales Network Secure

## Versiones

- Cada build incrementa `versionCode` automáticamente mediante `version.properties` y `build.gradle.kts`.
- No editar `version.properties` manualmente; el build lo gestiona.
- `versionName` se actualiza manualmente solo en cambios mayores.

## Instalación

- Usar `adb install -r` para actualizar sin desinstalar.
- No desinstalar la app durante las pruebas salvo que exista una razón explícita.

## Cambios

- Registrar cambios relevantes en `docs/DOCUMENTACION.md`.
- Incluir qué cambió, por qué, archivos modificados y resultado de las pruebas.
- Mantener la documentación del producto en ese único archivo.
