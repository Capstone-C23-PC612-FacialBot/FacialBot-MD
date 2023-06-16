package com.capstone.facialbot.network.responses

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class Data(

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
