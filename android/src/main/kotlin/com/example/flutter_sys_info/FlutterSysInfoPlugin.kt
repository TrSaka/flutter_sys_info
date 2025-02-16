package com.example.flutter_sys_info

import androidx.annotation.NonNull
import android.content.Context



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

      //HARDWARE

      "getPlatformVersion" -> HardwareInfo.getPlatformVersion(result)
    
      "getBatteryLevel" -> HardwareInfo.getBatteryLevel(context, result)

      "getDeviceModel"-> HardwareInfo.getDeviceModel(result)

      "getSdkVersion"-> HardwareInfo.getSdkVersion(result)

      "getTotalMemory"-> HardwareInfo.getTotalMemory(context, result)

      "getStorageInfo"-> HardwareInfo.getStorageInfo(context, result)

      "getBatteryTemperature"-> HardwareInfo.getBatteryTemperature(context,result)


      //NETWORK

      "getWifiSSID"-> NetworkInfo.getWifiSSID(context, result)
      
      "getWifiBSSID"-> NetworkInfo.getWifiBSSID(context, result)
      
      "getWifiNetworkSpeed"-> NetworkInfo.getWifiNetworkSpeed(context, result)
      
      "getWifiIP"-> NetworkInfo.getWifiIP(context, result)
      
      "getWifiRSSI"-> NetworkInfo.getWifiRSSI(context, result)
      

      else -> {
        result.notImplemented()
      }
    }
  }


  

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
