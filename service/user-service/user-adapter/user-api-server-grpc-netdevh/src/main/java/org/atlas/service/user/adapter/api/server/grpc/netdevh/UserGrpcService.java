package org.atlas.service.user.adapter.api.server.grpc.netdevh;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.api.server.grpc.protobuf.user.ListUserRequestProto;
import org.atlas.platform.api.server.grpc.protobuf.user.ListUserResponseProto;
import org.atlas.platform.api.server.grpc.protobuf.user.UserProto;
import org.atlas.platform.api.server.grpc.protobuf.user.UserServiceGrpc;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserOutput;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

  private final ListUserUseCase listUserUseCase;

  @Override
  public void listUser(ListUserRequestProto requestProto,
      StreamObserver<ListUserResponseProto> responseObserver) {
    ListUserInput input = map(requestProto);
    try {
      ListUserOutput output = listUserUseCase.handle(input);
      ListUserResponseProto responseProto = map(output);
      responseObserver.onNext(responseProto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ListUserInput map(ListUserRequestProto requestProto) {
    return new ListUserInput(requestProto.getIdList());
  }

  private ListUserResponseProto map(ListUserOutput output) {
    if (CollectionUtils.isEmpty(output.getUsers())) {
      return ListUserResponseProto.getDefaultInstance();
    }
    ListUserResponseProto.Builder builder = ListUserResponseProto.newBuilder();
    output.getUsers().forEach(user -> builder.addUser(map(user)));
    return builder.build();
  }

  private UserProto map(ListUserOutput.User user) {
    return UserProto.newBuilder()
        .setId(user.getId())
        .setUsername(user.getUsername())
        .setFirstName(user.getFirstName())
        .setLastName(user.getLastName())
        .setEmail(user.getEmail())
        .setPhoneNumber(user.getPhoneNumber())
        .setRole(user.getRole().name())
        .build();
  }
}
