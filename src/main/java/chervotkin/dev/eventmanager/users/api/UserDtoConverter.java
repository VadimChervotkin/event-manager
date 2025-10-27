package chervotkin.dev.eventmanager.users.api;

import chervotkin.dev.eventmanager.users.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convertDomainUser(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }
}
