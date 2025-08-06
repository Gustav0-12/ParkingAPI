package api.parking.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ParkingFeeCalculatorTest {

    ParkingFeeCalculator calculator = new ParkingFeeCalculator();

    @Test
    @DisplayName("o Cálculo deve retornar 5.0 se o tempo for em até 15 minutos")
    void calculateTotalValueCase1() {
        LocalDateTime dataEntrada = LocalDateTime.now();
        LocalDateTime dataSaida = dataEntrada.plusMinutes(10);

        BigDecimal valor = calculator.calculateTotalValue(dataEntrada, dataSaida);

        assertEquals(new BigDecimal("5.0"), valor);
    }

    @Test
    @DisplayName("o Cálculo deve retornar 9.25 se o tempo for entre 15 e 60 minutos")
    void calculateTotalValueCase2() {
        LocalDateTime dataEntrada = LocalDateTime.now();
        LocalDateTime dataSaida = dataEntrada.plusMinutes(45);

        BigDecimal valor = calculator.calculateTotalValue(dataEntrada, dataSaida);

        assertEquals(new BigDecimal("9.25"), valor);
    }

    @Test
    @DisplayName("O cálculo deve adicionar 2.00 por hora adicional após 60 minutos")
    void calculateTotalValueCase3() {
        LocalDateTime dataEntrada = LocalDateTime.now();
        LocalDateTime dataSaida = dataEntrada.plusHours(2);

        BigDecimal valor = calculator.calculateTotalValue(dataEntrada, dataSaida);

        assertEquals(new BigDecimal("11.25"), valor);
    }
}