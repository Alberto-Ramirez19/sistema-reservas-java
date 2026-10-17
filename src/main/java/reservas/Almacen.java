package reservas;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.*;
import java.util.Properties;

/** Un archivo de propiedades con sustitución atómica y una sola instancia escritora. */
public final class Almacen implements AutoCloseable {
    private final Path archivo;
    private final FileChannel canal;
    private final FileLock bloqueo;

    public Almacen(Path directorio) throws IOException {
        Files.createDirectories(directorio);
        archivo = directorio.resolve("reservas.properties");
        canal = FileChannel.open(directorio.resolve("app.lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        try {
            bloqueo = canal.tryLock();
            if (bloqueo == null) throw new IOException("Los datos ya están abiertos en otra instancia.");
        } catch (IOException | OverlappingFileLockException ex) {
            canal.close();
            throw new IOException("Los datos ya están abiertos en otra instancia.", ex);
        }
    }

    public Properties cargar() throws IOException {
        Properties datos = new Properties();
        if (Files.exists(archivo)) {
            try (InputStream entrada = Files.newInputStream(archivo)) { datos.load(entrada); }
            if (!"1".equals(datos.getProperty("formato"))) throw new IOException("Formato de datos desconocido. No se modificó el archivo.");
        }
        return datos;
    }

    public void guardar(Properties datos) throws IOException {
        Path temporal = Files.createTempFile(archivo.getParent(), "reservas-", ".tmp");
        try {
            try (OutputStream salida = Files.newOutputStream(temporal)) { datos.store(salida, "Sistema de reservas - formato 1"); }
            try { Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING); }
        } finally { Files.deleteIfExists(temporal); }
    }

    @Override public void close() throws IOException {
        try { bloqueo.release(); } finally { canal.close(); }
    }
}
