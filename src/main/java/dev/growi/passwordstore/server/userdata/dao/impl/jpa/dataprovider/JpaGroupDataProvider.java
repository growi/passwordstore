package dev.growi.passwordstore.server.userdata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupMemberNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.configuration.JpaDatasourceCondition;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroupMember;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaGroupMember;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUserMember;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaAccessGroupMemberRepository;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaUserMemberRepository;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaUserRepository;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroup;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaAccessGroupRepository;
import dev.growi.passwordstore.server.userdata.domain.service.CryptographyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Conditional(JpaDatasourceCondition.class)
public class JpaGroupDataProvider implements GroupDataProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CryptographyService cryptographyService;

    @Autowired
    private JpaAccessGroupRepository jpaAccessGroupRepository;

    @Autowired
    private JpaAccessGroupMemberRepository jpaAccessGroupMemberRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaUserMemberRepository jpaUserMemberRepository;

    @Autowired
    private UserDataProvider userDataProvider;

    @Override
    public List<GroupDAO> findAll() {
        return jpaAccessGroupRepository.findAll().stream().map(group -> (GroupDAO) group).collect(Collectors.toList());
    }

    @Override
    public GroupDAO findByGroupId(IdWrapper<?> id) throws GroupNotFoundException {

        if (!JpaAccessGroup.AccessGroupId.class.isAssignableFrom(id.getClass())) {
            throw new IllegalArgumentException("Id value is of the wrong type. " +
                    "Expected " + JpaAccessGroup.AccessGroupId.class.getCanonicalName() +
                    " got " + id.getClass().getCanonicalName() + ".");
        }
        Optional<JpaAccessGroup> optGroup = jpaAccessGroupRepository.findById((Long) id.getValue());

        if (optGroup.isPresent()) {
            return optGroup.get();
        }

        throw new GroupNotFoundException("id=" + ((Long) id.getValue()).toString());
    }

    @Override
    public GroupDAO findByGroupName(String groupName) throws GroupNotFoundException {

        Optional<JpaAccessGroup> optGroup = jpaAccessGroupRepository.findByGroupName(groupName);

        if (optGroup.isPresent()) {
            return optGroup.get();
        }

        throw new GroupNotFoundException("groupName=" + groupName);
    }

    @Override
    public GroupDAO createGroup(String groupName, UserDetails activeUser) throws UserNotFoundException, CryptographyException {
        Instant now = Instant.now();

        UserDAO activeUserDAO = userDataProvider.findUserByUserName(activeUser.getUsername());
        JpaAccessGroup group = new JpaAccessGroup(groupName);

        KeyPair keyPair ;
        try {
            keyPair = cryptographyService.generateRSAKeyPair();
            group.setPublicKey(keyPair.getPublic().getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptographyException(e);
        }

        group.setCreatedStamp(now);
        group.setCreatedByUser(activeUserDAO);
        group.setLastUpdatedStamp(now);
        group.setLastUpdatedByUser(activeUserDAO);

        group = jpaAccessGroupRepository.save(group);
        group.getMembers().add((JpaUserMember) createUserMember(group, keyPair.getPrivate(), activeUserDAO, 0, activeUser));

        return group;
    }

    private JpaGroupMember setContents(JpaGroupMember member,
                                       PrivateKey groupKey,
                                       PublicKey memberKey,
                                       int permissions,
                                       UserDetails activeUser) throws UserNotFoundException, CryptographyException {
        Instant now = Instant.now();
        UserDAO activeUserDAO = userDataProvider.findUserByUserName(activeUser.getUsername());

        member.setCreatedStamp(now);
        member.setCreatedByUser(activeUserDAO);
        member.setLastUpdatedStamp(now);
        member.setLastUpdatedByUser(activeUserDAO);
        member.setPermissions(permissions);

        try {
            SecretKey secretKey = cryptographyService.generateAESKey();
            byte[] encryptedKey = cryptographyService.encryptAES(secretKey, groupKey.getEncoded());
            byte[] secret = cryptographyService.encryptRSA(memberKey, secretKey.getEncoded());
            member.setSecret(secret);
            member.setGroupKey(encryptedKey);
        } catch (NoSuchAlgorithmException |  NoSuchProviderException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            throw new CryptographyException(e);
        }
        return member;
    }

    @Override
    public GroupMemberDAO createUserMember(GroupDAO group, PrivateKey groupKey, UserDAO memberUser, int permissions, UserDetails activeUser) throws UserNotFoundException, CryptographyException {

        JpaUserMember member = new JpaUserMember(new JpaUserMember.UserMemberPK(memberUser, group));
        try {
            setContents(member, groupKey, cryptographyService.createRSAPublicKey(memberUser.getPublicKey()), permissions, activeUser );
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptographyException(e);
        }
        member = jpaUserMemberRepository.save(member);
        group.getMembers().add(member);
       // jpaAccessGroupRepository.save((JpaAccessGroup)group);
        return member;
    }

    @Override
    public GroupMemberDAO createGroupMember(GroupDAO group, PrivateKey groupKey, GroupDAO memberGroup, int permissions, UserDetails activeUser) throws UserNotFoundException, CryptographyException {

        JpaAccessGroupMember member = new JpaAccessGroupMember(new JpaAccessGroupMember.AccessGroupMemberPK(memberGroup, group));
        try{
            setContents(member, groupKey, cryptographyService.createRSAPublicKey(memberGroup.getPublicKey()), permissions, activeUser );
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptographyException(e);
        }
        member = jpaAccessGroupMemberRepository.save(member);
        group.getMemberGroups().add(member);
        jpaAccessGroupRepository.save((JpaAccessGroup)group);
        return member;
    }

    @Override
    public GroupMemberDAO findGroupMemberById(PrincipalDAO principal, GroupDAO group) throws GroupMemberNotFoundException {

        GroupMemberDAO member;

        Optional<?> opt;

        if (principal instanceof GroupDAO) {
            opt = jpaAccessGroupMemberRepository.findById(new JpaAccessGroupMember.AccessGroupMemberPK((GroupDAO) principal, group));
        } else if (principal instanceof UserDAO) {
            opt = jpaUserMemberRepository.findById(new JpaUserMember.UserMemberPK((UserDAO) principal, group));
        } else {
            throw new IllegalArgumentException("Unknown member class found" + principal.getClass().getName());
        }

        if (opt.isPresent()) {
            return (GroupMemberDAO) opt.get();
        }

        throw new GroupMemberNotFoundException("unknown");
    }

    @Override
    public Set<GroupMemberDAO> findGroupMembers(GroupDAO group){

        Set<GroupMemberDAO> members = new HashSet<>();

        members.addAll(jpaAccessGroupMemberRepository.findAllByGroup((JpaAccessGroup) group));
        members.addAll(jpaUserMemberRepository.findAllByGroup((JpaAccessGroup) group));

        return members;
    }

    @Override
    public Set<GroupMemberDAO> findGroupMembersByGroupId(IdWrapper<?> groupId) throws GroupNotFoundException {

        GroupDAO group = findByGroupId(groupId);
        return findGroupMembers(group);
    }

    @Override
    public void removeUserMember(GroupMemberDAO member){

        //GroupDAO group = member.getGroup();
        //group.getMembers().remove(member);
        //jpaAccessGroupRepository.save((JpaAccessGroup) group);
        jpaUserMemberRepository.delete((JpaUserMember) member);

    }

    @Override
    public void removeGroupMember(GroupMemberDAO member){

        //GroupDAO group = member.getGroup();
        //group.getMemberGroups().remove(member);
        //jpaAccessGroupRepository.save((JpaAccessGroup) group);
        jpaAccessGroupMemberRepository.delete((JpaAccessGroupMember) member);

    }
}
