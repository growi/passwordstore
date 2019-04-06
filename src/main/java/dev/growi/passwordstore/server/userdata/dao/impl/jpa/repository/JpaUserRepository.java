package dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<JpaUser, Long> {

    Optional<JpaUser> findByUserName(String userName);
}
