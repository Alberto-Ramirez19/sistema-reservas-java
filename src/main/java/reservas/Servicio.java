package reservas;

import java.math.BigDecimal;

public record Servicio(int id, String nombre, BigDecimal precio, int duracionMinutos) {
    @Override public String toString() { return nombre + " · S/ " + precio + " · " + duracionMinutos + " min"; }
}
