package dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroup;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroupMember;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUserMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface JpaAccessGroupMemberRepository //extends JpaRepository<JpaAccessGroupMember, JpaAccessGroupMember.AccessGroupMemberPK> {
{
    Set<JpaAccessGroupMember> findAllByGroup(JpaAccessGroup group);

}
