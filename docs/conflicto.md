# Conflicto de integración y resolución

## Situación

Ejercicio planificado de conflicto real entre ramas, ejecutado el 20 de octubre de 2026. No se presenta como un incidente espontáneo. La issue #10 corresponde a esta actividad.

- Archivo: `CONTRIBUTING.md`, encabezado de datos de la línea 23.
- Base compartida: `5e576f30e0b9161e90121daa3734608893c6410e`.
- Alberto: `docs/10-agenda-alberto`, commit `d2d2a54a39b462a112a494e38d341319597a7990`, título `Protección de los datos locales`.
- Juan: `docs/10-agenda-juan`, commit `b1a41d040c4b9d94a7d73be62c313524d0d46093`, título `Respaldo y recuperación de datos`.

Las dos ramas cambiaron la misma línea. La propuesta de Alberto entró al MVP mediante la PR #20, revisada desde U23208480. Al incorporar ese estado a la rama de Juan, Git no pudo elegir automáticamente una de las variantes.

## Evidencia de Git

```text
Auto-merging CONTRIBUTING.md
CONFLICT (content): Merge conflict in CONTRIBUTING.md
Automatic merge failed; fix conflicts and then commit the result.
```

`git status --short` mostró `UU CONTRIBUTING.md`. El archivo contenía:

```text
<<<<<<< HEAD
## Respaldo y recuperación de datos
=======
## Protección de los datos locales
>>>>>>> origin/feature/mvp-reservas
```

Los registros completos se conservan en [evidencias/conflicto-merge.txt](evidencias/conflicto-merge.txt), [evidencias/conflicto-status.txt](evidencias/conflicto-status.txt) y [evidencias/conflicto-marcadores.txt](evidencias/conflicto-marcadores.txt).

## Decisión y verificación

Se usó `Protección, respaldo y recuperación de datos` para conservar el propósito de ambas propuestas. Se mantuvo el acuerdo de usar datos ficticios y excluir `data/` del repositorio. Se retiraron todos los marcadores.

La resolución quedó registrada desde U23208480 en el merge `933a808b8ff673e98a5d731e2a7001896db7de80`, con los padres `b1a41d0` y `ef8d315`. La [PR #21](https://github.com/Alberto-Ramirez19/sistema-reservas-java/pull/21) conserva la propuesta, el commit y la revisión de Alberto antes de integrar. Las 22 pruebas verifican que la aplicación conserva su comportamiento después de resolver el conflicto.
