package com.capstone.facialbot.network.responses

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataItem(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
