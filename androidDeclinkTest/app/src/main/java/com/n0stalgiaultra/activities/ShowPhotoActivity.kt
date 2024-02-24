package com.n0stalgiaultra.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.n0stalgiaultra.androidtest.databinding.ActivityShowPhotoBinding
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.utils.addWatermark
import com.n0stalgiaultra.utils.adjustBitmap
import com.n0stalgiaultra.viewModel.SavePhotoDataViewModel
import com.n0stalgiaultra.viewModel.SendPhotoDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShowPhotoActivity : AppCompatActivity() {

    private val savePhotoViewModel: SavePhotoDataViewModel by viewModel()
    private val sendPhotoViewModel: SendPhotoDataViewModel by viewModel()
    private lateinit var binding: ActivityShowPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val uri = Uri.parse(intent.extras?.getString("uri"))
        val camera = intent.extras?.getString("camera")
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

        val adjustedBitmap = adjustBitmap(bitmap) //ajusta tamanho e escalonamento
        val finalBitmap = addWatermark(adjustedBitmap) // adiciona watermark

        binding.cameraImageView.setImageBitmap(
            finalBitmap
        )

        if (finalBitmap != null) {
           val photoModel = getInfo(finalBitmap, camera!!)
            sendPhotoViewModel.localPhotoModel = photoModel
            CoroutineScope(Dispatchers.IO).launch {
                savePhotoViewModel.insertPhotoData(photoModel)
            }
        }

        binding.buttonNewPhoto.setOnClickListener {
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInfo(bitmap: Bitmap, cameraSelector: String) : PhotoModel{
        // Hora atual
        val currentTime = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss",
            Locale.getDefault()).format(Date(System.currentTimeMillis()))
        Log.d("INFO", "Hora atual: $currentTime")

        // Imei ou SecureId
        val secureId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("INFO", "Secure ID: $secureId")

        // Camera
        Log.d("INFO", "Câmera $cameraSelector")

        // Latitude e Longitude
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permissão já concedida, pode obter a localização
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            latitude = lastKnownLocation?.latitude!!
            longitude = lastKnownLocation?.longitude!!
            Log.d("INFO", "Latitude: $latitude, Longitude: $longitude")
        } else {
            // Permissão ainda não foi concedida, solicite-a ao usuário
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                10
            )
        }

        // Imagem Base64
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
        Log.d("INFO", "Imagem em Base64: $base64Image")

        // Modelo
        val model = Build.MODEL
        Log.d("INFO", "Modelo do dispositivo: $model")

        // Fabricante
        val manufacturer = Build.MANUFACTURER
        Log.d("INFO", "Fabricante do dispositivo: $manufacturer")

        // Versão
        val version = Build.VERSION.RELEASE
        Log.d("INFO", "Versão do Android: $version")

        // Nível API
        val apiLvl = Build.VERSION.SDK_INT
        Log.d("INFO", "Nível da API: $apiLvl")

        // Tipo de conexão
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(networkInfo)
        val connectionType = if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Dados Móveis"
                else -> "Outro"
            }
        } else {
            "Sem Conexão"
        }
        Log.d("INFO", "Tipo de conexão: $connectionType")

        // IP
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        //Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        val ipString = InetAddress.getByAddress(
            ByteBuffer
                .allocate(Integer.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(wifiManager.connectionInfo.ipAddress)
                .array()
        ).hostAddress
        Log.d("INFO", "IP do dispositivo: $ipString")

        // Transmitido (se foi enviado para o servidor)
        val transmitted = 0
        Log.d("INFO", "Transmitido: $transmitted")

        // Percentual Bateria
        val batteryLevel = registerReceiver(
            null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))?.let {
                intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            (level * 100 / scale.toFloat()).toInt()
        } ?: -1
        Log.d("INFO", "Percentual de bateria: $batteryLevel")

         return PhotoModel(
            DATA_HORA = currentTime,
            EQUIPAMENTO = secureId,
            CAMERA = cameraSelector,
            LATITUDE = latitude,
            LONGITUDE = longitude,
            IMAGEM = base64Image,
            MODELO = model,
            FABRICANTE = manufacturer,
            VERSAO = version,
            NIVEL_API = apiLvl,
            TIPO_CONEXAO = connectionType,
            IP_EQUIPAMENTO = ipString!!,
            TRANSMITIDO = transmitted,
            PERCENTUAL_BATERIA = batteryLevel
        )

    }



}