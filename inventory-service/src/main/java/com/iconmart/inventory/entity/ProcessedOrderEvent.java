package com.iconmart.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_order_events")
public class ProcessedOrderEvent {

    @Id
    private Long orderId;

    protected ProcessedOrderEvent() {}

    public ProcessedOrderEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
