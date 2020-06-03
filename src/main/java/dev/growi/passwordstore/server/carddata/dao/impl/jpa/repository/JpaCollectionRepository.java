package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaCardCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCollectionRepository extends JpaRepository<JpaCardCollection, Long> {
}
