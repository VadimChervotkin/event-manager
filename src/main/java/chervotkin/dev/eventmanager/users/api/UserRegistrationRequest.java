package chervotkin.dev.eventmanager.users.api;

public record UserRegistrationRequest(
        String login,
        String password
) {

}
