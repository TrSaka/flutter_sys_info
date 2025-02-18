package com.example.flutter_sys_info

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result

import android.content.Context

class SysInfoMethodHandler(private val context: Context){
    fun handleMethodCall(call:MethodCall, result:Result){
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
}