package org.atlas.service.notification.adapter.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * This config is useful when you just need to send messages to client one way.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketServerConfig implements WebSocketMessageBrokerConfigurer {

  public static final String DESTINATION_PREFIX = "/topic";
  private static final String STOMP_ENDPOINT = "/notification/ws";
  // TODO: Consider to put it into config-server
  private static final String[] ALLOWED_HOSTS = {
      "http://localhost:9000" // Frontend
  };

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // The "/topic" prefix is for broadcasting messages to subscribers
    config.enableSimpleBroker(DESTINATION_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Client connects to this endpoint
    registry.addEndpoint(STOMP_ENDPOINT)
        .setAllowedOrigins(ALLOWED_HOSTS)
        .withSockJS();
  }
}
