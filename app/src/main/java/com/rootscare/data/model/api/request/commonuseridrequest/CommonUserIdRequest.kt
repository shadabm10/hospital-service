package com.rootscare.data.model.api.request.commonuseridrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class CommonUserIdRequest(
	@field:JsonProperty("id")
	@field:SerializedName("id")
	var id: String? = null,
	@field:JsonProperty("service_type")
	@field:SerializedName("service_type")
	var service_type: String? = null
)