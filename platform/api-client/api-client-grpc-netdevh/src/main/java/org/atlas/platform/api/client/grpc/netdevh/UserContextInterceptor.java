package org.atlas.platform.api.client.grpc.netdevh;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.atlas.commons.context.CurrentUser;
import org.atlas.commons.context.CurrentUserContext;

@GrpcGlobalClientInterceptor
public class UserContextInterceptor implements ClientInterceptor {

  private static final Metadata.Key<String> USER_ID_HEADER =
      Metadata.Key.of(CustomHeaders.USER_ID, Metadata.ASCII_STRING_MARSHALLER);
  private static final Metadata.Key<String> USER_ROLE_HEADER =
      Metadata.Key.of(CustomHeaders.USER_ROLE, Metadata.ASCII_STRING_MARSHALLER);

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
      CallOptions callOptions,
      Channel next) {
    CurrentUser currentUser = CurrentUserContext.getCurrentUser();
    return new ForwardingClientCall.SimpleForwardingClientCall<>(
        next.newCall(method, callOptions)) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        if (currentUser != null) {
          headers.put(USER_ID_HEADER, String.valueOf(currentUser.getUserId()));
          headers.put(USER_ROLE_HEADER, currentUser.getRole().name());
        }
        super.start(responseListener, headers);
      }
    };
  }
}
