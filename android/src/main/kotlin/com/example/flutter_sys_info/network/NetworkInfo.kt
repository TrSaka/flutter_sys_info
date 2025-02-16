package com.example.flutter_sys_info

import android.content.Context
import io.flutter.plugin.common.MethodChannel.Result
import android.net.wifi.WifiManager


object NetworkInfo{

    private var wifiManager: WifiManager? = null
    private var wifiInfo: android.net.wifi.WifiInfo? = null

    private fun initWifiManager(context: Context){
        if(wifiManager == null){
            wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiInfo = wifiManager?.connectionInfo
        }
    }

    fun getWifiSSID(context: Context,result: Result){
        initWifiManager(context)
        result.success(wifiInfo?.ssid)
    }

    fun getWifiBSSID(context: Context,result: Result){
        initWifiManager(context)
        result.success(wifiInfo?.bssid)
    }

    fun getWifiNetworkSpeed(context: Context,result: Result){
        initWifiManager(context)
        result.success(wifiInfo?.linkSpeed)
    }
    fun getWifiRSSI(context: Context,result: Result){
        initWifiManager(context)
        result.success(wifiInfo?.rssi)
    }
    fun getWifiIP(context: Context,result: Result){
        initWifiManager(context)
        result.success(wifiInfo?.ipAddress)
    }
    
}