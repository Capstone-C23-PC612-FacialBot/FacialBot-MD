package com.capstone.facialbot.network


import com.capstone.facialbot.network.responses.LoginResponse
import com.capstone.facialbot.network.responses.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    // fungsi untuk mengirimkan data saat register
    @POST("register")
    @Headers("Content-Type: application/json")
    fun registerUser(
        @Body body: String
    ): Call<RegisterResponse>

    // fungsi untuk mengirimkan data saat login
    @POST("login")
    @Headers("Content-Type: application/json")
    fun loginUser(
        @Body body: String,
    ): Call<LoginResponse>
}