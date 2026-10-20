# Aportes y revisión de Juan Cesar Retuerto

Cuenta autenticada: `U23208480`. La invitación al repositorio fue aceptada el 20 de octubre de 2026.

## Pruebas de aceptación - issue #8

La rama `test/8-validacion-juan` aporta el commit `93e57a8` y la [PR #19](https://github.com/Alberto-Ramirez19/sistema-reservas-java/pull/19). Se añadieron tres escenarios y se amplió la comprobación de persistencia. Alberto registró la aprobación y la integración en `feature/mvp-reservas` mediante `0f591a0`.

## Revisión del MVP - PR #18

La revisión señaló el archivo de trazabilidad ausente, el conteo anterior de pruebas y la cuenta aún pendiente en README. Los cambios de documentación corrigen esas observaciones antes de la integración final.

## Conflicto planificado - issue #10

El commit `b1a41d0` cambia el encabezado de respaldo desde el mismo commit base usado por Alberto. Git detectó un conflicto real en CONTRIBUTING.md. La resolución se registró desde U23208480 en el commit de merge `933a808`, conservando ambas propuestas. Véanse la [PR #21](https://github.com/Alberto-Ramirez19/sistema-reservas-java/pull/21) y [conflicto.md](conflicto.md).

## Integración y versión - issue #11

La PR #18 es el punto de revisión del MVP antes de `main`. La etiqueta anotada `v1.0.0` y la release identifican el código verificado, el JAR y el alcance funcional. Los resultados se comprueban en Actions y en el historial de las pull requests.
