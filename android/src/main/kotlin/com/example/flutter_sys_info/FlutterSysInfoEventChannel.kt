package com.example.flutter_sys_info

import android.content.Context
import android.os.BatteryManager

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager


import io.flutter.plugin.common.EventChannel
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter


class SysInfoEventHandler(private val context:Context):EventChannel.StreamHandler{
    private var batteryEventSink: EventChannel.EventSink? = null
    private var wifirRssiEventSink: EventChannel.EventSink? = null
    private var wifiConnectionEventSink: EventChannel.EventSink? = null

    private var isWifiConnected : Boolean = false
    private var isInternetAvailable: Boolean = false

    private fun checkInternetConnection(){
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        isInternetAvailable = activeNetwork?.isConnectedOrConnecting == true

    }


    private val wifiConnectionReceiver = object: BroadcastReceiver(){
        override fun onReceive(context:Context, intent:Intent?){
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager?.connectionInfo
            isWifiConnected = wifiInfo?.networkId != -1

            checkInternetConnection()
            wifiConnectionEventSink?.success(isWifiConnected &&isInternetAvailable)
            
        }
    }

    private val internetAvailableReceiver = object: BroadcastReceiver(){
        override fun onReceive(context:Context, intent:Intent?){
            checkInternetConnection()
            wifiConnectionEventSink?.success(isWifiConnected &&isInternetAvailable)
        }
    }
    
    private val wifiRssiStatusReceiver = object: BroadcastReceiver(){
        override fun onReceive(context:Context, intent:Intent?){
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager?.connectionInfo
            wifirRssiEventSink?.success(wifiInfo?.rssi)
        }
    }


    val batteryStatusReceiver = object: BroadcastReceiver(){
        override fun onReceive(context:Context, intent:Intent?){
            val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
            batteryEventSink?.success(batteryLevel)
        }
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?){
        val argumentString = arguments as? String

        println("Received argument: $argumentString") 
        when (arguments as? String){
            "battery_level_stream" -> {
                batteryEventSink = events
                //intent type is ACTION_BATTERY_CHANGED
                //this intent filter is needed to invoke our boradcast receiver when battery changed
                //for more info: https://developer.android.com/reference/android/content/Intent.html#ACTION_BATTERY_CHANGED
                var batteryIntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                context.registerReceiver(batteryStatusReceiver,batteryIntentFilter)
            }

            "wifi_rssi_stream"->{
                wifirRssiEventSink = events
                //intent type is RSSI_CHANGED
                //this intent filter is needed to invoke our boradcast receiver when wifi rssi changed
                //for more info: https://developer.android.com/reference/android/net/wifi/WifiManager.html#RSSI_CHANGED_ACTION
                var wifiRssiIntentFilter = IntentFilter(WifiManager.RSSI_CHANGED_ACTION)
                context.registerReceiver(wifiRssiStatusReceiver,wifiRssiIntentFilter)   
            }
            
            "wifi_connection_stream"->{
                wifiConnectionEventSink = events
                val wifiConnectionIntentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
                context.registerReceiver(wifiConnectionReceiver,wifiConnectionIntentFilter)

                val wifiAvailableIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                context.registerReceiver(wifiConnectionReceiver,wifiAvailableIntentFilter)
            }
            
            else -> {
                events?.error("INVALID_ARGUMENT","Not Found",null)
            }
        }

    }
    override fun onCancel(arguments: Any?){

       when (arguments as? String) {
            "battery_level_stream" -> {
                batteryEventSink = null
                context.unregisterReceiver(batteryStatusReceiver)
            }
            "wifi_rssi_stream" -> {
                wifirRssiEventSink = null
                context.unregisterReceiver(wifiRssiStatusReceiver)
            }
        }
    }
}

