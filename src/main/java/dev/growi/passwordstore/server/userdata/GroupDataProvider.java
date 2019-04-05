package dev.growi.passwordstore.server.userdata;

public interface GroupDataProvider {

    Group findByGroupId(Id<?> groupId);

}
