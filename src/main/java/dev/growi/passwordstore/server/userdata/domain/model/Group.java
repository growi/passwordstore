package dev.growi.passwordstore.server.userdata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupMemberNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.shared.service.CryptographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "groupName")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group extends Principal<Group, GroupDAO> {

    @Autowired
    private GroupDataProvider groupDataProvider;

    @Autowired
    private UserDataProvider userDataProvider;

    @Autowired
    private CryptographyService cryptographyService;

    private String groupName;

    public static Group load(Long id) throws GroupNotFoundException {
        Group group = new Group();
        group.setProperties(group.groupDataProvider.findByGroupId(id));

        return group;
    }

    public static Group load(String name) throws GroupNotFoundException {
        Group group = new Group();
        group.setProperties(group.groupDataProvider.findByGroupName(name));

        return group;
    }

    public static Group create(Group template) throws GroupNotFoundException, UserNotFoundException, CryptographyException {
        Group group = create(template.getGroupName());
        group.update(group, true);

        return group.save();
    }

    public static Group create(String name) throws UserNotFoundException, CryptographyException{
        Group group = new Group();
        group.setProperties(group.groupDataProvider.createGroup(name));

        return group;
    }

    private Group(){}

    public Group(GroupDAO groupDAO) {
        setProperties(groupDAO);
    }

    @Override
    protected void setProperties(GroupDAO groupAO) {
        super.setProperties(groupAO);
        this.id = groupAO.getId();
        this.groupName = groupAO.getGroupName();
    }

    // id and monitored fields wont be updated!
    @Override
    public void update(Group template, boolean ignoreNull){
        super.update(template, ignoreNull);
        if(!ignoreNull || template.getGroupName() != null) this.groupName = template.getGroupName();
    }

    @Override
    protected GroupDAO getDao() throws GroupNotFoundException {
        GroupDAO dao = groupDataProvider.findByGroupId(this.getId());
        updateDao(dao);

        return dao;
    }

    // id and monitored fields wont be updated!
    @Override
    protected GroupDAO updateDao(GroupDAO dao) {
        super.updateDao(dao);
        dao.setGroupName(this.getGroupName());

        return dao;
    }

    @Override
    public Group save() throws GroupNotFoundException, UserNotFoundException {
        this.setProperties(groupDataProvider.save(getDao()));
        return this;
    }

    @Override
    public void delete() {
        userDataProvider.deleteById(this.getId());
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Set<Principal> getMembers() throws GroupNotFoundException {
        return groupDataProvider.findGroupMembersByGroupId(this.getId())
                .stream()
                .map(member -> member.getMember())
                .map(principal -> principal instanceof UserDAO ? new User((UserDAO) principal) : new Group((GroupDAO) principal))
                .collect(Collectors.toSet());
    }

    public void addMember(User member, int permissions, UserDetails activeUser, String password)
            throws GroupNotFoundException, UserNotFoundException, CryptographyException {

        UserDAO activeUserDAO = userDataProvider.findByUserName(activeUser.getUsername());
        UserDAO memberDAO = userDataProvider.findById(member.getId());
        GroupDAO groupDAO = groupDataProvider.findByGroupId(this.getId());

        PrivateKey privateKey = extractKey(activeUserDAO, password);
        groupDataProvider.createGroupMember(groupDAO, privateKey, memberDAO, permissions);
    }

    public void addMember(Group member, int permissions, UserDetails activeUser, String password)
            throws GroupNotFoundException, UserNotFoundException, CryptographyException {

        UserDAO activeUserDAO = userDataProvider.findByUserName(activeUser.getUsername());
        GroupDAO memberDAO = groupDataProvider.findByGroupId(member.getId());
        GroupDAO groupDAO = groupDataProvider.findByGroupId(this.getId());

        PrivateKey privateKey = extractKey(activeUserDAO, password);
        groupDataProvider.createGroupMember(groupDAO, privateKey, memberDAO, permissions);
    }

    public void removeMember(User userMember) throws GroupNotFoundException, UserNotFoundException, GroupMemberNotFoundException {

        GroupDAO groupDAO = groupDataProvider.findByGroupId(this.getId());
        UserDAO userMemberDAO = userDataProvider.findById(userMember.getId());
        GroupMemberDAO member = groupDataProvider.findGroupMemberById(userMemberDAO, groupDAO);
        groupDataProvider.removeUserMember(member);
    }

    public void removeMember(Group groupMember) throws GroupNotFoundException, GroupMemberNotFoundException {

        GroupDAO groupDAO = groupDataProvider.findByGroupId(this.getId());
        GroupDAO groupMemberDAO = groupDataProvider.findByGroupId(this.getId());
        GroupMemberDAO member = groupDataProvider.findGroupMemberById(groupMemberDAO, groupDAO);
        groupDataProvider.removeGroupMember(member);
    }

    private PrivateKey extractKey(UserDAO userDAO, String password)
            throws CryptographyException, GroupNotFoundException {

        List<GroupDAO> path = searchBFS(groupDataProvider.findByGroupId(this.getId()), userDAO);

        try {
            PrivateKey principalKey = cryptographyService.pbeDecrypt(password, userDAO.getPrivateKey());
            PrincipalDAO principal = userDAO;
            for (GroupDAO group : path) {

                GroupMemberDAO groupMember = groupDataProvider.findGroupMemberById(principal, group);

                SecretKey secretKey = cryptographyService.createAESKey(cryptographyService.decryptRSA(principalKey, groupMember.getSecret()));
                principalKey = cryptographyService.createRSAPrivateKey(cryptographyService.decryptAES(secretKey, groupMember.getGroupKey()));
                principal = group;
            }
            return principalKey;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
                | NoSuchProviderException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException
                | NoSuchPaddingException | IOException e) {
            throw new CryptographyException(e);
        }
    }

    private List<GroupDAO> searchBFS(GroupDAO start, UserDAO user) {

        Map<GroupDAO, GroupDAO> parents = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        Queue<GroupDAO> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start.getId());
        while (!queue.isEmpty()) {
            GroupDAO node = queue.poll();

            for (GroupMemberDAO child : node.getMembers()) {
                if (UserDAO.class.isAssignableFrom(child.getMember().getClass())) {
                    if (((UserDAO) child.getMember()).getId().equals(user.getId())) {
                        return buildPath(parents, node);
                    }
                } else {
                    if (!visited.contains(((GroupDAO) child.getMember()).getId())) {
                        parents.put((GroupDAO) child.getMember(), node);
                        queue.add((GroupDAO) child.getMember());
                        visited.add(((GroupDAO) child.getMember()).getId());
                    }
                }
            }
        }
        return null;
    }

    private List<GroupDAO> buildPath(Map<GroupDAO, GroupDAO> parents, GroupDAO goal) {

        List<GroupDAO> path = new ArrayList<>();
        GroupDAO child = goal;

        path.add(child);
        while (parents.containsKey(child)) {
            child = parents.get(child);
            path.add(child);
        }
        return path;
    }
}
