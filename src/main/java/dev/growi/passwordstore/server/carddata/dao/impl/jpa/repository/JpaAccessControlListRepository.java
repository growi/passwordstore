package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaAccessControlList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAccessControlListRepository extends JpaRepository<JpaAccessControlList, Long> {
}
