# Matriz de trazabilidad

Secuencia de evidencia: requerimiento -> issue -> responsable -> rama/commit -> revisión -> integración -> versión.

Repositorio: https://github.com/Alberto-Ramirez19/sistema-reservas-java

| Código / issue | Actividad | Responsable | Desarrollo y revisión | Integración / destino |
|---|---|---|---|---|
| G-01 / #1 | Repositorio y acuerdo | Alberto | `5e576f3`, CONTRIBUTING.md | Configuración inicial |
| RF-01 / #2 | Registrar clientes | Alberto | `10543ce`, `8a4983a`, PR #18; revisión U23208480 | `main`, v1.0.0 |
| RF-02 / #3 | Modificar clientes | Alberto | PR #18; persistencia ampliada en PR #19 | `main`, v1.0.0 |
| RF-03 / #4 | Consultar y buscar clientes | Alberto | ReservasPanel.java, PR #18; revisión U23208480 | `main`, v1.0.0 |
| RF-04 / #5 | Servicios, precio y duración | Alberto | GestorReservas.java y ReservasPanel.java, PR #18 | `main`, v1.0.0 |
| RF-05 / #6 | Registrar y consultar reservas | Alberto | PR #18; contención y superposición en PR #19 | `main`, v1.0.0 |
| RF-06 / #7 | Cancelar reservas | Alberto | PR #18; conservación y horario liberado | `main`, v1.0.0 |
| QA-01 / #8 | Ampliar pruebas y revisar | Juan | `93e57a8`, rama de pruebas, PR #19; aprobación Alberto | Merge `0f591a0`, incluido en PR #18 |
| G-02 / #9 | Tablero de cinco estados | Alberto | GitHub Projects 2, issues y responsables | Tablero público |
| G-03 / #10 | Conflicto planificado | Juan y Alberto | `d2d2a54`, `b1a41d0`, resolución `933a808`; PR #20 y #21 | Incluido en PR #18 |
| G-04 / #11 | Integrar y liberar | Juan | Aprobación U23208480 en PR #18, check `pruebas` | Etiqueta y release v1.0.0 |
| DOC-01 / #12 | Informe y evidencias | Alberto | Documentación, matriz, PDF y capturas | PDF adjunto a la release |
| RF-07 / #13 | Recordatorios | Juan | Sin desarrollo para esta versión | Backlog futuro |
| RF-08 / #14 | Reportes | Alberto | Sin desarrollo para esta versión | Backlog futuro |
| RF-09 / #15 | Dashboard | Juan | Sin desarrollo para esta versión | Backlog futuro |
| RF-10 / #16 | Pagos en línea | Alberto | Sin desarrollo para esta versión | Backlog futuro |
| RF-11 / #17 | Historial de clientes | Juan | Sin desarrollo para esta versión | Backlog futuro |

## Enlaces de comprobación

- [PR principal #18](https://github.com/Alberto-Ramirez19/sistema-reservas-java/pull/18).
- [Pruebas de Juan #19](https://github.com/Alberto-Ramirez19/sistema-reservas-java/pull/19).
- [Protección de datos #20](https://github.com/Alberto-Ramirez19/sistema-reservas-java/pull/20).
- [Resolución del conflicto #21](https://github.com/Alberto-Ramirez19/sistema-reservas-java/pull/21).
- [Actions](https://github.com/Alberto-Ramirez19/sistema-reservas-java/actions).
- [Tablero](https://github.com/users/Alberto-Ramirez19/projects/2).
- [Release v1.0.0](https://github.com/Alberto-Ramirez19/sistema-reservas-java/releases/tag/v1.0.0).
