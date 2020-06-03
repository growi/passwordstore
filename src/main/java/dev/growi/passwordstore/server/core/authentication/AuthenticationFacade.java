package dev.growi.passwordstore.server.core.authentication;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();
}
