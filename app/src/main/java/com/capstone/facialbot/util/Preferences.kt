import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    private val login = "login"
    private val myPref = "myPref"
    private val myToken = "userToken"
    private val userId = "userId"
    private val accessToken = "accessToken"
    private val myPreferences: SharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE)

    // Fungsi untuk menyimpan data login
    fun setStatusLogin(status: Boolean) {
        myPreferences.edit().putBoolean(login, status).apply()
    }

    // Fungsi untuk mendapatkan data login
    fun getStatusLogin(): Boolean {
        return myPreferences.getBoolean(login, false)
    }

    // Fungsi untuk menyimpan data token
    fun saveUserToken(token: String) {
        myPreferences.edit().putString(myToken, token).apply()
    }

    // Fungsi untuk mendapatkan data token
    fun getUserToken(): String? {
        return myPreferences.getString(myToken, "")
    }

    // Fungsi untuk menghapus data token
    fun clearUserToken() {
        myPreferences.edit().remove(myToken).apply()
    }

    // Fungsi untuk menghapus data login
    fun clearUserLogin() {
        myPreferences.edit().remove(login).apply()
    }

    // Fungsi untuk menyimpan data user
    fun saveUserData(userId: String, accessToken: String) {
        myPreferences.edit()
            .putString(this.userId, userId)
            .putString(this.accessToken, accessToken)
            .apply()
    }

    // Fungsi untuk menghapus semua data user
    fun clearUserData() {
        myPreferences.edit()
            .remove(this.userId)
            .remove(this.accessToken)
            .apply()
    }

    // Fungsi untuk mendapatkan data user id
    fun getUserId(): String? {
        return myPreferences.getString(userId, "")
    }

    // Fungsi untuk mendapatkan data access token
    fun getAccessToken(): String? {
        return myPreferences.getString(accessToken, "")
    }
}
