import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.ViewModel
import com.capstone.facialbot.network.ApiConfig
import com.capstone.facialbot.network.responses.LoginResponse
import com.capstone.facialbot.ui.login.LoginActivity
import com.capstone.facialbot.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private lateinit var myPreferences: Preferences

    fun init(context: Context) {
        myPreferences = Preferences(context)

        if (myPreferences.getStatusLogin()) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            finishAffinity(context as LoginActivity)
        }
    }

    fun login(context: Context, identifier: String, password: String) {

        val client = ApiConfig.getApiService().loginUser(identifier, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val body = response.body()
                if (body?.data != null && body.data.isNotEmpty()) {
                    val loginResponseItem = body.data[0]
                    val id = loginResponseItem?.id ?: ""
                    val accessToken = loginResponseItem?.accessToken ?: ""

                    myPreferences.setStatusLogin(true)
                    myPreferences.saveUserData(id, accessToken)
                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as LoginActivity).finishAffinity()
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
