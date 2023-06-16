package com.capstone.facialbot.network

import com.capstone.facialbot.network.responses.LoginResponse
import com.capstone.facialbot.network.responses.RegisterResponse
import com.capstone.facialbot.network.responses.ResultResponse
import com.capstone.facialbot.network.responses.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // fungsi untuk mengirimkan data saat register
    @POST("api/register")
    @Headers("Content-Type: application/json")
    fun registerUser(
        @Body body: String
    ): Call<RegisterResponse>

    // fungsi untuk mengirimkan data saat login
    @POST("api/login")
    @Headers("Content-Type: application/json")
    fun loginUser(
        @Body body: String,
    ): Call<LoginResponse>

    // fungsi untuk upload foto
    @Multipart
    @POST("upload")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<UploadResponse>


    // fungsi untuk mendapatkan prediksi
    @GET("predict")
    fun getPrediction(): Call<ResultResponse>
}
