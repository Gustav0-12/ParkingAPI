package api.parking.dto;

import api.parking.entities.UserRole;

public record UserResponseDTO(Long id, String name, String email, String cpf, UserRole role) {
}
