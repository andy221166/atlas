package org.atlas.platform.excel.easyexcel.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderWriteModel {

    @ExcelProperty(value = "Order ID")
    private Integer id;

    @ExcelProperty(value = "Amount")
    private BigDecimal amount;

    @ExcelProperty(value = "Status", converter = OrderStatusConverter.class)
    private String status;

    @ExcelProperty(value = "Canceled Reason")
    private String canceledReason;

    @ExcelProperty(value = "Created At")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
