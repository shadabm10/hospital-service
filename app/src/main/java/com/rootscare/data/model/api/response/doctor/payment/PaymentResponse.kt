package com.rootscare.data.model.api.response.doctor.payment

import com.google.gson.annotations.SerializedName
import java.util.*

data class PaymentResponse(

	@field:SerializedName("result")
	val result: LinkedList<ResultItem>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ResultItem(

	@field:SerializedName("transaction_id")
	val transactionId: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("payment_type")
	val paymentType: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("subtotal")
	val subtotal: String? = null,

	@field:SerializedName("payment_status")
	val paymentStatus: String? = null,

	@field:SerializedName("vat")
	val vat: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("order_type")
	val orderType: String? = null
)
