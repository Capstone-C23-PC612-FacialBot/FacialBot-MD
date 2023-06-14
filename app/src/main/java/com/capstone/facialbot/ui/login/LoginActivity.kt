package com.capstone.facialbot.ui.login

import LoginViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.capstone.facialbot.databinding.ActivityLoginBinding
import com.capstone.facialbot.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var registerHere: TextView
    private lateinit var loginButton: AppCompatButton
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Binding
        email = binding.editTextEmail
        password = binding.editTextPassword
        loginButton = binding.loginButton

        // Inisialisasi ViewModel
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.init(this@LoginActivity)

        // Ketika tombol login diklik
        loginButton.setOnClickListener {
            loginViewModel.login(
                this@LoginActivity,
                email.text.toString(),
                password.text.toString()
            )
        }

        // Navigasi ke halaman register
        registerHere = binding.daftarDisini
        registerHere.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        setMyButtonEnable()

        // Listener untuk kolom email
        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing.
            }
        })

        // Listener untuk kolom password
        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing.
            }
        })
    }

    private fun setMyButtonEnable() {
        val emailText = email.text
        val passwordText = password.text
        loginButton.isEnabled =
            (emailText != null && emailText.isNotEmpty()) && (passwordText != null && passwordText.isNotEmpty())
    }

    // Override metode onResume untuk memeriksa status login saat activity dilanjutkan
    override fun onResume() {
        super.onResume()
        loginViewModel.init(this@LoginActivity)
    }
}
