package org.atlas.service.notification.channels.websocket;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.core.EventType;
import org.atlas.platform.event.core.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.core.handler.EventHandler;
import org.atlas.platform.notification.websocket.order.OrderStatusNotificationWsService;
import org.atlas.service.notification.model.OrderStatusDto;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedWsHandler implements EventHandler<OrderConfirmedEvent> {

    private final OrderStatusNotificationWsService wsService;

    @Override
    public EventType supports() {
        return EventType.ORDER_CONFIRMED;
    }

    @Override
    public void handle(OrderConfirmedEvent event) {
        OrderStatusDto payload = new OrderStatusDto(OrderStatus.CONFIRMED);
        wsService.notify(event.getOrder().getId(), payload);
    }
}
