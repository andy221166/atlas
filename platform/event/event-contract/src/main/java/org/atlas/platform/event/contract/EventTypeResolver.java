package org.atlas.platform.event.contract;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;

public class EventTypeResolver extends TypeIdResolverBase {

    private static final Map<EventType, Class<? extends DomainEvent>> EVENT_TYPE_TO_CLASS = new HashMap<>();
    private static final Map<Class<? extends DomainEvent>, EventType> CLASS_TO_EVENT_TYPE = new HashMap<>();

    static {
        // Map EventType to DomainEvent subclasses
        EVENT_TYPE_TO_CLASS.put(EventType.ORDER_CREATED, OrderCreatedEvent.class);
        EVENT_TYPE_TO_CLASS.put(EventType.RESERVE_QUANTITY_SUCCEEDED, ReserveQuantitySucceededEvent.class);
        EVENT_TYPE_TO_CLASS.put(EventType.RESERVE_QUANTITY_FAILED, ReserveQuantityFailedEvent.class);
        EVENT_TYPE_TO_CLASS.put(EventType.ORDER_CONFIRMED, OrderConfirmedEvent.class);
        EVENT_TYPE_TO_CLASS.put(EventType.ORDER_CANCELED, OrderCanceledEvent.class);

        // Build reverse mapping for serialization
        EVENT_TYPE_TO_CLASS.forEach((key, value) -> CLASS_TO_EVENT_TYPE.put(value, key));
    }

    @Override
    public String idFromValue(Object value) {
        EventType eventType = getEventTypeByClass(value.getClass());
        if (eventType == null) {
            throw new IllegalArgumentException("Unknown event class: " + value.getClass());
        }
        return eventType.name();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        EventType eventType = EventType.valueOf(id);
        Class<? extends DomainEvent> clazz = getClassByEventType(eventType);
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown event type: " + id);
        }
        return context.getTypeFactory().constructType(clazz);
    }

    @Override
    public Id getMechanism() {
        return Id.CUSTOM;
    }

    public static EventType getEventTypeByClass(Class<?> clazz) {
        return CLASS_TO_EVENT_TYPE.get(clazz);
    }

    public static Class<? extends DomainEvent> getClassByEventType(EventType eventType) {
        return EVENT_TYPE_TO_CLASS.get(eventType);
    }
}

