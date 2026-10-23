# Guía del proyecto

## Qué resuelve

ReservaSimple reúne clientes, servicios y reservas en Java Swing. Sustituye el registro disperso por un flujo local: registrar cliente, definir servicio con precio y duración, elegir fecha y hora, consultar la agenda y cancelar.

La versión 1.0.0 trabaja con un operador y una agenda única. Conserva datos entre sesiones en un archivo de propiedades y no exige una base de datos externa. Cada reserva guarda su precio y duración. Una cancelación conserva el registro y libera el horario.

## Organización del código

- `Main`: ventana, hora de Lima y cierre del almacén.
- `ReservasPanel`: formularios, tablas, búsqueda y confirmación de cancelación.
- `GestorReservas`: reglas de negocio y validaciones.
- `Cliente`, `Servicio`, `Reserva`: datos del dominio.
- `Almacen`: carga, guardado mediante sustitución de archivo y bloqueo de escritura simultánea.
- `Pruebas`: 22 escenarios de aceptación y recorrido de componentes Swing.

## Ejecución

1. Instalar JDK 17 o superior. Comprobar `java -version` y `javac -version`.
2. Clonar el repositorio y abrir una terminal en su carpeta.
3. Windows: `./build.ps1 -Run`. Linux/macOS: `bash build.sh` y `java -jar build/sistema-reservas.jar`.
4. También puede descargarse el JAR de la release y ejecutarse con `java -jar sistema-reservas.jar`.
5. Cerrar antes de copiar `data/reservas.properties` para respaldo. Usar la misma carpeta de trabajo o fijarla mediante `-Dreservas.data`.

## Distribución del trabajo

Alberto configuró el repositorio, las issues y el tablero, implementó el MVP y preparó la documentación. U23208480 aportó pruebas en la PR #19, revisó las propuestas de Alberto y creó la rama y el commit de resolución de la PR #21. Las revisiones de los aportes de Juan se registraron desde Alberto-Ramirez19.

## Control de cambios

El tablero usa Pendientes, Seleccionadas, En ejecución, En revisión y Terminadas. Las issues #1 a #12 corresponden a la primera versión; las #13 a #17 conservan mejoras futuras. Las ramas permiten preparar cambios sin incorporarlos directamente a `main`. La revisión requiere observaciones concretas y aprobación del otro integrante. `main` exige el check `pruebas`, una aprobación y conversaciones resueltas. Los merges conservan los commits y las ramas de evidencia.

La PR #18 recibió una solicitud de cambios por documentación incompleta. Se añadió la matriz de trazabilidad, se actualizó el total de pruebas y se confirmó la cuenta de Juan antes de la aprobación final.

## Explicación del conflicto

Ambos cambios partieron de `5e576f3` y sustituyeron el mismo encabezado en CONTRIBUTING.md. Git no pudo decidir entre protección y respaldo. Se guardó `CONFLICT`, se conservaron las ramas y se eligió un título que integra ambos propósitos. El merge `933a808` registra la resolución. Fue un ejercicio planificado sobre un conflicto real.

## Selección de la primera versión

Se priorizó el flujo operativo completo. Recordatorios, reportes, dashboard, pagos e historial de clientes añaden automatización y análisis sobre esa base y quedan en el backlog. Consultar reservas canceladas no equivale al futuro historial de atenciones realizadas.

## Demostración sugerida

Registrar un cliente ficticio, editarlo y buscarlo. Crear un servicio de 30 minutos a S/ 50.00. Reservar un día futuro a las 09:00 y comprobar fin a las 09:30. Intentar otra a las 09:15 para mostrar el rechazo y otra a las 09:30 para mostrar continuidad válida. Cancelar la primera, comprobar el estado CANCELADA y volver a usar su horario. Reiniciar para mostrar persistencia. Seguir [pruebas-manuales.md](pruebas-manuales.md).

## Límites y siguientes versiones

No hay inicio de sesión de operadores, servidor multiusuario, empleados ni agenda por recurso. Se debe respaldar el archivo local. Las mejoras futuras se administran como issues separadas para conservar el alcance de la primera versión.
