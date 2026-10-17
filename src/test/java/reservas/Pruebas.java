package reservas;

import java.awt.Component;
import java.awt.Container;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.*;
import javax.swing.*;

/** Pruebas sin dependencias externas; cada caso usa un directorio temporal aislado. */
public final class Pruebas {
    private static int correctas;
    private static final Clock RELOJ = Clock.fixed(Instant.parse("2026-10-16T15:00:00Z"), ZoneId.of("America/Lima"));
    private static final LocalDateTime INICIO = LocalDateTime.of(2026, 10, 17, 9, 0);
    private Pruebas() { }
    public static void main(String[] args) throws Exception {
        caso("RF-01 registrar y consultar cliente", g -> {
            Cliente c = g.registrarCliente("  Ana Pérez  ", "999111222", "ana@example.com");
            igual("Ana Pérez", c.nombre()); igual(1, g.clientes().size());
        });
        caso("RF-01 validar nombre, teléfono y correo", g -> {
            rechaza(() -> g.registrarCliente(" ", "999111222", ""));
            rechaza(() -> g.registrarCliente("Ana", "abc", ""));
            rechaza(() -> g.registrarCliente("Ana", "999111222", "sin-arroba")); igual(0, g.clientes().size());
        });
        caso("RF-02 modificar conservando ID", g -> {
            Cliente c = g.registrarCliente("Ana", "999111222", "");
            g.modificarCliente(c.id(), "Ana Pérez", "999222333", "ana@example.com");
            igual("Ana Pérez", g.cliente(c.id()).nombre()); igual(1, g.clientes().size());
        });
        caso("RF-02 rechazar cliente inexistente", g -> rechaza(() -> g.modificarCliente(999, "Ana", "999111222", "")));
        caso("RF-04 precio y duración", g -> {
            Servicio s = g.registrarServicio("Corte", new BigDecimal("35.50"), 30);
            igual(new BigDecimal("35.50"), s.precio()); igual(30, s.duracionMinutos());
        });
        caso("RF-04 rechazar precio cero negativo o fracción de centavo", g -> {
            for (String p : new String[]{"0", "-1", "15.001"}) rechaza(() -> g.registrarServicio("Corte", new BigDecimal(p), 30));
            igual(0, g.servicios().size());
        });
        caso("RF-04 validar duración y nombre duplicado", g -> {
            rechaza(() -> g.registrarServicio("Corte", BigDecimal.TEN, 0));
            rechaza(() -> g.registrarServicio("Corte", BigDecimal.TEN, 481));
            g.registrarServicio("Corte", BigDecimal.TEN, 30);
            rechaza(() -> g.registrarServicio(" corte ", BigDecimal.TEN, 30));
        });
        caso("RF-05 reservar cliente y servicio", g -> {
            preparar(g); Reserva r = g.registrarReserva(1, 1, INICIO);
            igual(Reserva.Estado.CONFIRMADA, r.estado()); igual(INICIO.plusMinutes(30), r.fin());
        });
        caso("RF-05 impedir referencias inexistentes", g -> {
            preparar(g); rechaza(() -> g.registrarReserva(99, 1, INICIO)); rechaza(() -> g.registrarReserva(1, 99, INICIO));
        });
        caso("RF-05 rechazar pasado y presente", g -> {
            preparar(g); rechaza(() -> g.registrarReserva(1, 1, LocalDateTime.now(RELOJ)));
            rechaza(() -> g.registrarReserva(1, 1, LocalDateTime.now(RELOJ).minusDays(1)));
        });
        caso("RF-05 impedir superposición parcial y contenida", g -> {
            preparar(g); g.registrarReserva(1, 1, INICIO);
            rechaza(() -> g.registrarReserva(1, 1, INICIO.plusMinutes(15)));
            rechaza(() -> g.registrarReserva(1, 1, INICIO.minusMinutes(15)));
            rechaza(() -> g.registrarReserva(1, 1, INICIO)); igual(1, g.reservas().size());
        });
        caso("RF-05 permitir reservas consecutivas", g -> {
            preparar(g); g.registrarReserva(1, 1, INICIO); g.registrarReserva(1, 1, INICIO.plusMinutes(30)); igual(2, g.reservas().size());
        });
        caso("RF-06 cancelar libera el horario sin borrar", g -> {
            preparar(g); Reserva r = g.registrarReserva(1, 1, INICIO); g.cancelarReserva(r.id());
            g.registrarReserva(1, 1, INICIO); igual(2, g.reservas().size()); igual(Reserva.Estado.CANCELADA, g.reservas().get(0).estado());
        });
        caso("RF-06 rechazar doble cancelación e ID inexistente", g -> {
            preparar(g); Reserva r = g.registrarReserva(1, 1, INICIO); g.cancelarReserva(r.id());
            rechaza(() -> g.cancelarReserva(r.id())); rechaza(() -> g.cancelarReserva(99));
        });
        caso("Consultas inmutables", g -> {
            preparar(g);
            try { g.clientes().clear(); throw new AssertionError("La consulta permite alterar el estado"); }
            catch (UnsupportedOperationException expected) { }
        });
        persistencia(); bloqueo(); datosInvalidos(); interfaz();
        System.out.println("RESULTADO: " + correctas + " pruebas aprobadas; 0 fallidas.");
    }
    private static void preparar(GestorReservas g) throws Exception {
        g.registrarCliente("Ana Pérez", "999111222", "ana@example.com");
        g.registrarServicio("Asesoría", new BigDecimal("50.00"), 30);
    }
    private static void persistencia() throws Exception {
        Path p = Files.createTempDirectory("reservas-persistencia-");
        try {
            try (Almacen a = new Almacen(p)) {
                GestorReservas g = new GestorReservas(a, RELOJ); preparar(g); g.registrarReserva(1, 1, INICIO); g.cancelarReserva(1);
            }
            try (Almacen a = new Almacen(p)) {
                GestorReservas g = new GestorReservas(a, RELOJ);
                igual("Ana Pérez", g.cliente(1).nombre()); igual(new BigDecimal("50.00"), g.servicio(1).precio());
                igual(Reserva.Estado.CANCELADA, g.reservas().get(0).estado());
                igual(2, g.registrarCliente("Luis", "999888777", "").id());
            }
            aprobado("Persistencia de clientes, servicios, cancelaciones e IDs al reiniciar");
        } finally { limpiar(p); }
    }
    private static void bloqueo() throws Exception {
        Path p = Files.createTempDirectory("reservas-bloqueo-");
        try (Almacen a = new Almacen(p)) {
            try (Almacen duplicado = new Almacen(p)) { throw new AssertionError("Se permitió escritura simultánea"); }
            catch (java.io.IOException expected) { aprobado("Bloqueo de una segunda instancia"); }
        } finally { limpiar(p); }
    }
    private static void datosInvalidos() throws Exception {
        Path p = Files.createTempDirectory("reservas-formato-");
        try {
            Files.writeString(p.resolve("reservas.properties"), "formato=99\n");
            try (Almacen a = new Almacen(p)) {
                try { new GestorReservas(a, RELOJ); throw new AssertionError("Se aceptó formato desconocido"); }
                catch (java.io.IOException expected) { igual("formato=99\n", Files.readString(p.resolve("reservas.properties"))); }
            }
            aprobado("Formato desconocido conserva el archivo original");
        } finally { limpiar(p); }
    }
    private static void interfaz() throws Exception {
        caso("Interfaz Swing: registrar cliente, servicio y reserva", g -> SwingUtilities.invokeAndWait(() -> {
            ReservasPanel panel = new ReservasPanel(g, RELOJ);
            ((JTextField) buscar(panel, "cliente.nombre")).setText("Cliente de prueba");
            ((JTextField) buscar(panel, "cliente.telefono")).setText("999111222");
            ((JButton) buscar(panel, "cliente.guardar")).doClick();
            igual(1, g.clientes().size());
            ((JTextField) buscar(panel, "servicio.nombre")).setText("Servicio de prueba");
            ((JTextField) buscar(panel, "servicio.precio")).setText("25,50");
            ((JButton) buscar(panel, "servicio.guardar")).doClick();
            igual(1, g.servicios().size());
            ((JButton) buscar(panel, "reserva.guardar")).doClick();
            igual(1, g.reservas().size());
            igual(1, ((JTable) buscar(panel, "reservas.tabla")).getRowCount());
        }));
    }
    private static Component buscar(Component componente, String nombre) {
        if (nombre.equals(componente.getName())) return componente;
        if (componente instanceof Container c) for (Component hijo : c.getComponents()) {
            Component encontrado = buscar(hijo, nombre); if (encontrado != null) return encontrado;
        }
        return null;
    }
    private interface Caso { void run(GestorReservas g) throws Exception; }
    private interface Accion { void run() throws Exception; }
    private static void caso(String nombre, Caso prueba) throws Exception {
        Path p = Files.createTempDirectory("reservas-test-");
        try (Almacen a = new Almacen(p)) { prueba.run(new GestorReservas(a, RELOJ)); aprobado(nombre); }
        finally { limpiar(p); }
    }
    private static void aprobado(String nombre) { correctas++; System.out.println("OK " + correctas + " - " + nombre); }
    private static void igual(Object esperado, Object real) {
        if (!java.util.Objects.equals(esperado, real)) throw new AssertionError("Esperado " + esperado + "; obtenido " + real);
    }
    private static void rechaza(Accion accion) throws Exception {
        try { accion.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("La operación inválida fue aceptada");
    }
    private static void limpiar(Path p) throws Exception {
        try (var files = Files.walk(p)) {
            for (Path file : files.sorted(java.util.Comparator.reverseOrder()).toList()) Files.delete(file);
        }
    }
}
