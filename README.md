# Sistema de reservas en Java

Proyecto académico de **Alberto Joel Ramirez** y **Juan Cesar Retuerto** para gestionar clientes, servicios y reservas de una empresa de servicios.

## Alcance de la primera versión

- Clientes: registrar, modificar y consultar.
- Servicios: registrar, establecer precio y duración, y consultar.
- Reservas: seleccionar cliente y servicio, registrar fecha y hora, consultar y cancelar.

Recordatorios automáticos, reportes, dashboard, pagos en línea e historial de clientes forman parte del backlog futuro.

## Organización

- Repositorio: https://github.com/Alberto-Ramirez19/sistema-reservas-java
- Actividades: pestaña **Issues**; cada actividad registra requerimiento, prioridad, responsable y criterios de aceptación.
- Flujo: Pendientes → Seleccionadas → En ejecución → En revisión → Terminadas.
- `main`: versión integrada. Cada cambio posterior a la configuración inicial entra mediante una pull request revisada por el otro integrante.
- Ramas: `feature/<numero>-<tema>`, `test/<numero>-<tema>` y `docs/<numero>-<tema>`.

## Integrantes y responsabilidades

| Integrante | Cuenta | Responsabilidad inicial |
|---|---|---|
| Alberto Joel Ramirez | Alberto-Ramirez19 | Configuración, implementación del MVP, propuesta de cambios y correcciones |
| Juan Cesar Retuerto | U23208480 | Revisión del MVP, ampliación de pruebas, validación e integración |

La atribución de trabajo se respalda con cuentas autenticadas, commits, pull requests y revisiones registradas. Cada integrante debe poder explicar las decisiones y los aportes asociados a su cuenta.

## Ejecutar

Requiere **JDK 17 o superior**. No necesita Maven, base de datos externa ni librerías de terceros.

```powershell
# Windows PowerShell, dentro del repositorio:
.\build.ps1 -Run
```

```bash
# Linux o macOS:
bash build.sh
java -jar build/sistema-reservas.jar
```

La compilación ejecuta 22 pruebas antes de generar `build/sistema-reservas.jar`. También puede abrirse un JAR descargado con `java -jar sistema-reservas.jar`.

Los datos se guardan en `data/reservas.properties`, relativo al directorio desde el que se ejecuta Java. Para elegir una ubicación fija:

```powershell
java '-Dreservas.data=C:\ReservasSimple\datos' -jar build/sistema-reservas.jar
```

## Uso

1. En **Clientes**, registrar nombre, teléfono y correo opcional. Seleccionar una fila y pulsar **Modificar cliente seleccionado** para editar.
2. En **Servicios**, registrar nombre, precio en soles y duración de 5 a 480 minutos.
3. En **Reservas**, elegir cliente y servicio e indicar fecha `AAAA-MM-DD` y hora `HH:mm` de Lima.
4. Consultar y filtrar las listas. Para cancelar, seleccionar la reserva y confirmar la acción.

## Decisiones y límites del MVP

- Aplicación de escritorio para un operador. Agenda única: una atención a la vez, sin horarios superpuestos. No existe aún gestión de empleados o recursos.
- Precio positivo con hasta dos decimales; se conserva el precio y duración de la reserva.
- Una cancelación cambia el estado y libera el horario; conserva el registro.
- Persistencia local mediante archivo de propiedades. La aplicación bloquea una segunda instancia sobre la misma carpeta de datos. No es un servidor multiusuario.
- Para respaldo, cerrar la aplicación y copiar `data/reservas.properties` a una ubicación segura. No subir datos reales al repositorio.

## Estado y enlaces

- [Tablero de trabajo](https://github.com/users/Alberto-Ramirez19/projects/2).
- [Actividades](https://github.com/Alberto-Ramirez19/sistema-reservas-java/issues).
- [Pruebas automáticas](https://github.com/Alberto-Ramirez19/sistema-reservas-java/actions).
- [Matriz de trazabilidad](docs/trazabilidad.md).
- [Guía para continuar con Juan](docs/continuacion-juan.md).

La [versión estable v1.0.0](https://github.com/Alberto-Ramirez19/sistema-reservas-java/releases/tag/v1.0.0) identifica el código integrado y verificado. Se conservan las ramas de desarrollo y las PR #18 a #21 para comprobar las contribuciones.

Consulta la [guía del proyecto y la sustentación](docs/guia-proyecto.md), los [resultados de validación](docs/validacion.md) y la [resolución del conflicto](docs/conflicto.md).

Consulta [CONTRIBUTING.md](CONTRIBUTING.md) para el procedimiento del equipo.
