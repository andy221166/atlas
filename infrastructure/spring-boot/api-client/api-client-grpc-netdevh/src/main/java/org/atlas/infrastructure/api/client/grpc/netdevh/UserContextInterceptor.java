package org.atlas.infrastructure.api.client.grpc.netdevh;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.atlas.framework.security.session.SessionContext;
import org.atlas.framework.security.session.SessionInfo;
import org.atlas.framework.security.enums.CustomClaim;

@GrpcGlobalClientInterceptor
public class UserContextInterceptor implements ClientInterceptor {

  private static final Metadata.Key<String> SESSION_ID_HEADER =
      Metadata.Key.of(CustomClaim.SESSION_ID.getHeader(), Metadata.ASCII_STRING_MARSHALLER);
  private static final Metadata.Key<String> USER_ID_HEADER =
      Metadata.Key.of(CustomClaim.USER_ID.getHeader(), Metadata.ASCII_STRING_MARSHALLER);
  private static final Metadata.Key<String> USER_ROLE_HEADER =
      Metadata.Key.of(CustomClaim.USER_ROLE.getHeader(), Metadata.ASCII_STRING_MARSHALLER);

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
      CallOptions callOptions,
      Channel next) {
    SessionInfo sessionInfo = SessionContext.get();
    return new ForwardingClientCall.SimpleForwardingClientCall<>(
        next.newCall(method, callOptions)) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        if (sessionInfo != null) {
          headers.put(SESSION_ID_HEADER, sessionInfo.getSessionId());
          headers.put(USER_ID_HEADER, String.valueOf(sessionInfo.getUserId()));
          headers.put(USER_ROLE_HEADER, sessionInfo.getUserRole().name());
        }
        super.start(responseListener, headers);
      }
    };
  }
}
