package org.atlas.infrastructure.api.client.core.grpc.netdevh;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.atlas.framework.context.UserContext;
import org.atlas.framework.context.UserInfo;
import org.atlas.framework.security.enums.CustomClaim;

@GrpcGlobalClientInterceptor
public class UserContextInterceptor implements ClientInterceptor {

  private static final Metadata.Key<String> USER_ID_HEADER =
      Metadata.Key.of(CustomClaim.USER_ID.getHeader(), Metadata.ASCII_STRING_MARSHALLER);
  private static final Metadata.Key<String> USER_ROLE_HEADER =
      Metadata.Key.of(CustomClaim.USER_ROLE.getHeader(), Metadata.ASCII_STRING_MARSHALLER);
  private static final Metadata.Key<String> SESSION_ID_HEADER =
      Metadata.Key.of(CustomClaim.SESSION_ID.getHeader(), Metadata.ASCII_STRING_MARSHALLER);

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
      CallOptions callOptions,
      Channel next) {
    UserInfo userInfo = UserContext.get();
    return new ForwardingClientCall.SimpleForwardingClientCall<>(
        next.newCall(method, callOptions)) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        if (userInfo != null) {
          headers.put(USER_ID_HEADER, String.valueOf(userInfo.getUserId()));
          headers.put(USER_ROLE_HEADER, userInfo.getRole().name());
          headers.put(SESSION_ID_HEADER, userInfo.getSessionId());
        }
        super.start(responseListener, headers);
      }
    };
  }
}
