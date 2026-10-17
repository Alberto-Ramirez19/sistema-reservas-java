# Continuación con Juan Cesar Retuerto

## Estado de la colaboración

La primera etapa se realizó con la cuenta autenticada `Alberto-Ramirez19`, con asistencia de IA. Juan tiene previstas las issues #8, #10 y #11. Su participación solo podrá registrarse cuando acepte la invitación y trabaje desde su propia cuenta.

## 1. Validar el sistema y aportar pruebas (#8)

1. Aceptar la invitación del repositorio desde GitHub.
2. Clonar el repositorio y ejecutar `git switch feature/mvp-reservas`.
3. Ejecutar `./build.ps1 -Run` (Windows) o `bash build.sh` (Linux/macOS).
4. Probar altas, modificación de clientes, consultas y cancelación. Anotar resultados reales.
5. Crear `test/8-validacion-juan` desde `feature/mvp-reservas`.
6. Añadir una prueba útil, por ejemplo un servicio largo que englobe totalmente otra reserva, o persistencia de la modificación del cliente tras reiniciar.
7. Hacer un commit con su identidad real y `Refs #8`. Abrir PR hacia `feature/mvp-reservas` para que Alberto revise ese aporte.
8. Revisar la PR principal de Alberto: dejar una observación concreta, solicitar cambios si corresponde y aprobar solo tras verificarla. GitHub no permite al autor aprobar su propia PR.

## 2. Ejecutar el conflicto planificado (#10)

Se preparará la rama `docs/10-agenda-alberto` desde el `main` inicial. En ella Alberto modifica el título `## Datos` de CONTRIBUTING.md.

Juan debe crear su rama desde el mismo commit base, antes de integrar la de Alberto:

```bash
git fetch origin
git switch -c docs/10-agenda-juan 5e576f3
```

Cambiar `## Datos` por `## Respaldo y recuperación de datos`, guardar y hacer commit propio. Después integrar la rama de Alberto:

```bash
git merge origin/docs/10-agenda-alberto
```

Git deberá detectar el cambio incompatible sobre la misma línea. Capturar `git status`, los marcadores y los dos commits. Resolver con un título acordado, por ejemplo `## Protección, respaldo y recuperación de datos`, y ejecutar `git add CONTRIBUTING.md` y `git commit`. Documentar lo sucedido en `docs/conflicto.md`.

Este es un ejercicio planificado de conflicto real entre ramas; no debe presentarse como un incidente espontáneo. No cambiar autor o correo para aparentar contribuciones de otra persona.

## 3. Integrar y liberar (#11)

1. Completar la revisión cruzada, resolver conversaciones y comprobar Java CI.
2. Integrar mediante merge commit. Actualizar el tablero al estado real.
3. Ejecutar las pruebas sobre `main` integrado.
4. Crear etiqueta anotada `v1.0.0` sobre ese commit y publicarla.
5. Crear la release estable con el JAR, funcionalidades, limitaciones y pendientes. La candidata `v1.0.0-rc.1`, si existe, no reemplaza esta release.
6. Actualizar el PDF con capturas de los aportes de Juan, aprobación, merge, conflicto y release estable.

## Datos del informe por completar

Institución, curso, docente y fecha de entrega no fueron confirmados. Conservar los campos pendientes hasta contar con esos datos.
