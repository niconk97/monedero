package dds.monedero.model;

import java.time.LocalDate;

public class Deposito extends Movimiento{

  public Deposito(LocalDate fecha, Double monto) {
    super(fecha, monto);
  }

  public Double calcularValor(Cuenta cuenta) {
    return cuenta.getSaldo() + getMonto();
  }

}
