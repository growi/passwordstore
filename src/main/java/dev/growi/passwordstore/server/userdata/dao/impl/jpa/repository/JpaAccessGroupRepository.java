package dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAccessGroupRepository extends JpaRepository<JpaAccessGroup, Long> {

    Optional<JpaAccessGroup> findByGroupName(String groupName);
}
