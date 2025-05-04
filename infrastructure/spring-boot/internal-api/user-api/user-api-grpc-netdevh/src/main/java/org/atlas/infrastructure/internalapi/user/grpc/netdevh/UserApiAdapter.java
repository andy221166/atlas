package org.atlas.infrastructure.internalapi.user.grpc.netdevh;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.domain.user.shared.internal.ListUserInput;
import org.atlas.domain.user.shared.internal.ListUserOutput;
import org.atlas.framework.internalapi.UserApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.grpc.protobuf.user.ListUserRequestProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.user.ListUserResponseProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.user.UserProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.user.UserServiceGrpc;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default")
@CircuitBreaker(name = "default")
@Bulkhead(name = "default")
public class UserApiAdapter implements UserApiPort {

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
