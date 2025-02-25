package org.atlas.service.user.api.client.grpc.netdevh;

import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.platform.api.server.grpc.protobuf.user.ListUserRequestProto;
import org.atlas.platform.api.server.grpc.protobuf.user.ListUserResponseProto;
import org.atlas.platform.api.server.grpc.protobuf.user.UserProto;
import org.atlas.platform.api.server.grpc.protobuf.user.UserServiceGrpc;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.api.client.UserApiClient;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.springframework.stereotype.Component;

@Component
public class UserApiClientImpl implements UserApiClient {

  @GrpcClient("user-service")
  private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

  @Override
  public ListUserUseCase.Output call(ListUserUseCase.Input input) {
    ListUserRequestProto requestProto = map(input);
    ListUserResponseProto responseProto = userServiceBlockingStub.listUser(requestProto);
    return map(responseProto);
  }

  private ListUserRequestProto map(ListUserUseCase.Input input) {
    return ListUserRequestProto.newBuilder()
        .addAllId(input.getIds())
        .build();
  }

  private ListUserUseCase.Output map(ListUserResponseProto responseProto) {
    List<ListUserUseCase.Output.User> users = responseProto.getUserList()
        .stream()
        .map(this::map)
        .toList();
    return new ListUserUseCase.Output(users);
  }

  private ListUserUseCase.Output.User map(UserProto userProto) {
    ListUserUseCase.Output.User user = ObjectMapperUtil.getInstance().map(userProto, ListUserUseCase.Output.User.class);
    user.setRole(Role.valueOf(userProto.getRole()));
    return user;
  }
}
