package com.capstone.facialbot.ui.detection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.facialbot.databinding.ActivityDetectionBinding
import com.capstone.facialbot.ui.main.MainActivity
import com.capstone.facialbot.util.uriToFile


import java.io.File

class DetectionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetectionBinding
    private lateinit var backButton : ImageView
    private var counter = 0
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    // fungsi untuk menangani hasil dari request permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan izin!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // fungsi untuk memeriksa apakah semua permission sudah diberikan
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memeriksa apakah semua permission sudah diberikan
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        backButton = binding.backButton
        val galleryButton = binding.galleryButton
        val camera = binding.cameraButton

        // Tombol kembali di klik
        backButton.setOnClickListener {
            val intent = Intent(this@DetectionActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // Tombol galeri di klik
        galleryButton.setOnClickListener {
            openGallery()
            counter = 1
            setMyButtonEnable()
        }

        // Tombol kamera di klik
        camera.setOnClickListener {
            openCamera()
            counter = 1
            setMyButtonEnable()
        }

        // Tombol deteksi di klik
        val detectButton = binding.detectionButton
        detectButton.setOnClickListener {
            val intent = Intent(this@DetectionActivity, DetectionResultActivity::class.java)
            startActivity(intent)
//            detect()
        }

        setMyButtonEnable()
    }

    // fungsi untuk mengatur tombol deteksi
    private fun setMyButtonEnable() {
        val isEnable = counter == 1
        binding.detectionButton.isEnabled = isEnable
    }

    // fungsi untuk menangani hasil dari pemilihan gambar dari galeri
    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile

            binding.ivResultImage.setImageURI(selectedImg)
        }
    }

    // fungsi untuk membuka galeri dan memilih gambar
    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    // fungsi untuk menangani hasil dari pemanggilan kamera
    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT ) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
//                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivResultImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    // companion object untuk menyimpan properti dan konstanta yang digunakan di activity ini
    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}