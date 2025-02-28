package com.example.flutter_sys_info

import android.content.Context
import android.os.BatteryManager

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager

import android.os.Handler
import android.os.Looper


import io.flutter.plugin.common.EventChannel
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter


class SysInfoEventHandler(private val context: Context) : EventChannel.StreamHandler {
    private var batteryEventSink: EventChannel.EventSink? = null
    private var isBatteryEventListening: Boolean = false
    
    private var batteryTempEventSink: EventChannel.EventSink? = null
    private var isBatteryTempEventListening: Boolean = false
    
    private var wifiRssiEventSink: EventChannel.EventSink? = null
    private var isWifiRssiEventListening: Boolean = false

    private var internetConnectionEventSink: EventChannel.EventSink? = null
    private var isInternetAvailableEventListening: Boolean = false
    private var isInternetAvailable: Boolean = false

    private fun checkInternetConnection() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        isInternetAvailable = activeNetwork?.isConnectedOrConnecting == true
    }


    private val internetAvailableReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            // İnternet bağlantısını kontrol et
            checkInternetConnection()
            
            // İnternet durumu güncellenmiş olarak EventSink'e gönder
            internetConnectionEventSink?.success(isInternetAvailable)
        }
    }

    private val wifiRssiStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            wifiRssiEventSink?.success(wifiInfo?.rssi)
        }
    }

    private val batteryStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            batteryEventSink?.success(batteryLevel)
        }
    }

    private val batteryTempReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val batteryTemp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
            if (batteryTemp != null && batteryTemp != -1) {
                batteryTempEventSink?.success(batteryTemp / 10.0)
            } else {
                batteryTempEventSink?.error("UNAVAILABLE", "Battery temperature is unavailable", null)
            }
        }
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        val argumentString = arguments as? String
        println("Received argument: $argumentString")

        when (arguments as? String) {
            "battery_level_stream" -> {
                if (isBatteryEventListening) return
                isBatteryEventListening = true
                batteryEventSink = events
                val batteryIntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                context.registerReceiver(batteryStatusReceiver, batteryIntentFilter)
            }

            "wifi_rssi_stream" -> {
                if (isWifiRssiEventListening) return
                isWifiRssiEventListening = true
                wifiRssiEventSink = events
                val wifiRssiIntentFilter = IntentFilter(WifiManager.RSSI_CHANGED_ACTION)
                context.registerReceiver(wifiRssiStatusReceiver, wifiRssiIntentFilter)
            }

            "internet_connection_stream" -> {
                if (isInternetAvailableEventListening) return
                internetConnectionEventSink = events

                val wifiAvailableIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                context.registerReceiver(internetAvailableReceiver, wifiAvailableIntentFilter)
                isInternetAvailableEventListening = true
            }

            "battery_temp_stream" -> {
                if (isBatteryTempEventListening) return
                isBatteryTempEventListening = true
                batteryTempEventSink = events
                val batteryTempIntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                context.registerReceiver(batteryTempReceiver, batteryTempIntentFilter)
            }

            else -> {
                events?.error("INVALID_ARGUMENT", "Not Found", null)
            }
        }
    }

    override fun onCancel(arguments: Any?) {
        val argumentString = arguments as? String
        println("Received argument to Cancel: $argumentString")

        when (arguments as? String) {
            "battery_level_stream" -> {
                if (!isBatteryEventListening) return
                isBatteryEventListening = false
                batteryEventSink = null
                context.unregisterReceiver(batteryStatusReceiver)
            }
            "wifi_rssi_stream" -> {
                if (!isWifiRssiEventListening) return
                isWifiRssiEventListening = false
                wifiRssiEventSink = null
                context.unregisterReceiver(wifiRssiStatusReceiver)
            }
            "internet_connection_stream" -> {
                if (isInternetAvailableEventListening) {
                    internetConnectionEventSink = null
                    isInternetAvailableEventListening = false
                    context.unregisterReceiver(internetAvailableReceiver)

                }
            }
            "battery_temp_stream" -> {
                if (!isBatteryTempEventListening) return
                isBatteryTempEventListening = false
                batteryTempEventSink = null
                context.unregisterReceiver(batteryTempReceiver)
            }
        }
    }
}


