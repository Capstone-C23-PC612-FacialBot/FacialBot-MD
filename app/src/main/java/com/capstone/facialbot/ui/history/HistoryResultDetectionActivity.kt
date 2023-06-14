package com.capstone.facialbot.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.facialbot.databinding.ActivityHistoryResultDetectionBinding


class HistoryResultDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryResultDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryResultDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}