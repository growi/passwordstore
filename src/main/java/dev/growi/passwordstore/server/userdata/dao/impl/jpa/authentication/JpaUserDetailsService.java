package dev.growi.passwordstore.server.userdata.dao.impl.jpa.authentication;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private JpaUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<JpaUser> user = userRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new JpaUserPrincipal(user.get());
    }
}
