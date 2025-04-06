package org.atlas.edge.api.gateway.springcloudgateway.route;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/gateway/routes")
@RequiredArgsConstructor
public class RouteController {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final RouteLocator routeLocator;

  @GetMapping
  public Mono<String> listRoutes() {
    return routeLocator.getRoutes()
        .map(route -> route.getId() + ": " + route.getUri())
        .collectList()
        .map(routes -> String.join("\n", routes));
  }

  @PostMapping("/refresh")
  public Mono<String> refreshRoutes() {
    applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    return Mono.just("Routes refreshed successfully");
  }
}
