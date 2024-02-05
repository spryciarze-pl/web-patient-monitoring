package se.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public CustomUserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(CustomUserDetails.class).get();
    }

    public void logout() {
        authenticationContext.logout();
    }
}