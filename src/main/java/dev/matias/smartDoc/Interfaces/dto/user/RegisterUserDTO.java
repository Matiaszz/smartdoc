package dev.matias.smartDoc.Interfaces.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
        @NotNull
        @Size(min = 3, max = 20)
        String username,

        @NotNull
        @Size(min = 3, max = 50)
        String name,

        @NotNull
        @Email
        String email,

        @NotNull
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password
) {
}
