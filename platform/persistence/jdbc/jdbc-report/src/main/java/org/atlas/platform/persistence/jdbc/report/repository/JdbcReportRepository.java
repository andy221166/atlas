package org.atlas.platform.persistence.jdbc.report.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcReportRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
        String sql = """
            select o.id, o.user_id, o.first_name, o.last_name, o.amount, o.created_at
            from orders o
            where o.created_at between :startDate and :endDate
            order by o.amount desc
            limit :limit
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("startDate", startDate);
        params.addValue("endDate", endDate);
        params.addValue("limit", limit);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            OrderDto order = new OrderDto();
            order.setId(rs.getInt("id"));
            order.setUserId(rs.getInt("user_id"));
            order.setFirstName(rs.getString("first_name"));
            order.setLastName(rs.getString("last_name"));
            order.setAmount(rs.getBigDecimal("amount"));
            order.setCreatedAt(rs.getTimestamp("created_at"));
            return order;
        });
    }

    public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
        String sql = """
            select oi.product_id, oi.product_name, sum(oi.quantity) as total_quantity
            from order_item oi
            inner join orders o on o.id = oi.order_id
            where o.created_at between :startDate and :endDate
            group by oi.product_id, oi.product_name,
            order by total_quantity desc
            limit :limit
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("startDate", startDate);
        params.addValue("endDate", endDate);
        params.addValue("limit", limit);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            ProductDto product = new ProductDto();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setTotalQuantity(rs.getInt("total_quantity"));
            return product;
        });
    }

    public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
        String sql = """
            select o.user_id, o.first_name, o.last_name, sum(o.amount) as total_amount
            from orders o
            where o.created_at between :startDate and :endDate
            group by o.user_id, o.first_name, o.last_name
            order by total_amount desc
            limit :limit
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("startDate", startDate);
        params.addValue("endDate", endDate);
        params.addValue("limit", limit);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            UserDto user = new UserDto();
            user.setId(rs.getInt("user_id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setTotalAmount(rs.getBigDecimal("total_amount"));
            return user;
        });
    }
}
