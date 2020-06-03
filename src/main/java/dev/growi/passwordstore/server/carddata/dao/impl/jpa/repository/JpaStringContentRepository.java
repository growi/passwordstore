package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaStringContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStringContentRepository extends JpaRepository<JpaStringContent, Long> {
}
