package api.parking.dto;



import api.parking.entities.UserParkingSpot;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserParkingResponse (
        String marca,
        String cor,
        String modelo,
        String placa,
        String userCpf,
        String recibo,
        BigDecimal valor,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime dataEntrada,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime dataSaida,
        String vagaCodigo
){
    public UserParkingResponse(UserParkingSpot userParkingSpot) {
        this(userParkingSpot.getMarca(), userParkingSpot.getCor(), userParkingSpot.getModelo(), userParkingSpot.getPlaca(), userParkingSpot.getUser().getCpf(), userParkingSpot.getRecibo(), userParkingSpot.getValor(), userParkingSpot.getDataEntrada(), userParkingSpot.getDataSaida(), userParkingSpot.getParkingSpot().getCode());
    }
}
