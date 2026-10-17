package reservas;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;

/** Reglas de negocio. En el MVP la empresa atiende una reserva a la vez. */
public final class GestorReservas {
    private final Almacen almacen;
    private final Clock reloj;
    private final Map<Integer, Cliente> clientes = new TreeMap<>();
    private final Map<Integer, Servicio> servicios = new TreeMap<>();
    private final Map<Integer, Reserva> reservas = new TreeMap<>();

    public GestorReservas(Almacen almacen, Clock reloj) throws IOException {
        this.almacen = almacen;
        this.reloj = reloj;
        try { restaurar(almacen.cargar()); }
        catch (RuntimeException ex) { throw new IOException("Los datos no se pueden leer. Conserve el archivo para revisarlo.", ex); }
    }

    public List<Cliente> clientes() { return List.copyOf(clientes.values()); }
    public List<Servicio> servicios() { return List.copyOf(servicios.values()); }
    public List<Reserva> reservas() {
        return reservas.values().stream().sorted(Comparator.comparing(Reserva::inicio).thenComparing(Reserva::id)).toList();
    }
    public Cliente cliente(int id) { return exigir(clientes, id, "cliente"); }
    public Servicio servicio(int id) { return exigir(servicios, id, "servicio"); }

    public Cliente registrarCliente(String nombre, String telefono, String correo) throws IOException {
        Cliente nuevo = validarCliente(siguiente(clientes), nombre, telefono, correo);
        persistir(() -> clientes.put(nuevo.id(), nuevo));
        return nuevo;
    }
    public void modificarCliente(int id, String nombre, String telefono, String correo) throws IOException {
        cliente(id);
        Cliente actualizado = validarCliente(id, nombre, telefono, correo);
        persistir(() -> clientes.put(id, actualizado));
    }
    public Servicio registrarServicio(String nombre, BigDecimal precio, int minutos) throws IOException {
        nombre = texto(nombre, "nombre del servicio", 100);
        if (precio == null || precio.signum() <= 0 || precio.compareTo(new BigDecimal("1000000")) > 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero y no superar S/ 1 000 000.");
        try { precio = precio.setScale(2, RoundingMode.UNNECESSARY); }
        catch (ArithmeticException ex) { throw new IllegalArgumentException("El precio admite como máximo dos decimales."); }
        if (minutos < 5 || minutos > 480) throw new IllegalArgumentException("La duración debe estar entre 5 y 480 minutos.");
        String nombreFinal = nombre;
        if (servicios.values().stream().anyMatch(s -> s.nombre().equalsIgnoreCase(nombreFinal)))
            throw new IllegalArgumentException("Ya existe un servicio con ese nombre.");
        Servicio nuevo = new Servicio(siguiente(servicios), nombre, precio, minutos);
        persistir(() -> servicios.put(nuevo.id(), nuevo));
        return nuevo;
    }
    public Reserva registrarReserva(int clienteId, int servicioId, LocalDateTime inicio) throws IOException {
        cliente(clienteId);
        Servicio servicio = servicio(servicioId);
        if (inicio == null || !inicio.isAfter(LocalDateTime.now(reloj)))
            throw new IllegalArgumentException("La reserva debe tener una fecha y hora futuras (Lima).");
        if (inicio.getSecond() != 0 || inicio.getNano() != 0)
            throw new IllegalArgumentException("Indique la hora con precisión de minutos.");
        LocalDateTime fin = inicio.plusMinutes(servicio.duracionMinutos());
        boolean ocupado = reservas.values().stream().anyMatch(r -> r.estado() == Reserva.Estado.CONFIRMADA
                && inicio.isBefore(r.fin()) && fin.isAfter(r.inicio()));
        if (ocupado) throw new IllegalArgumentException("El horario se superpone con una reserva confirmada.");
        Reserva nueva = new Reserva(siguiente(reservas), clienteId, servicioId, inicio,
                servicio.duracionMinutos(), servicio.precio(), Reserva.Estado.CONFIRMADA);
        persistir(() -> reservas.put(nueva.id(), nueva));
        return nueva;
    }
    public void cancelarReserva(int id) throws IOException {
        Reserva actual = exigir(reservas, id, "reserva");
        if (actual.estado() == Reserva.Estado.CANCELADA) throw new IllegalArgumentException("La reserva ya está cancelada.");
        persistir(() -> reservas.put(id, actual.cancelar()));
    }

    private static Cliente validarCliente(int id, String nombre, String telefono, String correo) {
        nombre = texto(nombre, "nombre del cliente", 100);
        telefono = texto(telefono, "teléfono", 25);
        if (!telefono.matches("\\+?[0-9 ()-]{6,25}") || telefono.replaceAll("\\D", "").length() < 6)
            throw new IllegalArgumentException("Ingrese un teléfono válido con al menos 6 dígitos.");
        correo = correo == null ? "" : correo.trim();
        if (correo.length() > 150 || (!correo.isEmpty() && !correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")))
            throw new IllegalArgumentException("Ingrese un correo válido o deje el campo vacío.");
        return new Cliente(id, nombre, telefono, correo);
    }
    private static String texto(String valor, String campo, int max) {
        if (valor == null || valor.isBlank() || valor.trim().length() > max)
            throw new IllegalArgumentException("Complete " + campo + " (máximo " + max + " caracteres).");
        return valor.trim();
    }
    private static int siguiente(Map<Integer, ?> mapa) { return mapa.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1; }
    private static <T> T exigir(Map<Integer, T> mapa, int id, String tipo) {
        T elemento = mapa.get(id);
        if (elemento == null) throw new IllegalArgumentException("No existe el " + tipo + " seleccionado.");
        return elemento;
    }
    private void persistir(Runnable cambio) throws IOException {
        Properties anterior = serializar();
        cambio.run();
        try { almacen.guardar(serializar()); }
        catch (IOException ex) { restaurar(anterior); throw ex; }
    }
    private Properties serializar() {
        Properties p = new Properties();
        p.setProperty("formato", "1");
        p.setProperty("clientes", ids(clientes)); p.setProperty("servicios", ids(servicios)); p.setProperty("reservas", ids(reservas));
        for (Cliente c : clientes.values()) {
            String k = "cliente." + c.id() + ".";
            p.setProperty(k+"nombre", c.nombre()); p.setProperty(k+"telefono", c.telefono()); p.setProperty(k+"correo", c.correo());
        }
        for (Servicio s : servicios.values()) {
            String k = "servicio." + s.id() + ".";
            p.setProperty(k+"nombre", s.nombre()); p.setProperty(k+"precio", s.precio().toPlainString()); p.setProperty(k+"duracion", ""+s.duracionMinutos());
        }
        for (Reserva r : reservas.values()) {
            String k = "reserva." + r.id() + ".";
            p.setProperty(k+"cliente", ""+r.clienteId()); p.setProperty(k+"servicio", ""+r.servicioId());
            p.setProperty(k+"inicio", r.inicio().toString()); p.setProperty(k+"duracion", ""+r.duracionMinutos());
            p.setProperty(k+"precio", r.precio().toPlainString()); p.setProperty(k+"estado", r.estado().name());
        }
        return p;
    }
    private static String ids(Map<Integer, ?> mapa) { return String.join(",", mapa.keySet().stream().map(String::valueOf).toList()); }
    private static List<Integer> ids(Properties p, String key) {
        String raw = p.getProperty(key, "");
        return raw.isBlank() ? List.of() : Arrays.stream(raw.split(",")).map(Integer::valueOf).toList();
    }
    private void restaurar(Properties p) {
        clientes.clear(); servicios.clear(); reservas.clear();
        for (int id : ids(p, "clientes")) {
            String k = "cliente."+id+".";
            clientes.put(id, validarCliente(id, p.getProperty(k+"nombre"), p.getProperty(k+"telefono"), p.getProperty(k+"correo")));
        }
        for (int id : ids(p, "servicios")) {
            String k = "servicio."+id+".";
            servicios.put(id, new Servicio(id, Objects.requireNonNull(p.getProperty(k+"nombre")), new BigDecimal(p.getProperty(k+"precio")), Integer.parseInt(p.getProperty(k+"duracion"))));
        }
        for (int id : ids(p, "reservas")) {
            String k = "reserva."+id+".";
            Reserva r = new Reserva(id, Integer.parseInt(p.getProperty(k+"cliente")), Integer.parseInt(p.getProperty(k+"servicio")),
                    LocalDateTime.parse(p.getProperty(k+"inicio")), Integer.parseInt(p.getProperty(k+"duracion")),
                    new BigDecimal(p.getProperty(k+"precio")), Reserva.Estado.valueOf(p.getProperty(k+"estado")));
            cliente(r.clienteId()); servicio(r.servicioId()); reservas.put(id, r);
        }
    }
}
