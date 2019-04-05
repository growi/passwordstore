package dev.growi.passwordstore.server.userdata.jpa.repository;

import dev.growi.passwordstore.server.userdata.jpa.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAORepository extends JpaRepository<UserDAO, Long> {

    Optional<UserDAO> findByUserName(String userName);
}
