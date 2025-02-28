package com.example.flutter_sys_info

import android.content.Context
import android.content.Intent
import android.content.IntentFilter


import io.flutter.plugin.common.MethodChannel.Result
import java.io.File

import android.os.HardwarePropertiesManager
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs
import android.os.Build

object HardwareInfo{
    //BatteryManager instance for multiple use
    private var batteryManager: BatteryManager? = null
    

    private fun initBatteryManager(context: Context){
        //initialize batteryManager if it is null
        if(batteryManager == null){
            batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        }
    }

    //getPlatformVersion method to get the platform version
    fun getPlatformVersion(result: Result){
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }
    
    //getBatteryLevel method to get the battery level from the device
    //this method is configurated for method channel
    //That means it will return the battery level as a result by calling the method for each time
    //If you want to track the battery level, please inspect flutter_sys_info/FlutterSysInfoEventChannel.kt

    fun getBatteryLevel(context: Context,result: Result){
        initBatteryManager(context)
        val batteryLevel = batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        result.success(batteryLevel)
    }

    //getDeviceModel method to get the device model number from the device
    fun getDeviceModel(result: Result){
        val deviceModel = android.os.Build.MODEL
        result.success(deviceModel)
    }

    //getSdkVersion method to get the sdk version from the device
    fun getSdkVersion(result: Result){
        val sdkVersion = android.os.Build.VERSION.SDK_INT
        result.success(sdkVersion)
    }

    //getTotalMemory method to get the total memory from the device in bytes
    fun getTotalMemory(context: Context,result: Result){
        val memoryInfo = android.app.ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        activityManager.getMemoryInfo(memoryInfo)
        result.success(memoryInfo.totalMem)
    }

    //getStorageInfo method to get the total and available storage from the device in bytes
    //There is two retun type in single map.
    //total: total storage in bytes
    //available: available storage in bytes
    fun getStorageInfo(context: Context,result:Result){
        val externalStorageDir = context.getExternalFilesDir(null)
        val status = StatFs(externalStorageDir?.absolutePath)

        val blockSize = status.blockSizeLong
        val totalBlocks = status.blockCountLong
        val availableBlocks = status.availableBlocksLong

        val totalStorage = totalBlocks * blockSize
        val availableStorage = availableBlocks * blockSize
        result.success(mapOf("total" to totalStorage, "available" to availableStorage))
      
    }

    //getBatteryTemperature method to get the battery temperature from the device
    //This method is configurated for method channel
    //That means it will return the battery temperature as a result by calling the method for each time
    //If you want to track the battery temperature, please inspect flutter_sys_info/FlutterSysInfoEventChannel.kt
    fun getBatteryTemperature(context: Context, result: Result) {
        val batteryStatus: Intent? = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val temperature = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)

        if (temperature != null && temperature != -1) {
            result.success(temperature / 10.0) // temperature is in tenths of degree Celsius
        } else {
            result.success(null) // Return null if temperature is not available
        }
    }
}