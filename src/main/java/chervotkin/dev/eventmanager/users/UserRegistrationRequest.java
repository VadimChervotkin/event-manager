package chervotkin.dev.eventmanager.users;

public record UserRegistrationRequest(
        String login,
        String password
) {

}
