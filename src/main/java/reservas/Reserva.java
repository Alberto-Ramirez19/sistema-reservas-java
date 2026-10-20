package reservas;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Reserva(int id, int clienteId, int servicioId, LocalDateTime inicio,
                      int duracionMinutos, BigDecimal precio, Estado estado) {
    public enum Estado { CONFIRMADA, CANCELADA }
    public LocalDateTime fin() { return inicio.plusMinutes(duracionMinutos); }
    public Reserva cancelar() {
        return new Reserva(id, clienteId, servicioId, inicio, duracionMinutos, precio, Estado.CANCELADA);
    }
}
