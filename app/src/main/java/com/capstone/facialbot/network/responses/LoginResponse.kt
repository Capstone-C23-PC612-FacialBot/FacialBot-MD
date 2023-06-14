package com.capstone.facialbot.network.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: List<DataItemm?>? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataItemm(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
