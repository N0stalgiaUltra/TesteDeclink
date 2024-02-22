package com.n0stalgiaultra.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXThreads.TAG
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.n0stalgiaultra.androidtest.R
import com.n0stalgiaultra.androidtest.databinding.ActivityPhotoBinding
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.jar.Manifest

class PhotoActivity : AppCompatActivity() {

    private var imageCapture : ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //diretorio de saida
        outputDirectory = getOutputDirectory()

        //inicia executor da camera
        cameraExecutor = Executors.newSingleThreadExecutor()

        if(allPermissionsGranted())
            startCamera()
        else{
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onStart() {
        super.onStart()
        binding.cameraButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraPreviewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try{
                cameraProvider.unbindAll()

                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            }catch (e: Exception){
                Log.e("Camera", "Erro ao iniciar camera", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(){
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            "foto_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Foto capturada: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d("Camera", msg)

                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    val intent = Intent(this@PhotoActivity, ShowPhotoActivity::class.java)
                    intent.putExtra("uri", savedUri.toString())
                    startActivity(intent)

                    //Salva o bitmap no disco e depois carrega na prox activity
//                    try{
//                        val bitmapFilename = "bitmap.jpeg"
//                        val stream : FileOutputStream = openFileOutput(bitmapFilename, Context.MODE_PRIVATE)
//
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//
//                        stream.close()
//                        bitmap.recycle()
//                        val intent = Intent(this@PhotoActivity, ShowPhotoActivity::class.java)
//                        intent.putExtra("photoBitmap", bitmapFilename)
//                        startActivity(intent)
//
//                    }catch (e: Exception){
//                        e.printStackTrace()
//                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Camera", "Erro ao capturar a foto: ${exception.message}", exception)
                }
            }
        )
    }

    private fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if(mediaDir!=null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object{
        private const val TAG = "CameraX"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

}