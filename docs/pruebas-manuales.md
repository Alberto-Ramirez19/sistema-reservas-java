# Guion de aceptación para la sustentación

Usar solo datos ficticios. Registrar quién ejecutó cada paso y el resultado observado.

| Paso | Acción | Resultado esperado |
|---|---|---|
| 1 | Registrar a Ana Pérez, 999111222, ana@example.com | Aparece en Clientes |
| 2 | Cambiar teléfono a 999222333 | Se actualiza el mismo ID |
| 3 | Buscar por Pérez | La lista muestra coincidencias |
| 4 | Registrar Asesoría, S/ 50.00, 30 minutos | Aparece en Servicios |
| 5 | Intentar precio negativo o duración cero | Se muestra validación y no se guarda |
| 6 | Reservar mañana a las 09:00 | Reserva CONFIRMADA y fin a las 09:30 |
| 7 | Reservar el mismo día a las 09:15 | Se rechaza superposición |
| 8 | Reservar a las 09:30 | Se permite un horario consecutivo |
| 9 | Cancelar la de las 09:00 y confirmar | Queda CANCELADA y no se elimina |
| 10 | Reservar nuevamente a las 09:00 | El horario está libre |
| 11 | Cerrar y abrir desde la misma carpeta | Se mantienen datos y cancelaciones |
| 12 | Abrir otra instancia sobre los mismos datos | Se impide la escritura simultánea |

Estas acciones constituyen un guion pendiente de ejecución humana. Las pruebas automáticas registradas en CI se documentan por separado.
