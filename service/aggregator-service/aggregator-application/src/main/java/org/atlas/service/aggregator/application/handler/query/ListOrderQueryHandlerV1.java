package org.atlas.service.aggregator.application.handler.query;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "app.aggregator", havingValue = "api")
public class ListOrderQueryHandlerV1 {

}
