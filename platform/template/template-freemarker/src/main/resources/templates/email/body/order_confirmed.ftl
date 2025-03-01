<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
        }
        .header {
            background-color: #f8f8f8;
            padding: 10px;
            text-align: center;
            font-size: 20px;
            font-weight: bold;
        }
        .content {
            margin: 20px;
        }
        .orderPayload-summary {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }
        .orderPayload-summary th, .orderPayload-summary td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        .orderPayload-summary th {
            background-color: #f2f2f2;
        }
        .total {
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="header">Order Confirmation</div>

<div class="content">
    <p>Dear ${orderPayload.userEntity.firstName} ${orderPayload.userEntity.lastName},</p>

    <p>Thank you for your orderPayload! Your orderPayload <strong>${orderPayload.id}</strong>, placed on <strong>${orderPayload.createdAt?string("yyyy-MM-dd HH:mm:ss")}</strong>, was confirmed.</p>

    <table class="orderPayload-summary">
        <thead>
        <tr>
            <th>Product ID</th>
            <th>Product Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Subtotal</th>
        </tr>
        </thead>
        <tbody>
        <#list orderPayload.orderItems as orderItem>
            <tr>
                <td>${orderItem.aggProduct.id}</td>
                <td>${orderItem.aggProduct.name}</td>
                <td>${orderItem.aggProduct.price?string("$#,##0.00")}</td>
                <td>${orderItem.quantity}</td>
                <td>${(orderItem.aggProduct.price * orderItem.quantity)?string("$#,##0.00")}</td>
            </tr>
        </#list>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="4" class="total">Total Amount</td>
            <td class="total">${orderPayload.amount?string("$#,##0.00")}</td>
        </tr>
        </tfoot>
    </table>

    <p>Best regards,</p>
    <p>Atlas</p>
</div>
</body>
</html>
