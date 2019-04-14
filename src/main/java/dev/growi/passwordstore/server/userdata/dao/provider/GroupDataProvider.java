package dev.growi.passwordstore.server.userdata.dao.provider;

import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupMemberNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PrivateKey;
import java.util.List;
import java.util.Set;

public interface GroupDataProvider {

    List<GroupDAO> findAll();

    GroupDAO findByGroupId(IdWrapper<?> groupId) throws GroupNotFoundException;

    GroupDAO findByGroupName(String groupName) throws GroupNotFoundException;

    GroupDAO createGroup(String groupName, UserDetails activeUser) throws UserNotFoundException, CryptographyException;

    GroupMemberDAO createUserMember(GroupDAO group, PrivateKey groupKey, UserDAO member, int permissions,  UserDetails activeUser) throws UserNotFoundException, CryptographyException;

    GroupMemberDAO createGroupMember(GroupDAO group, PrivateKey groupKey, GroupDAO member, int permissions,  UserDetails activeUser) throws UserNotFoundException, CryptographyException;

    GroupMemberDAO findGroupMemberById(PrincipalDAO principal, GroupDAO group) throws GroupMemberNotFoundException;

    Set<GroupMemberDAO> findGroupMembers(GroupDAO group);

    Set<GroupMemberDAO> findGroupMembersByGroupId(IdWrapper<?> group) throws GroupNotFoundException;

    void removeUserMember(GroupMemberDAO member);

    void removeGroupMember(GroupMemberDAO member);
}
