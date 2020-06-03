package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaFileContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFileContentRepository extends JpaRepository<JpaFileContent, Long> {
}
