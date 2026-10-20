package reservas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/** Interfaz de escritorio. Los formularios delegan las reglas al gestor. */
public final class ReservasPanel extends JPanel {
    private final GestorReservas gestor;
    private final Clock reloj;
    private final JTabbedPane pestanas = new JTabbedPane();
    private final JLabel mensaje = new JLabel("Listo. Registre clientes y servicios antes de reservar.");
    private final DefaultTableModel clientes = modelo("ID", "Nombre", "Teléfono", "Correo");
    private final DefaultTableModel servicios = modelo("ID", "Servicio", "Precio (S/)", "Duración (min)");
    private final DefaultTableModel reservas = modelo("ID", "Cliente", "Servicio", "Fecha y hora", "Fin", "Precio (S/)", "Estado");
    private final JComboBox<Cliente> selectorCliente = new JComboBox<>();
    private final JComboBox<Servicio> selectorServicio = new JComboBox<>();
    private final JTextField nombre = campo("cliente.nombre"), telefono = campo("cliente.telefono"), correo = campo("cliente.correo");
    private int clienteEditado = 0;
    private final JButton guardarCliente = new JButton("Registrar cliente");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public ReservasPanel(GestorReservas gestor, Clock reloj) {
        super(new BorderLayout(16, 16));
        this.gestor = gestor;
        this.reloj = reloj;
        setBorder(new EmptyBorder(22, 24, 16, 24));
        setBackground(new Color(245, 247, 251));
        JPanel cabecera = new JPanel(new BorderLayout()); cabecera.setOpaque(false);
        JLabel titulo = new JLabel("ReservaSimple"); titulo.setFont(new Font("SansSerif", Font.BOLD, 28)); titulo.setForeground(new Color(22, 44, 73));
        JLabel subtitulo = new JLabel("Clientes, servicios y reservas  |  Agenda única  |  Hora de Lima"); subtitulo.setForeground(new Color(76, 92, 110));
        cabecera.add(titulo, BorderLayout.NORTH); cabecera.add(subtitulo, BorderLayout.SOUTH);
        add(cabecera, BorderLayout.NORTH);
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 14));
        pestanas.addTab("Clientes", panelClientes());
        pestanas.addTab("Servicios", panelServicios());
        pestanas.addTab("Reservas", panelReservas());
        add(pestanas, BorderLayout.CENTER);
        mensaje.setForeground(new Color(26, 92, 65)); add(mensaje, BorderLayout.SOUTH);
        actualizar();
    }

    private JPanel panelClientes() {
        JPanel panel = panel();
        JPanel formulario = formulario();
        fila(formulario, "Nombre *", nombre); fila(formulario, "Teléfono *", telefono); fila(formulario, "Correo (opcional)", correo);
        guardarCliente.setName("cliente.guardar");
        guardarCliente.addActionListener(e -> ejecutar(() -> {
            if (clienteEditado == 0) gestor.registrarCliente(nombre.getText(), telefono.getText(), correo.getText());
            else gestor.modificarCliente(clienteEditado, nombre.getText(), telefono.getText(), correo.getText());
            limpiarCliente(); mensaje.setText("Cliente guardado correctamente.");
        }));
        JButton nuevo = new JButton("Limpiar formulario"); nuevo.addActionListener(e -> limpiarCliente());
        formulario.add(guardarCliente); formulario.add(nuevo);
        panel.add(formulario, BorderLayout.NORTH);
        JTable tabla = tabla(clientes); tabla.setName("clientes.tabla");
        panel.add(consulta(tabla, "Buscar cliente"), BorderLayout.CENTER);
        JButton editar = new JButton("Modificar cliente seleccionado"); editar.setName("cliente.editar");
        editar.addActionListener(e -> ejecutar(() -> {
            Cliente c = gestor.cliente(idSeleccionado(tabla)); clienteEditado = c.id();
            nombre.setText(c.nombre()); telefono.setText(c.telefono()); correo.setText(c.correo());
            guardarCliente.setText("Guardar cambios"); mensaje.setText("Editando cliente #" + c.id());
        }));
        panel.add(editar, BorderLayout.SOUTH); return panel;
    }
    private void limpiarCliente() {
        clienteEditado = 0; nombre.setText(""); telefono.setText(""); correo.setText(""); guardarCliente.setText("Registrar cliente");
    }
    private JPanel panelServicios() {
        JPanel panel = panel(); JPanel formulario = formulario();
        JTextField nombreServicio = campo("servicio.nombre"), precio = campo("servicio.precio");
        JSpinner duracion = new JSpinner(new SpinnerNumberModel(30, 5, 480, 5)); duracion.setName("servicio.duracion");
        fila(formulario, "Servicio *", nombreServicio); fila(formulario, "Precio en soles *", precio); fila(formulario, "Duración en minutos *", duracion);
        JButton guardar = new JButton("Registrar servicio"); guardar.setName("servicio.guardar");
        guardar.addActionListener(e -> ejecutar(() -> {
            duracion.commitEdit();
            gestor.registrarServicio(nombreServicio.getText(), new BigDecimal(precio.getText().trim().replace(',', '.')), (Integer) duracion.getValue());
            nombreServicio.setText(""); precio.setText(""); mensaje.setText("Servicio registrado correctamente.");
        }));
        formulario.add(guardar); formulario.add(new JLabel("Precio positivo; hasta dos decimales."));
        panel.add(formulario, BorderLayout.NORTH); panel.add(consulta(tabla(servicios), "Buscar servicio"), BorderLayout.CENTER); return panel;
    }
    private JPanel panelReservas() {
        JPanel panel = panel(); JPanel formulario = formulario();
        selectorCliente.setName("reserva.cliente"); selectorServicio.setName("reserva.servicio");
        JTextField fecha = campo("reserva.fecha"), hora = campo("reserva.hora");
        fecha.setText(LocalDate.now(reloj).plusDays(1).toString()); hora.setText("09:00");
        fila(formulario, "Cliente *", selectorCliente); fila(formulario, "Servicio *", selectorServicio);
        fila(formulario, "Fecha (AAAA-MM-DD) *", fecha); fila(formulario, "Hora (HH:mm, Lima) *", hora);
        JButton guardar = new JButton("Registrar reserva"); guardar.setName("reserva.guardar");
        guardar.addActionListener(e -> ejecutar(() -> {
            Cliente c = (Cliente) selectorCliente.getSelectedItem(); Servicio s = (Servicio) selectorServicio.getSelectedItem();
            if (c == null || s == null) throw new IllegalArgumentException("Registre y seleccione un cliente y un servicio.");
            LocalDateTime inicio = LocalDateTime.parse(fecha.getText().trim()+" "+hora.getText().trim(), FECHA);
            gestor.registrarReserva(c.id(), s.id(), inicio); mensaje.setText("Reserva confirmada correctamente.");
        }));
        formulario.add(guardar); formulario.add(new JLabel("No se permiten horarios superpuestos."));
        panel.add(formulario, BorderLayout.NORTH);
        JTable tabla = tabla(reservas); tabla.setName("reservas.tabla"); panel.add(consulta(tabla, "Buscar reserva o estado"), BorderLayout.CENTER);
        JButton cancelar = new JButton("Cancelar reserva seleccionada"); cancelar.setName("reserva.cancelar");
        cancelar.addActionListener(e -> ejecutar(() -> {
            int id = idSeleccionado(tabla);
            if (JOptionPane.showConfirmDialog(this, "¿Cancelar la reserva #"+id+"? Se conservará en la lista.", "Confirmar cancelación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                gestor.cancelarReserva(id); mensaje.setText("Reserva #"+id+" cancelada. Su horario queda disponible.");
            }
        }));
        panel.add(cancelar, BorderLayout.SOUTH); return panel;
    }
    private void actualizar() {
        Cliente seleccionado = (Cliente) selectorCliente.getSelectedItem(); Servicio servicioSeleccionado = (Servicio) selectorServicio.getSelectedItem();
        clientes.setRowCount(0); selectorCliente.removeAllItems();
        for (Cliente c : gestor.clientes()) {
            clientes.addRow(new Object[]{c.id(), c.nombre(), c.telefono(), c.correo()}); selectorCliente.addItem(c);
            if (seleccionado != null && seleccionado.id() == c.id()) selectorCliente.setSelectedItem(c);
        }
        servicios.setRowCount(0); selectorServicio.removeAllItems();
        for (Servicio s : gestor.servicios()) {
            servicios.addRow(new Object[]{s.id(), s.nombre(), s.precio(), s.duracionMinutos()}); selectorServicio.addItem(s);
            if (servicioSeleccionado != null && servicioSeleccionado.id() == s.id()) selectorServicio.setSelectedItem(s);
        }
        reservas.setRowCount(0);
        for (Reserva r : gestor.reservas()) reservas.addRow(new Object[]{r.id(), gestor.cliente(r.clienteId()).nombre(), gestor.servicio(r.servicioId()).nombre(),
                r.inicio().format(FECHA), r.fin().format(FECHA), r.precio(), r.estado()});
    }
    private void ejecutar(Accion accion) {
        try { accion.run(); actualizar(); }
        catch (java.time.format.DateTimeParseException ex) { error("Ingrese una fecha válida (AAAA-MM-DD) y hora (HH:mm). Ejemplo: 2027-01-20 09:30."); }
        catch (NumberFormatException | java.text.ParseException ex) { error("Revise el precio y la duración: deben ser números válidos."); }
        catch (IllegalArgumentException ex) { error(ex.getMessage()); }
        catch (java.io.IOException ex) { error("No se pudieron guardar los datos. " + ex.getMessage()); }
    }
    private void error(String texto) { JOptionPane.showMessageDialog(this, texto, "Revise los datos", JOptionPane.WARNING_MESSAGE); }
    private interface Accion { void run() throws java.io.IOException, java.text.ParseException; }
    private static int idSeleccionado(JTable tabla) {
        int fila = tabla.getSelectedRow(); if (fila < 0) throw new IllegalArgumentException("Seleccione una fila de la lista.");
        return (Integer) tabla.getModel().getValueAt(tabla.convertRowIndexToModel(fila), 0);
    }
    private static JPanel panel() { JPanel p = new JPanel(new BorderLayout(12, 16)); p.setBorder(new EmptyBorder(20, 18, 18, 18)); return p; }
    private static JPanel formulario() { return new JPanel(new GridLayout(0, 2, 14, 9)); }
    private static JTextField campo(String nombre) { JTextField c = new JTextField(); c.setName(nombre); return c; }
    private static void fila(JPanel p, String texto, JComponent campo) { JLabel label = new JLabel(texto); label.setLabelFor(campo); p.add(label); p.add(campo); }
    private static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } };
    }
    private static JTable tabla(DefaultTableModel modelo) {
        JTable t = new JTable(modelo); t.setRowHeight(29); t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); t.setAutoCreateRowSorter(true);
        t.setFillsViewportHeight(true); t.getTableHeader().setReorderingAllowed(false); return t;
    }
    private static JPanel consulta(JTable tabla, String etiqueta) {
        JPanel p = new JPanel(new BorderLayout(8, 8)); JTextField filtro = new JTextField();
        JLabel label = new JLabel(etiqueta); label.setLabelFor(filtro); JPanel barra = new JPanel(new BorderLayout(12, 0)); barra.add(label, BorderLayout.WEST); barra.add(filtro, BorderLayout.CENTER);
        filtro.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                @SuppressWarnings("unchecked") TableRowSorter<DefaultTableModel> orden = (TableRowSorter<DefaultTableModel>) tabla.getRowSorter();
                orden.setRowFilter(RowFilter.regexFilter("(?iu)" + Pattern.quote(filtro.getText().trim())));
            }
            public void insertUpdate(DocumentEvent e) { filtrar(); } public void removeUpdate(DocumentEvent e) { filtrar(); } public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
        p.add(barra, BorderLayout.NORTH); p.add(new JScrollPane(tabla), BorderLayout.CENTER); return p;
    }
}
