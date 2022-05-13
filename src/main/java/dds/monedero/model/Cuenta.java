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
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();

  public Cuenta() {
    saldo = 0.0;
  }

  public Cuenta(Double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(Double cuanto) {
    validacionMontoPositivo(cuanto);
    validacionDepositosMaximos();
    Deposito depositoNuevo = new Deposito(LocalDate.now(), cuanto);
    agregarDeposito(depositoNuevo);
    this.setSaldo(depositoNuevo.calcularValor(this));
  }

  public void sacar(Double cuanto) {
    validacionMontoPositivo(cuanto);
    validacionSaldoMenor(cuanto);
    validacionExtraccionesMaximos(cuanto);
    Extraccion extraccionNueva = new Extraccion(LocalDate.now(), cuanto);
    agregarExtraccion(extraccionNueva);
    this.setSaldo(extraccionNueva.calcularValor(this));
  }

  public void agregarDeposito(Deposito deposito) {
    depositos.add(deposito);
  }

  public void agregarExtraccion(Extraccion extraccion){
    extracciones.add(extraccion);
  }

  public Double getMontoExtraidoA(LocalDate fecha) {
    List<Extraccion> extraccionesDeLaFecha = extraccionesSegunFecha(fecha);
    return extraccionesDeLaFecha.stream().mapToDouble(Extraccion::getMonto).sum();
  }

  public List<Extraccion> extraccionesSegunFecha(LocalDate fecha){
    return getExtracciones()
        .stream()
        .filter(extraccion -> extraccion.fueRealizadaEn(fecha))
        .collect(Collectors.toList());
  }

  public List<Extraccion> getExtracciones() {
    return extracciones;
  }

   public List<Deposito> getDepositos() {
    return depositos;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  private void validacionMontoPositivo(Double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validacionDepositosMaximos(){
    if (getDepositos().stream().count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private void validacionSaldoMenor(Double monto){
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  private void validacionExtraccionesMaximos(Double monto){
    Double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    Double limite = 1000 - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

}
