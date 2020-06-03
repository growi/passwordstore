package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCardRepository extends JpaRepository<JpaCard, Long> {
}
