package dev.growi.passwordstore.server.userdata.dao.provider;

import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupMemberNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PrivateKey;
import java.util.List;
import java.util.Set;

public interface GroupDataProvider {

    List<GroupDAO> findAll();

    GroupDAO findByGroupId(Long id) throws GroupNotFoundException;

    GroupDAO findByGroupName(String groupName) throws GroupNotFoundException;

    GroupDAO createGroup(String groupName) throws CryptographyException, UserNotFoundException;

    GroupMemberDAO createGroupMember(GroupDAO group, PrivateKey groupKey, PrincipalDAO member, int permissions) throws UserNotFoundException, CryptographyException;

    GroupMemberDAO findGroupMemberById(PrincipalDAO principal, GroupDAO group) throws GroupMemberNotFoundException;

    Set<GroupMemberDAO> findGroupMembers(GroupDAO group);

    Set<GroupMemberDAO> findGroupMembersByGroupId(Long id) throws GroupNotFoundException;

    void removeUserMember(GroupMemberDAO member);

    void removeGroupMember(GroupMemberDAO member);

    GroupDAO save(GroupDAO group) throws UserNotFoundException;

    void deleteById(Long groupId);
}
