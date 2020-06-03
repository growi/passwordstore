package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaTextContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTextContentRepository extends JpaRepository<JpaTextContent, Long> {
}
