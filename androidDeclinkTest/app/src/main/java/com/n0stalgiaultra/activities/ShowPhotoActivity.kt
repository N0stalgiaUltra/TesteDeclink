package com.n0stalgiaultra.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.n0stalgiaultra.androidtest.databinding.ActivityShowPhotoBinding
import com.n0stalgiaultra.database.exportDatabase
import com.n0stalgiaultra.domain.model.PhotoModel
import com.n0stalgiaultra.utils.addWatermark
import com.n0stalgiaultra.utils.adjustBitmap
import com.n0stalgiaultra.utils.getLastLocation
import com.n0stalgiaultra.utils.isDeviceConnected
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val uri = Uri.parse(intent.extras?.getString("uri"))
        val camera = intent.extras?.getString("camera").toString()
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

        val adjustedBitmap = adjustBitmap(bitmap)//ajusta tamanho e escalonamento
        val finalBitmap = addWatermark(adjustedBitmap) // adiciona watermark

        binding.cameraImageView.setImageBitmap(
            finalBitmap
        )


        binding.buttonNewPhoto.setOnClickListener {
            finish()
        }
        if(finalBitmap!=null) {
            CoroutineScope(Dispatchers.IO).launch {
                val photoModel = getInfo(finalBitmap, camera)
                sendPhotoViewModel.localPhotoModel = photoModel
                savePhotoViewModel.insertPhotoData(photoModel)
            }
        }
        binding.buttonExportDatabase.setOnClickListener {
            val message = exportDatabase(applicationContext)
            Toast.makeText(
                this,
                message.second,
                Toast.LENGTH_SHORT
            ).show()
            Log.d("Database Export", message.second)
        }
        binding.buttonSendDetails.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(isDeviceConnected(applicationContext))
                    sendPhotoViewModel.getAllPhotoData()
                else
                    Toast.makeText(applicationContext,
                        "Dispositivo não está conectado à internet",
                        Toast.LENGTH_SHORT).show()
            }

            sendPhotoViewModel.sendDataResult.observe(this){
                    result ->
                if(result != null) {
                    if (result.isSuccess)
                        Toast.makeText(this,
                            "Envio efetuado com sucesso!",
                            Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this,
                            "Erro ao enviar, tente novamente!",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getInfo(bitmap: Bitmap, cameraSelector: String) : PhotoModel{
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
        val (latitude, longitude) = getLastLocation(this, fusedLocationClient)
        Log.d("INFO", "Latitude: $latitude, Longitude: $longitude")

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