package api.parking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ParkingFeeCalculator {

    public BigDecimal calculateTotalValue(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        long minutes = dataEntrada.until(dataSaida, ChronoUnit.MINUTES);
        BigDecimal valorTotal;

        if (minutes < 15) {
            valorTotal = BigDecimal.valueOf(5.00);
        } else if (minutes < 60) {
            valorTotal = BigDecimal.valueOf(9.25);
        } else {
            double additionalHours = Math.ceil((minutes - 60 ) / 60);
            double additionalFee = 2.00;
            valorTotal = BigDecimal.valueOf(9.25).add(new BigDecimal(additionalFee * additionalHours));
        }
        return valorTotal;
    }
}
