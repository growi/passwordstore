package dev.growi.passwordstore.server.userdata.jpa.repository;

import dev.growi.passwordstore.server.userdata.jpa.dao.AccessGroupDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessGroupDAORepository extends JpaRepository<AccessGroupDAO, Long> {

    Optional<AccessGroupDAO> findByGroupName(String groupName);
}
