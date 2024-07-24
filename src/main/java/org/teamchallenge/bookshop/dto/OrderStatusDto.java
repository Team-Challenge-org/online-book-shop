package org.teamchallenge.bookshop.dto;

import org.teamchallenge.bookshop.enums.OrderStatus;

public class OrderStatusDto {
    private OrderStatus orderStatus;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
