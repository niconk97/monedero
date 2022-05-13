package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500.0));
  }

  @Test
  void TresDepositosYUnaExtraccion() {
    cuenta.poner(1500.0);
    cuenta.poner(456.0);
    cuenta.poner(1900.0);
    cuenta.sacar(125.6);
    assertEquals(3730.4,cuenta.getSaldo());
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500.0);
          cuenta.poner(456.0);
          cuenta.poner(1900.0);
          cuenta.poner(245.0);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.poner(90.0);
          cuenta.sacar(1001.0);
    });
  }

  @Test
  void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.poner(5000.0);
      cuenta.sacar(1001.0);
    });
  }

  @Test
  void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500.0));
  }

  @Test
  void ExisteUnaExtraccionQueSeRealizoHoy(){
    cuenta.poner(10000.0);
    cuenta.sacar(300.0);
    cuenta.sacar(200.0);
    assertTrue(cuenta.extraccionesSegunFecha(LocalDate.now()).size() > 0);
  }

  @Test
  void MontoExtraidoDeHoy(){
    cuenta.poner(10000.0);
    cuenta.sacar(300.0);
    cuenta.sacar(200.0);
    cuenta.sacar(250.0);
    assertEquals(750.0, cuenta.getMontoExtraidoA(LocalDate.now()));
  }

}