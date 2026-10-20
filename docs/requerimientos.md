# Requerimientos y alcance

## Criterio de selección

La primera versión cubre el flujo operativo completo: identificar al cliente, definir el servicio, reservar un horario, consultar y cancelar. Se priorizó el valor mínimo para sustituir los registros dispersos; la automatización y analítica requieren esa base y permanecen pendientes.

| Código | Requerimiento | Prioridad | Versión | Issue |
|---|---|---|---|---|
| RF-01 | Registrar clientes | Alta | 1.0.0 | #2 |
| RF-02 | Modificar información de clientes | Alta | 1.0.0 | #3 |
| RF-03 | Consultar clientes | Alta | 1.0.0 | #4 |
| RF-04 | Registrar y consultar servicios, precio y duración | Alta | 1.0.0 | #5 |
| RF-05 | Registrar y consultar reservas con cliente, servicio, fecha y hora | Alta | 1.0.0 | #6 |
| RF-06 | Cancelar reservas | Alta | 1.0.0 | #7 |
| RF-07 | Recordatorios automáticos | Media | Futura | #13 |
| RF-08 | Reportes | Media | Futura | #14 |
| RF-09 | Dashboard de indicadores | Baja | Futura | #15 |
| RF-10 | Pagos en línea | Baja | Futura | #16 |
| RF-11 | Historial de clientes | Media | Futura | #17 |

## Condiciones de aceptación generales

No se permiten nombres vacíos, referencias a clientes o servicios inexistentes, precios no positivos, más de dos decimales, duraciones fuera de 5 a 480 minutos, reservas pasadas ni superpuestas. Los registros se conservan al reiniciar. La consulta de reservas no equivale al futuro historial de atenciones del cliente.

## Requisitos de proceso

Repositorio compartido, tablero con cinco estados, ramas por cambio, commits identificables, revisión cruzada, conflicto entre ramas documentado, etiqueta y release, matriz de trazabilidad. La asistencia de IA no sustituye la participación verificable de los dos integrantes.
