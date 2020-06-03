package dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaGroupMember;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface JpaGroupMemberRepository extends JpaRepository<JpaGroupMember, JpaGroupMember.GroupMemberPK> {

    Set<GroupMemberDAO> findAllByGroupId(Long id);
}
