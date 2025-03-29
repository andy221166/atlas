package org.atlas.platform.api.client.user.grpc.netdevh;

import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.api.server.grpc.protobuf.user.ListUserRequestProto;
import org.atlas.platform.api.server.grpc.protobuf.user.ListUserResponseProto;
import org.atlas.platform.api.server.grpc.protobuf.user.UserProto;
import org.atlas.platform.api.server.grpc.protobuf.user.UserServiceGrpc;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserOutput;
import org.springframework.stereotype.Component;

@Component
public class UserApiClientImpl implements UserApiClient {

  @GrpcClient("user-service")
  private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

  @Override
  public ListUserOutput call(ListUserInput input) {
    ListUserRequestProto requestProto = map(input);
    ListUserResponseProto responseProto = userServiceBlockingStub.listUser(requestProto);
    return map(responseProto);
  }

  private ListUserRequestProto map(ListUserInput input) {
    return ListUserRequestProto.newBuilder()
        .addAllId(input.getIds())
        .build();
  }

  private ListUserOutput map(ListUserResponseProto responseProto) {
    List<ListUserOutput.User> users = responseProto.getUserList()
        .stream()
        .map(this::map)
        .toList();
    return new ListUserOutput(users);
  }

  private ListUserOutput.User map(UserProto userProto) {
    ListUserOutput.User user = ObjectMapperUtil.getInstance()
        .map(userProto, ListUserOutput.User.class);
    user.setRole(Role.valueOf(userProto.getRole()));
    return user;
  }
}
