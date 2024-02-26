package com.n0stalgiaultra.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build


fun isDeviceConnected(context: Context): Boolean{
    val wifiMgr = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    if (wifiMgr.isWifiEnabled) { // Wi-Fi adapter is ON
        val wifiInfo = wifiMgr.connectionInfo

        if (wifiInfo.networkId == -1) {
            return false // Not connected to an access point
        }
        return true // Connected to an access point
    } else {
        return false // Wi-Fi adapter is OFF
    }
}