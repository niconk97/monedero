package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cuenta {

  private Double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();


  public Cuenta() {
    saldo = 0.0;
  }

  public Cuenta(Double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(Double cuanto) {
    validacionMontoPositivo(cuanto);
    validacionDepositosMaximos();
    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }

  public void sacar(Double cuanto) {
    validacionMontoPositivo(cuanto);
    validacionSaldoMenor(cuanto);
    validacionExtraccionesMaximos(cuanto);
    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public Double getMontoExtraidoA(LocalDate fecha) {
    List<Movimiento> extraccionesDeLaFecha = extraccionesSegunFecha(fecha);
    return extraccionesDeLaFecha.stream().mapToDouble(Movimiento::getMonto).sum();
  }

  public List<Movimiento> extraccionesSegunFecha(LocalDate fecha){
    return getMovimientos()
        .stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .collect(Collectors.toList());
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  public void validacionMontoPositivo(Double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validacionDepositosMaximos(){
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void validacionSaldoMenor(Double monto){
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void validacionExtraccionesMaximos(Double monto){
    Double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    Double limite = 1000 - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

}
