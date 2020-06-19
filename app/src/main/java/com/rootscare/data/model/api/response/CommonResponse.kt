package com.rootscare.data.model.api.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(

	@field:SerializedName("result")
	val result: Any? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
