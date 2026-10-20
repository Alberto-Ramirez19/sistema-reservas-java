package reservas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZoneId;
import javax.swing.*;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Almacen almacen = null;
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Path directorio = Path.of(System.getProperty("reservas.data", "data"));
                almacen = new Almacen(directorio);
                Clock reloj = Clock.system(ZoneId.of("America/Lima"));
                GestorReservas gestor = new GestorReservas(almacen, reloj);
                JFrame ventana = new JFrame("ReservaSimple | Sistema de reservas");
                ventana.setContentPane(new ReservasPanel(gestor, reloj));
                ventana.setSize(1100, 740); ventana.setMinimumSize(new java.awt.Dimension(900, 640));
                ventana.setLocationRelativeTo(null); ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Almacen abierto = almacen;
                ventana.addWindowListener(new WindowAdapter() {
                    @Override public void windowClosed(WindowEvent e) {
                        try { abierto.close(); } catch (java.io.IOException ex) { System.err.println(ex.getMessage()); }
                    }
                });
                ventana.setVisible(true);
            } catch (Exception ex) {
                if (almacen != null) try { almacen.close(); } catch (java.io.IOException ignored) { }
                JOptionPane.showMessageDialog(null, "No se pudo iniciar: " + ex.getMessage(), "ReservaSimple", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
