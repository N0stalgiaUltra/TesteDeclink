package com.n0stalgiaultra.activities

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.n0stalgiaultra.androidtest.R
import com.n0stalgiaultra.androidtest.databinding.ActivityShowPhotoBinding

class ShowPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val photoUriString = intent.getStringExtra("photoUri")
        val photoUri = Uri.parse(photoUriString)
        binding.cameraImageView.setImageURI(
            photoUri
        )

        binding.buttonNewPhoto.setOnClickListener {
            finish()
        }

    }


}