package chervotkin.dev.eventmanager.users.domain;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers() {
        createUserIfNotExists("admin", "admin", UserRole.ADMIN);
        createUserIfNotExists("user", "user", UserRole.USER);

    }

    private void createUserIfNotExists(
            String login,
            String password,
            UserRole userRole
    ) {
        if (userService.isUserExistsByLogin(login)) {
            return;
        }
        var hashedPass = passwordEncoder.encode(password);
        var user = new User(
                null,
                login,
                21,
                userRole,
                hashedPass
        );
        userService.saveUser(user);
    }
}
