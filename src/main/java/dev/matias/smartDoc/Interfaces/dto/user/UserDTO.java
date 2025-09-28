package dev.matias.smartDoc.Interfaces.dto.user;

import dev.matias.smartDoc.Domain.User.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String name,
        String email,
        Boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public UserDTO(User user){
        this(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
