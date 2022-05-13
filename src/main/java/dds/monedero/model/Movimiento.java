package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
  private LocalDate fecha;
  // Nota: En ningún lenguaje de programación usen jamás doubles (es decir, números con punto flotante) para modelar dinero en el mundo real.
  // En su lugar siempre usen numeros de precision arbitraria o punto fijo, como BigDecimal en Java y similares
  // De todas formas, NO es necesario modificar ésto como parte de este ejercicio. 
  private Double monto;

  public Movimiento(LocalDate fecha, Double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public Double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public Boolean fueRealizadaEn(LocalDate fecha) {
    return this.fecha.isEqual(fecha);
  }

  public abstract Double calcularValor(Cuenta cuenta);
}
