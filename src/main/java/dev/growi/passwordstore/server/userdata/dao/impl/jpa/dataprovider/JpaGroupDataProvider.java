package dev.growi.passwordstore.server.userdata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.core.authentication.AuthenticationFacade;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupMemberNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.configuration.JpaDatasourceCondition;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaGroupMember;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.*;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroup;
import dev.growi.passwordstore.server.shared.service.CryptographyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Conditional(JpaDatasourceCondition.class)
public class JpaGroupDataProvider implements GroupDataProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private CryptographyService cryptographyService;

    @Autowired
    private JpaAccessGroupRepository jpaAccessGroupRepository;

    @Autowired
    private JpaGroupMemberRepository jpaGroupMemberRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private UserDataProvider userDataProvider;

    @Override
    public List<GroupDAO> findAll() {
        return jpaAccessGroupRepository.findAll().stream().map(group -> (GroupDAO) group).collect(Collectors.toList());
    }

    @Override
    public GroupDAO findByGroupId(Long id) throws GroupNotFoundException {

        Optional<JpaAccessGroup> optGroup = jpaAccessGroupRepository.findById(id);

        if (optGroup.isPresent()) {
            return optGroup.get();
        }

        throw new GroupNotFoundException("id=" + id);
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
    public GroupDAO createGroup(String groupName) throws CryptographyException, UserNotFoundException {
        Instant now = Instant.now();

        UserDAO activeUserDAO = userDataProvider.findByUserName(authenticationFacade.getAuthentication().getName());
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
        group.getMembers().add( createGroupMember(group, keyPair.getPrivate(), activeUserDAO, 0));

        return group;
    }

    private JpaGroupMember setContents(JpaGroupMember member,
                                       PrivateKey groupKey,
                                       PublicKey memberKey,
                                       int permissions) throws UserNotFoundException, CryptographyException {
        Instant now = Instant.now();
        UserDAO activeUserDAO = userDataProvider.findByUserName(authenticationFacade.getAuthentication().getName());

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
    public GroupMemberDAO createGroupMember(GroupDAO group,
                                            PrivateKey groupKey,
                                            PrincipalDAO memberDAO,
                                            int permissions)
            throws UserNotFoundException, CryptographyException {

        JpaGroupMember member = new JpaGroupMember(new JpaGroupMember.GroupMemberPK(memberDAO, group));

        try {
            setContents(member, groupKey, cryptographyService.createRSAPublicKey(memberDAO.getPublicKey()), permissions );
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptographyException(e);
        }

        member = jpaGroupMemberRepository.save(member);
        group.getMembers().add(member);

        return member;
    }

    @Override
    public GroupMemberDAO findGroupMemberById(PrincipalDAO principal, GroupDAO group)
            throws GroupMemberNotFoundException {

        GroupMemberDAO member;
        Optional<?> opt = jpaGroupMemberRepository.findById(new JpaGroupMember.GroupMemberPK(principal, group));

        if (opt.isPresent()) {
            return (GroupMemberDAO) opt.get();
        }
        throw new GroupMemberNotFoundException("unknown");
    }

    @Override
    public Set<GroupMemberDAO> findGroupMembers(GroupDAO group){

        return jpaGroupMemberRepository.findAllByGroupId(group.getId());
    }

    //TODO: remove, see above
    @Override
    public Set<GroupMemberDAO> findGroupMembersByGroupId(Long id) throws GroupNotFoundException {

        GroupDAO group = findByGroupId(id);
        return findGroupMembers(group);
    }

    @Override
    public void removeUserMember(GroupMemberDAO member){
        jpaGroupMemberRepository.delete((JpaGroupMember) member);
    }

    @Override
    public void removeGroupMember(GroupMemberDAO member){
        jpaGroupMemberRepository.delete((JpaGroupMember) member);
    }

    @Override
    public GroupDAO save(GroupDAO group) throws UserNotFoundException {

        UserDAO activeUserDAO = userDataProvider.findByUserName(authenticationFacade.getAuthentication().getName());

        group.setLastUpdatedByUser(activeUserDAO);
        group.setLastUpdatedStamp(Instant.now());

        return jpaAccessGroupRepository.save((JpaAccessGroup) group);
    }

    @Override
    public void deleteById(Long groupId){

        jpaAccessGroupRepository.deleteById(groupId);
    }
}
