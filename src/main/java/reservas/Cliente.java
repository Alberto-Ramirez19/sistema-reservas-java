package reservas;

public record Cliente(int id, String nombre, String telefono, String correo) {
    @Override public String toString() { return nombre + " · " + telefono; }
}
