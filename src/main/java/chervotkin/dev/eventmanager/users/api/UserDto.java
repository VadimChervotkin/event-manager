package chervotkin.dev.eventmanager.users.api;

import chervotkin.dev.eventmanager.users.domain.UserRole;

public record UserDto(
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}
