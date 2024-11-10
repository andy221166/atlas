package org.atlas.service.report.contract.model;

import lombok.Data;

import java.util.List;

@Data
public class ReportDto {

    private List<OrderDto> topHighestAmountOrders;
    private List<ProductDto> topBestSoldProducts;
    private List<UserDto> topHighestSpentUsers;
}
