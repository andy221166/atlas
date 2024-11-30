package org.atlas.platform.grpc.server.user;

import io.grpc.stub.StreamObserver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.grpc.protobuf.user.ListUserRequestProto;
import org.atlas.platform.grpc.protobuf.user.UserListProto;
import org.atlas.platform.grpc.protobuf.user.UserProto;
import org.atlas.platform.grpc.protobuf.user.UserServiceGrpc;
import org.atlas.service.user.contract.model.UserDto;
import org.atlas.service.user.contract.query.ListUserQuery;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

  private final QueryGateway queryGateway;

  @Override
  public void listUser(ListUserRequestProto requestProto,
      StreamObserver<UserListProto> responseObserver) {
    ListUserQuery query = map(requestProto);
    try {
      List<UserDto> userDtoList = queryGateway.send(query);
      UserListProto responseProto = map(userDtoList);
      responseObserver.onNext(responseProto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ListUserQuery map(ListUserRequestProto requestProto) {
    return new ListUserQuery(requestProto.getUserIdList());
  }

  private UserListProto map(List<UserDto> userDtoList) {
    if (CollectionUtils.isEmpty(userDtoList)) {
      return UserListProto.getDefaultInstance();
    }
    UserListProto.Builder builder = UserListProto.newBuilder();
    userDtoList.forEach(userDto -> builder.addUser(map(userDto)));
    return builder.build();
  }

  private UserProto map(UserDto userDto) {
    return UserProto.newBuilder()
        .setId(userDto.getId())
        .setEmail(userDto.getEmail())
        .setFirstName(userDto.getFirstName())
        .setLastName(userDto.getLastName())
        .setPhoneNumber(userDto.getPhoneNumber())
        .build();
  }
}
