package org.atlas.platform.grpc.client.user;

import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.platform.api.client.contract.user.IUserServiceClient;
import org.atlas.platform.grpc.protobuf.user.ListUserRequestProto;
import org.atlas.platform.grpc.protobuf.user.UserListProto;
import org.atlas.platform.grpc.protobuf.user.UserProto;
import org.atlas.platform.grpc.protobuf.user.UserServiceGrpc;
import org.atlas.service.user.contract.model.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceClient implements IUserServiceClient {

  @GrpcClient("user")
  private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

  @Override
  public List<UserDto> listUser(List<Integer> userIds) {
    ListUserRequestProto requestProto = map(userIds);
    UserListProto responseProto = userServiceBlockingStub.listUser(requestProto);
    return map(responseProto);
  }

  private ListUserRequestProto map(List<Integer> userIds) {
    return ListUserRequestProto.newBuilder()
        .addAllUserId(userIds)
        .build();
  }

  private List<UserDto> map(UserListProto userListProto) {
    return userListProto.getUserList()
        .stream()
        .map(this::map)
        .toList();
  }

  private UserDto map(UserProto userProto) {
    UserDto userDto = new UserDto();
    userDto.setId(userProto.getId());
    userDto.setEmail(userProto.getEmail());
    userDto.setFirstName(userProto.getFirstName());
    userDto.setLastName(userProto.getLastName());
    userDto.setPhoneNumber(userProto.getPhoneNumber());
    return userDto;
  }
}
