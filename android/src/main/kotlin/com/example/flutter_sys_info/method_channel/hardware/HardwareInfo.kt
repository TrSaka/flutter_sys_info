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

    private var batteryManager: BatteryManager? = null
    

    private fun initBatteryManager(context: Context){
        if(batteryManager == null){
            batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        }
    }


    fun getPlatformVersion(result: Result){
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }
    

    fun getBatteryLevel(context: Context,result: Result){
        initBatteryManager(context)
        val batteryLevel = batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        result.success(batteryLevel)
    }

    fun getDeviceModel(result: Result){
        val deviceModel = android.os.Build.MODEL
        result.success(deviceModel)
    }

    fun getSdkVersion(result: Result){
        val sdkVersion = android.os.Build.VERSION.SDK_INT
        result.success(sdkVersion)
    }

    fun getTotalMemory(context: Context,result: Result){
        val memoryInfo = android.app.ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        activityManager.getMemoryInfo(memoryInfo)
        result.success(memoryInfo.totalMem)
    }

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


    fun getBatteryTemperature(context: Context, result: Result) {
        val batteryStatus: Intent? = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val temperature = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)

        if (temperature != null && temperature != -1) {
            result.success(temperature / 10.0) // temperature is in tenths of degree Celsius
        } else {
            result.success(null) // Return null if temperature is not available
        }
    }


    private fun getBatteryLevel(context:Context):Int{
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
  }


}