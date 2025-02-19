package api.parking.dto;

import api.parking.entities.UserRole;

public record RegisterDTO(String name, String email, String password, String cpf, UserRole role) {
}

