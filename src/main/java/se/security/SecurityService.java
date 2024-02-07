package se.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public se.secuirty.CustomUserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(se.secuirty.CustomUserDetails.class).get();
    }

    public void logout() {
        authenticationContext.logout();
    }
}