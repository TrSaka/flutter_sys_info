package com.example.flutter_sys_info

import androidx.annotation.NonNull
import android.content.Context
import android.os.BatteryManager
import android.os.StatFs
import android.os.Environment


import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** FlutterSysInfoPlugin */
class FlutterSysInfoPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context : Context

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_sys_info")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(call: MethodCall, result: Result) {

    when (call.method){
      "getPlatformVersion" -> {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
      }
    
      "getBatteryLevel" -> {
        val batteryLevel = getBatteryLevel()
        if(batteryLevel != -1){
          result.success(batteryLevel)
        }else{
          result.error("UNAVAILABLE", "Battery level not available.", null)
        }
      }


      "getDeviceModel"->{
        val deviceModel = android.os.Build.MODEL
        result.success(deviceModel)
      }

      "getSdkVersion"->{
        val sdkVersion = android.os.Build.VERSION.SDK_INT
        result.success(sdkVersion)
      }

      "getTotalMemory"->{
        val memoryInfo = android.app.ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        activityManager.getMemoryInfo(memoryInfo)
        result.success(memoryInfo.totalMem)
      }
      "getStorageInfo"->{
        val externalStorageDir = context.getExternalFilesDir(null)
        val status = StatFs(externalStorageDir?.absolutePath)


        val blockSize = status.blockSizeLong
        val totalBlocks = status.blockCountLong
        val availableBlocks = status.availableBlocksLong

        val totalStorage = totalBlocks * blockSize
        val availableStorage = availableBlocks * blockSize
        result.success(mapOf("total" to totalStorage, "available" to availableStorage))
      }

      "getWifiSSID"->{
        NetworkInfo.getWifiSSID(context, result)
      }
      "getWifiBSSID"->{
        NetworkInfo.getWifiBSSID(context, result)
      }
      "getWifiNetworkSpeed"->{
        NetworkInfo.getWifiNetworkSpeed(context, result)
      }
      "getWifiIP"->{
        NetworkInfo.getWifiIP(context, result)
      }
      "getWifiRSSI"->{
        NetworkInfo.getWifiRSSI(context, result)
      }

      else -> {
        result.notImplemented()
      }
    }
  }

  private fun getBatteryLevel():Int{
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
  }

  

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
