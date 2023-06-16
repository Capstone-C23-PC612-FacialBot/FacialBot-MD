package com.capstone.facialbot.ui.main

import MainViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.capstone.facialbot.databinding.ActivityMainBinding
import com.capstone.facialbot.ui.activity.DetectionActivity
import com.capstone.facialbot.ui.activity.ProfileActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var helloName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helloName = binding.helloUser

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.init(this)

        viewModel.name.observe(this) { name ->
             helloName.text = "Halo, $name!"
        }

        // Set button listener
        val profileButton = binding.rectangleProfile
        profileButton.setOnClickListener{
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }



//        val historyButton = binding.lihatLebih
//        historyButton.setOnClickListener{
//            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
//            startActivity(intent)
//        }

        val detectionButton = binding.fab
        detectionButton.setOnClickListener{
            val intent = Intent(this@MainActivity, DetectionActivity::class.java)
            startActivity(intent)
        }
    }

    // Exit app when back button pressed
    override fun onBackPressed() {
        finishAffinity()
    }
}