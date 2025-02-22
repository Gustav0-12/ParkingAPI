package api.parking.dto;


import api.parking.entities.Status;


public record ParkingSpotDTO(Long id , String code, Status status) {
}
