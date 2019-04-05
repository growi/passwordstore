package dev.growi.passwordstore.server.userdata.jpa.authentication;

import dev.growi.passwordstore.server.userdata.jpa.dao.UserDAO;
import dev.growi.passwordstore.server.userdata.jpa.repository.UserDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAORepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserDAO> user = userRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new JpaUserPrincipal(user.get());
    }
}
