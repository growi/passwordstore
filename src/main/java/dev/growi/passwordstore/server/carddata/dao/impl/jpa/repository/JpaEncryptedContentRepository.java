package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEncryptedContent extends JpaRepository<JpaEncryptedContent, Long> {
}
