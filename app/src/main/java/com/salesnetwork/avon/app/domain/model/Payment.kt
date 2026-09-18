package com.salesnetwork.avon.app.domain.model

import java.time.Instant
import java.util.UUID

enum class PaymentStatus {
    PENDIENTE,
    CONFIRMADO,
    RECHAZADO,
    ANULADO
}

data class Payment(
    val id: String = "pay-${UUID.randomUUID()}",
    val orderId: String,
    val amount: Double,
    val method: PaymentMethod,
    val status: PaymentStatus = PaymentStatus.CONFIRMADO,
    val paidAt: String = Instant.now().toString(),
    val dueAt: String? = null,
    val evidenceUri: String? = null,
    val recordedByUserId: String = "",
    val notes: String = ""
)
