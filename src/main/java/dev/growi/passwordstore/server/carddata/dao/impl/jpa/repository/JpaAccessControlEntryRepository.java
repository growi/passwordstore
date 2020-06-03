package dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaAccessControlEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAccessControlEntryRepository extends JpaRepository<JpaAccessControlEntry, Long> {
}
