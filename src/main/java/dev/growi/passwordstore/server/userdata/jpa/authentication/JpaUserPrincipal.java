package dev.growi.passwordstore.server.userdata.jpa.authentication;

import dev.growi.passwordstore.server.userdata.jpa.dao.UserDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JpaUserPrincipal implements UserDetails {
    private UserDAO user;

    public JpaUserPrincipal(UserDAO user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.user.isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.user.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }
}