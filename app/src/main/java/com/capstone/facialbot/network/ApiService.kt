package com.capstone.facialbot.network


import com.capstone.facialbot.network.responses.LoginResponse
import com.capstone.facialbot.network.responses.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    // fungsi untuk mengirimkan data saat register
    @POST("/register")
    @FormUrlEncoded
    fun registerUser(
        @Header("Authorization") Bearer: String,
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    // fungsi untuk mengirimkan data saat login
    @POST("/login")
    @FormUrlEncoded
    fun loginUser(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}