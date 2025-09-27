package dev.matias.smartDoc.Domain.User.ValueObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    public List<String> getPermissions() {
        return switch (this) {
            case ADMIN -> List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");
            case MANAGER -> List.of("ROLE_MANAGER","ROLE_USER");
            case USER -> List.of("ROLE_USER");
        };
    }

}