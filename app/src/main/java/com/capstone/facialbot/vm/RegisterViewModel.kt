package com.capstone.facialbot.vm

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.capstone.facialbot.network.ApiConfig
import com.capstone.facialbot.network.responses.RegisterResponse
import com.capstone.facialbot.ui.activity.LoginActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    fun register(context: Context, name: String, email: String, password: String) {
        val token = "Bearer " + "JpZCI6Il9JOFJDN3U0enFONEk3OUsiLCJlbWFpbCI6Imh1c2FkYUBnbWFpbC5jb20iLCJpYXQiOjE2ODU4NzcxMTUsImV4cCI6MTY4NTg4MDcxNX0.IaZJBH1B00ZXatE060aVZVEXUwtMiZEGM06sy-U8kOM"

        try {
            val paramObject = JSONObject()
            paramObject.put("username", name)
            paramObject.put("email", email)
            paramObject.put("password", password)

            val client = ApiConfig.getApiService().registerUser(paramObject.toString())

            client.enqueue(object : Callback<RegisterResponse> {

                // Jika berhasil
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        Log.d("DATAREGIS", body.toString())
                        if (body != null) {
                            Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("NOT SUCCESS REGIS", "CEK: $response")
                    }
                }

                // Jika gagal
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
                    Log.e("ONFAILURE REGIS", t.message.toString())
                }
            })
        } catch (e: Exception) {
            Log.e("REGISTER", "GAGAL: $e")
        }

    }
}