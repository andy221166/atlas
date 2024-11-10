package org.atlas.service.notification.channels.websocket;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.core.EventType;
import org.atlas.platform.event.core.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.core.handler.EventHandler;
import org.atlas.platform.notification.websocket.order.OrderStatusNotificationWsService;
import org.atlas.service.notification.model.OrderStatusDto;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCanceledWsHandler implements EventHandler<OrderCanceledEvent> {

    private final OrderStatusNotificationWsService wsService;

    @Override
    public EventType supports() {
        return EventType.ORDER_CANCELED;
    }

    @Override
    public void handle(OrderCanceledEvent event) {
        OrderStatusDto payload = new OrderStatusDto(OrderStatus.CANCELED, event.getOrder().getCanceledReason());
        wsService.notify(event.getOrder().getId(), payload);
    }
}
