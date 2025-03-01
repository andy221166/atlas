package org.atlas.platform.event.core;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"rawtypes", "unchecked"})
@RequiredArgsConstructor
@Slf4j
public class EventDispatcher {

  private final List<EventHandler> handlers;
  private final EventIdempotenceService eventIdempotenceService;

  private Map<EventType, List<EventHandler>> handlerMap;

  @PostConstruct
  public void init() {
    handlerMap = handlers.stream()
        .collect(Collectors.groupingBy(EventHandler::supports, Collectors.toList()));
  }

  public void dispatch(DomainEvent event) {
    // Obtain handlers
    List<EventHandler> handlersByType = handlerMap.get(event.getEventType());
    if (CollectionUtils.isEmpty(handlersByType)) {
      log.warn("No handler found for event type {}", event.getEventType());
      return;
    }

    // Double-check whether event has been processed
    if (!eventIdempotenceService.isNew(event)) {
      log.error("Event has already been processed: {}", event);
      return;
    }

    try {
      handlersByType.forEach(handler -> {
        try {
          handler.handle(event);
        } catch (Exception e) {
          log.error("Failed to handle event: event={}, handler={}",
              event, handler.getClass().getSimpleName(), e);
        }
      });
    } finally {
      eventIdempotenceService.deleteKey(event);
    }
  }
}
