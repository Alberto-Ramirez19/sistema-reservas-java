# Validación del sistema

Entorno: JDK 17; compilación `--release 17`; Windows PowerShell local y Ubuntu en GitHub Actions. Los casos usan un reloj fijo de Lima y carpetas temporales. No dependen de la fecha actual ni utilizan datos personales reales.

Resultado de `./build.ps1` o `bash build.sh`: **22 pruebas aprobadas; 0 fallidas**, seguido de la generación de `build/sistema-reservas.jar`.

| Grupo | Cobertura ejecutada |
|---|---|
| Clientes | Registro, consulta, normalización, campos inválidos, edición conservando ID, ID inexistente, edición inválida sin perder el registro |
| Servicios | Precio y duración válidos, precio cero/negativo/fracción de centavo, duración inválida, nombre duplicado |
| Reservas | Referencias existentes, rechazo de pasado/presente, superposición parcial, contención en ambos sentidos, horarios consecutivos |
| Cancelación | Conserva el registro, libera el horario, rechaza doble cancelación e ID inexistente |
| Persistencia | Reinicio con nombre, teléfono y correo editados; precio del servicio; cancelación e incremento de IDs |
| Protección | Consultas inmutables, bloqueo de una segunda instancia, formato desconocido conservado |
| Interfaz | Recorrido automatizado de formularios Swing para registrar cliente, servicio y reserva |

La comprobación automática de Swing ejecuta componentes sin una ventana visible. El [guion para la sustentación](pruebas-manuales.md) permite repetir la demostración interactiva. No se presenta dicho guion como una sesión manual ya ejecutada.
