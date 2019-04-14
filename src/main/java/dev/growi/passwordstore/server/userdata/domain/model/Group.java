package dev.growi.passwordstore.server.userdata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupMemberNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.domain.service.CryptographyService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
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
@Configurable(preConstruction = true, dependencyCheck = true, autowire = Autowire.BY_TYPE)
public class Group extends Principal {

    @Autowired
    private GroupDataProvider groupDataProvider;

    @Autowired
    private UserDataProvider userDataProvider;

    @Autowired
    private CryptographyService cryptographyService;

    private String groupName;

    public Group(GroupDAO groupDAO)  {
        super(groupDAO);
        this.id = groupDAO.getGroupId();
        this.groupName = groupDAO.getGroupName();
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

        UserDAO activeUserDAO = userDataProvider.findUserByUserName(activeUser.getUsername());
        UserDAO memberDAO = userDataProvider.findUserById(member.getId());
        GroupDAO groupDAO = groupDataProvider.findByGroupId(this.getId());

        PrivateKey privateKey = extractKey(activeUserDAO, password);
        groupDataProvider.createUserMember(groupDAO, privateKey, memberDAO, permissions, activeUser);
    }

    public void addMember(Group member, int permissions, UserDetails activeUser, String password)
            throws GroupNotFoundException, UserNotFoundException, CryptographyException {

        UserDAO activeUserDAO = userDataProvider.findUserByUserName(activeUser.getUsername());
        GroupDAO memberDAO = groupDataProvider.findByGroupId(member.getId());
        GroupDAO groupDAO = groupDataProvider.findByGroupId(this.getId());

        PrivateKey privateKey = extractKey(activeUserDAO, password);
        groupDataProvider.createGroupMember(groupDAO, privateKey, memberDAO, permissions, activeUser);
    }

    public void removeMember(User userMember) throws GroupNotFoundException, UserNotFoundException, GroupMemberNotFoundException {

        GroupDAO groupDAO = groupDataProvider.findByGroupId(this.getId());
        UserDAO userMemberDAO = userDataProvider.findUserById(userMember.getId());
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
        Set<IdWrapper<?>> visited = new HashSet<>();
        Queue<GroupDAO> queue = new LinkedList<>();

        queue.add(start);                                       // mit Start-Knoten beginnen
        visited.add(start.getGroupId());
        while(!queue.isEmpty() ) {                              // solange queue nicht leer ist
            GroupDAO node = queue.poll();                       // erstes Element von der queue nehmen

            for(GroupMemberDAO child : node.getMembers()){
                if(((UserDAO)child.getMember()).getUserId().equals(user.getUserId())){
                    return buildPath(parents, node);
                }
            }

            for(GroupMemberDAO child : node.getMemberGroups()) {                        // alle Nachfolge-Knoten, …
                if (!visited.contains(((GroupDAO)child.getMember()).getGroupId())) {    // … die noch nicht besucht wurden …
                    parents.put((GroupDAO)child.getMember(), node);
                    queue.add((GroupDAO)child.getMember());                             // … zur queue hinzufügen…
                    visited.add(((GroupDAO)child.getMember()).getGroupId());            // … und als bereits gesehen markieren
                }
            }
        }
        return null;                                                    // Knoten kann nicht erreicht werden
    }

    private List<GroupDAO> buildPath(Map<GroupDAO, GroupDAO> parents, GroupDAO goal){

        List<GroupDAO> path = new ArrayList<>();
        GroupDAO child = goal;

        path.add(child);
        while(parents.containsKey(child)){
            child = parents.get(child);
            path.add(child);
        }
        return path;
    }
}
