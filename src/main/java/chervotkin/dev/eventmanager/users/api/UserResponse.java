package chervotkin.dev.eventmanager.users.api;

public record UserResponse(
        Long id,
        String login,
        String role
) {
}
