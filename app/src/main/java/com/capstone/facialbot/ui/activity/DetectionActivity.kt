package com.capstone.facialbot.ui.activity

import Preferences
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.facialbot.databinding.ActivityDetectionBinding
import com.capstone.facialbot.network.ApiConfig
import com.capstone.facialbot.network.ApiService
import com.capstone.facialbot.network.responses.UploadResponse
import com.capstone.facialbot.network.responses.ResultResponse
import com.capstone.facialbot.ui.main.MainActivity
import com.capstone.facialbot.util.reduceFileImage
import com.capstone.facialbot.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionBinding
    private lateinit var backButton: ImageView
    private lateinit var myPreferences: Preferences
    private var counter = 0
    private var getFile: File? = null
    private lateinit var apiService: ApiService
    private lateinit var confidence: TextView

    private fun setMyButtonEnable() {
        val isEnable = counter == 1
        binding.detectionButton.isEnabled = isEnable
    }

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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreferences = Preferences(this)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        backButton = binding.backButton
        val galleryButton = binding.galleryButton
        val camera = binding.cameraButton
        confidence = binding.confidence

        apiService = ApiConfig.getApiService()

        backButton.setOnClickListener {
            val intent = Intent(this@DetectionActivity, MainActivity::class.java)
            startActivity(intent)
        }

        galleryButton.setOnClickListener {
            openGallery()
            counter = 1
            setMyButtonEnable()
        }

        camera.setOnClickListener {
            openCamera()
            counter = 1
            setMyButtonEnable()
        }

        val detectButton = binding.detectionButton
        detectButton.setOnClickListener {
            if (getFile != null) {
                uploadImage(getFile)
            } else {
                Toast.makeText(this, "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

        setMyButtonEnable()
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile

            binding.ivResultImage.setImageURI(selectedImg)

            // Call getPrediction here
            getPrediction()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                getFile = file
                binding.ivResultImage.setImageBitmap(BitmapFactory.decodeFile(file.path))

                // Call getPrediction here
                getPrediction()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private fun uploadImage(file: File?) {
        file?.let {
            val reducedFile = reduceFileImage(file)

            val requestBody = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", reducedFile.name, requestBody)

            val call: Call<UploadResponse> = apiService.uploadImage(multipartBody)
            call.enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful) {
                        val uploadResponse = response.body()
                        val fileUrl = uploadResponse?.data?.file
                        Toast.makeText(this@DetectionActivity, "Upload berhasil! URL file: $fileUrl", Toast.LENGTH_SHORT).show()
                        // Lakukan tindakan lain dengan URL file

                        // Call getPrediction here
                        getPrediction()
                    } else {
                        Toast.makeText(this@DetectionActivity, "Upload gagal. Kode error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(this@DetectionActivity, "Upload gagal. Pesan error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getPrediction() {
        val apiService = ApiConfig.getApiPredict() // Menggunakan getApiPredict() dari ApiConfig
        val call: Call<ResultResponse> = apiService.getPrediction()
        call.enqueue(object : Callback<ResultResponse> {
            override fun onResponse(call: Call<ResultResponse>, response: Response<ResultResponse>) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    val confidenceValue = resultResponse?.prediction

                    // Tampilkan hasil prediksi di confidence TextView
                    confidence.text = "Potensi Penyakit: $confidenceValue"
                } else {
                    Toast.makeText(this@DetectionActivity, "Gagal mendapatkan prediksi. Kode error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                Toast.makeText(this@DetectionActivity, "Gagal mendapatkan prediksi. Pesan error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
