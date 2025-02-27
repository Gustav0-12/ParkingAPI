package api.parking.dto;

public record UserParkingRequest(
        String marca,
        String cor,
        String modelo,
        String placa,
        String userCPF

){
}
