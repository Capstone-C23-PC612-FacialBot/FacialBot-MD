package com.capstone.facialbot.ui.detection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.facialbot.databinding.ActivityDetectionResultBinding
import com.capstone.facialbot.ui.main.MainActivity


class DetectionResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetectionResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val homeButton = binding.homeButton
        homeButton.setOnClickListener {
            val intent = Intent(this@DetectionResultActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@DetectionResultActivity, MainActivity::class.java)
        startActivity(intent)
    }
}