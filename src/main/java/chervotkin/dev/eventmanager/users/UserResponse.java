package chervotkin.dev.eventmanager.users;

public record UserResponse(
        Long id,
        String login,
        String role
) {
}
