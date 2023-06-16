 package com.capstone.facialbot.ui.activity

import Preferences
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.capstone.facialbot.databinding.ActivityProfileBinding
import com.capstone.facialbot.ui.main.MainActivity
import com.capstone.facialbot.vm.ProfileViewModel


 class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var backButton: ImageView
    private lateinit var logout: View
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var myPreferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreferences = Preferences(this)
        userName = binding.tvNama
        userEmail = binding.tvEmail
        logout = binding.lihatLebihKeluar

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.init(this)

        viewModel.name.observe(this) { name ->
            userName.text = name
        }

        viewModel.email.observe(this) { email ->
            userEmail.text = email
        }

        // Tombol kembali di klik
        backButton = binding.backButton
        backButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            startActivity(intent)
        }



        // Launcher untuk pindah ke activity help centre
        val helpCentreLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel.init(this)
            }
        }







        logout.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            with(alertDialogBuilder) {
                setTitle("Keluar")
                setMessage("Apakah anda yakin ingin keluar?")
                setPositiveButton("Ya") { dialog, which ->
                    myPreferences.clearUserToken()
                    myPreferences.clearUserLogin()
                    myPreferences.clearUserData()
                    myPreferences.setStatusLogin(false)
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                setNegativeButton("Tidak") { dialog, which ->
                    dialog.cancel()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }
}