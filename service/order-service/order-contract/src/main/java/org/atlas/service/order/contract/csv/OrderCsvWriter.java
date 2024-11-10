package org.atlas.service.order.contract.csv;

import org.atlas.service.order.domain.Order;

import java.util.List;

public interface OrderCsvWriter {

    byte[] write(List<Order> orders) throws Exception;
}
