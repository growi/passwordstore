package dev.growi.passwordstore.server.userdata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.configuration.JpaDatasourceCondition;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaAccessGroup;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaAccessGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Conditional(JpaDatasourceCondition.class)
public class JpaGroupDataProvider implements GroupDataProvider {

    @Autowired
    private JpaAccessGroupRepository jpaAccessGroupRepository;

    @Override
    public GroupDAO findByGroupId(IdWrapper<?> id) throws GroupNotFoundException {

        if (!JpaAccessGroup.AccessGroupId.class.isAssignableFrom(id.getIdClass())) {
            throw new IllegalArgumentException("Id value is of the wrong type. " +
                    "Expected " + JpaAccessGroup.AccessGroupId.class.getCanonicalName() +
                    " got " + id.getIdClass().getCanonicalName() + ".");
        }
        Optional<JpaAccessGroup> optGroup = jpaAccessGroupRepository.findById((Long) id.getValue());

        if(optGroup.isPresent()){
            return optGroup.get();
        }

        throw new GroupNotFoundException("id="+((Long) id.getValue()).toString());
    }

    @Override
    public GroupDAO findByGroupName(String groupName) throws GroupNotFoundException {

        Optional<JpaAccessGroup> optGroup = jpaAccessGroupRepository.findByGroupName(groupName);

        if(optGroup.isPresent()){
            return optGroup.get();
        }

        throw new GroupNotFoundException("groupName="+groupName);
    }
}
