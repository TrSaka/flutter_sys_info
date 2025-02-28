package com.example.flutter_sys_info

import io.flutter.plugin.common.EventChannel

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager

import android.os.BatteryManager
import android.os.Handler
import android.os.Looper

import android.content.Context
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter


class SysInfoEventHandler(private val context: Context) : EventChannel.StreamHandler {

    private var batteryEventSink: EventChannel.EventSink? = null
    // Boolean flag for checking is Battery Event Listening
    // this flag is used to prevent multiple registration & unregistration of the same event
    private var isBatteryEventListening: Boolean = false
    
    private var batteryTempEventSink: EventChannel.EventSink? = null
    // Boolean flag for checking is Battery Temp Event Listening
    // this flag is used to prevent multiple registration & unregistration of the same event
    private var isBatteryTempEventListening: Boolean = false
    
    private var wifiRssiEventSink: EventChannel.EventSink? = null
    // Boolean flag for checking is Wifi RSSI Event Listening
    // this flag is used to prevent multiple registration & unregistration of the same event
    private var isWifiRssiEventListening: Boolean = false

    private var internetConnectionEventSink: EventChannel.EventSink? = null
    // Boolean flag for checking is Internet Connection Event Listening
    // this flag is used to prevent multiple registration & unregistration of the same event
    private var isInternetAvailableEventListening: Boolean = false
    // Boolean flag for checking is Internet Connection Available
    private var isInternetAvailable: Boolean = false

    //private function to check internet connection with ConnectivityManager package from android
    private fun checkInternetConnection() {
        //instance of ConnectivityManager
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //get active network info
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        //check if active network is connected or connecting thens return true
        isInternetAvailable = activeNetwork?.isConnectedOrConnecting == true
    }


    private val internetAvailableReceiver = object : BroadcastReceiver() {
        //if ConnectivityManager.CONNECTIVITY_ACTIONintent was triggered then onReceive function will be called
        override fun onReceive(context: Context, intent: Intent?) {
            //call the method to check internet connection
            checkInternetConnection()
            
            //send the result to the flutter side
            internetConnectionEventSink?.success(isInternetAvailable)
        }
    }

    private val wifiRssiStatusReceiver = object : BroadcastReceiver() {
        //if WifiManager.RSSI_CHANGED_ACTION intent was triggered then onReceive function will be called
        override fun onReceive(context: Context, intent: Intent?) {
            //instance of WifiManager
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            //get the wifi info
            val wifiInfo = wifiManager.connectionInfo
            //send the rssi result to the flutter side
            wifiRssiEventSink?.success(wifiInfo?.rssi)
        }
    }

    private val batteryStatusReceiver = object : BroadcastReceiver() {
        //if Intent.ACTION_BATTERY_CHANGED intent was triggered then onReceive function will be called
        override fun onReceive(context: Context, intent: Intent?) {
            //get the battery level
            val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            //send the battery level to the flutter side
            batteryEventSink?.success(batteryLevel)
        }
    }

    private val batteryTempReceiver = object : BroadcastReceiver() {
        //if Intent.ACTION_BATTERY_CHANGED intent was triggered then onReceive function will be called
        override fun onReceive(context: Context, intent: Intent?) {
            //get the battery temperature
            val batteryTemp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
            //if batteryTemp is not null or not euqal to -1 then send the battery temperature to flutter side
            //-1 means the battery temperature is unavailable or not found
            //otherwise send error message to flutter side
            if (batteryTemp != null && batteryTemp != -1) {
                batteryTempEventSink?.success(batteryTemp / 10.0)
            } else {
                batteryTempEventSink?.error("UNAVAILABLE", "Battery temperature is unavailable", null)
            }
        }
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        //TODO:
        //this print statement is used to check the argument for debugging
        //in production version need to remove this line for better logging experience
        val argumentString = arguments as? String
        println("Received argument: $argumentString")

        when (arguments as? String) {
            "battery_level_stream" -> {
                //is battery event alredy listening then escape
                if (isBatteryEventListening) return
                //change boolean flag
                isBatteryEventListening = true
                batteryEventSink = events
                //register the receiver with Intent.ACTION_BATTERY_CHANGED intent
                val batteryIntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                context.registerReceiver(batteryStatusReceiver, batteryIntentFilter)
            }

            "wifi_rssi_stream" -> {
                //is wifi rssi event alredy listening then escape
                if (isWifiRssiEventListening) return
                //change boolean flag
                isWifiRssiEventListening = true
                wifiRssiEventSink = events
                //register the receiver with WifiManager.RSSI_CHANGED_ACTION intent
                val wifiRssiIntentFilter = IntentFilter(WifiManager.RSSI_CHANGED_ACTION)
                context.registerReceiver(wifiRssiStatusReceiver, wifiRssiIntentFilter)
            }

            "internet_connection_stream" -> {
                //is internet connection event alredy listening then escape
                if (isInternetAvailableEventListening) return
                //change boolean flag
                isInternetAvailableEventListening = true
                internetConnectionEventSink = events
                //register the receiver with ConnectivityManager.CONNECTIVITY_ACTION intent
                val wifiAvailableIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                context.registerReceiver(internetAvailableReceiver, wifiAvailableIntentFilter)
            }

            "battery_temp_stream" -> {
                //is battery temp event alredy listening then escape
                if (isBatteryTempEventListening) return
                //change boolean flag
                isBatteryTempEventListening = true
                batteryTempEventSink = events
                //register the receiver with Intent.ACTION_BATTERY_CHANGED intent
                val batteryTempIntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                context.registerReceiver(batteryTempReceiver, batteryTempIntentFilter)
            }

            else -> {
                //if the argument is not found then send error message to flutter side
                events?.error("INVALID_ARGUMENT", "Not Found", null)
            }
        }
    }

    override fun onCancel(arguments: Any?) {
        //TODO:
        //remove this line too :)
        val argumentString = arguments as? String
        println("Received argument to Cancel: $argumentString")

        when (arguments as? String) {
            "battery_level_stream" -> {
                //if battery event is not listening then escape
                if (!isBatteryEventListening) return
                //change boolean flag
                isBatteryEventListening = false
                batteryEventSink = null
                //unregister the receiver
                context.unregisterReceiver(batteryStatusReceiver)
            }
            "wifi_rssi_stream" -> {
                //if wifi rssi event is not listening then escape
                if (!isWifiRssiEventListening) return
                //change boolean flag
                isWifiRssiEventListening = false
                wifiRssiEventSink = null
                //unregister the receiver
                context.unregisterReceiver(wifiRssiStatusReceiver)
            }
            "internet_connection_stream" -> {
                //if internet connection event is not listening then escape
                if(!isInternetAvailableEventListening) return
                //change boolean flag
                internetConnectionEventSink = null
                isInternetAvailableEventListening = false
                //unregister the receiver
                context.unregisterReceiver(internetAvailableReceiver)
            }
            "battery_temp_stream" -> {
                //if battery temp event is not listening then escape
                if (!isBatteryTempEventListening) return
                //change boolean flag
                isBatteryTempEventListening = false
                batteryTempEventSink = null
                //unregister the receiver
                context.unregisterReceiver(batteryTempReceiver)
            }
        }
    }
}


