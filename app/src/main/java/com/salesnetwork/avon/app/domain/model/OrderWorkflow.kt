package com.salesnetwork.avon.app.domain.model

object OrderWorkflow {
    fun canTransition(current: OrderStatus, next: OrderStatus): Boolean {
        if (current == next) return true
        return when (current) {
            OrderStatus.PENDIENTE -> next == OrderStatus.CONFIRMADO || next == OrderStatus.CANCELADO
            OrderStatus.CONFIRMADO -> next == OrderStatus.COBRADO || next == OrderStatus.CANCELADO
            OrderStatus.COBRADO -> next == OrderStatus.ENTREGADO || next == OrderStatus.CANCELADO
            OrderStatus.ENTREGADO, OrderStatus.CANCELADO -> false
        }
    }
}
