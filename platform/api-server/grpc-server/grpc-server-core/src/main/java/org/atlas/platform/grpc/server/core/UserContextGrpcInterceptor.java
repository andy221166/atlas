package org.atlas.platform.grpc.server.core;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.atlas.commons.constant.CustomHeaders;
import org.atlas.commons.context.CurrentUser;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.service.user.domain.shared.enums.Role;

@GrpcGlobalServerInterceptor
@Slf4j
public class UserContextGrpcInterceptor implements ServerInterceptor {

  private static final Metadata.Key<String> USER_ID_HEADER =
      Metadata.Key.of(CustomHeaders.USER_ID, Metadata.ASCII_STRING_MARSHALLER);
  private static final Metadata.Key<String> USER_ROLE_HEADER =
      Metadata.Key.of(CustomHeaders.USER_ROLE, Metadata.ASCII_STRING_MARSHALLER);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {
    String userId = metadata.get(USER_ID_HEADER);
    String userRole = metadata.get(USER_ROLE_HEADER);
    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userRole)) {
      CurrentUser currentUser = new CurrentUser();
      currentUser.setUserId(Integer.valueOf(userId));
      currentUser.setRole(Role.valueOf(userRole));
      CurrentUserContext.setCurrentUser(currentUser);
    }
    return serverCallHandler.startCall(serverCall, metadata);
  }
}
