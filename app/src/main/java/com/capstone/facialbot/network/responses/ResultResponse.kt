package com.capstone.facialbot.network.responses

import com.google.gson.annotations.SerializedName

data class ResultResponse(

	@field:SerializedName("y_end")
	val yEnd: String? = null,

	@field:SerializedName("x_end")
	val xEnd: String? = null,

	@field:SerializedName("y_start")
	val yStart: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null,

	@field:SerializedName("x_start")
	val xStart: String? = null
)
