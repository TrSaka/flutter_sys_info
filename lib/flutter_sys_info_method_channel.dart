import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_sys_info_platform_interface.dart';

class MethodChannelFlutterSysInfo extends FlutterSysInfoPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_sys_info');


  @override
  Future<String?> getBatteryTemperature() async {
    final version =
        await methodChannel.invokeMethod<int>('getBatteryTemperature');
    return version.toString();
  }

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<int?> getBatteryLevel() async {
    //ONLY ANDROID
    final batteryLevel =
        await methodChannel.invokeMethod<int?>('getBatteryLevel');
    return batteryLevel;
  }

  @override
  Future<String?> getDeviceModel() async {
    final deviceModel =
        await methodChannel.invokeMethod<String>('getDeviceModel');
    return deviceModel;
  }

  @override
  Future<int?> getSdkVersion() async {
    int? sdkVersion = await methodChannel.invokeMethod<int>('getSdkVersion');
    return sdkVersion;
  }

  @override
  Future<int?> getTotalMemory() async {
    final totalMemory = await methodChannel.invokeMethod<int>('getTotalMemory');
    return totalMemory;
  }

  @override
  Future<Map<Object?, Object?>?> getStorageInfo() async {
    final storageInfo = await methodChannel
        .invokeMethod<Map<Object?, Object?>>('getStorageInfo');
    return storageInfo;
  }
}

class MethodChannelFlutterSysInfoNetwork extends FlutterSysInfoNetworkPlatform {
  @visibleForTesting
  final MethodChannel methodChannel = const MethodChannel('flutter_sys_info');

  @override
  Future<String?> getWifiSSID() async {
    final wifiBSSID = await methodChannel.invokeMethod<String>('getWifiSSID');
    return wifiBSSID;
  }

  @override
  Future<String?> getWifiBSSID() async {
    final wifiBSSID = await methodChannel.invokeMethod<String>('getWifiBSSID');
    return wifiBSSID;
  }

  @override
  Future<int?> getWifiNetworkSpeed() async {
    final wifiNetworkSpeed =
        await methodChannel.invokeMethod<int>('getWifiNetworkSpeed');
    return wifiNetworkSpeed;
  }

  @override
  Future<int?> getWifiIP() async {
    final wifiIP = await methodChannel.invokeMethod<int>('getWifiIP');
    return wifiIP;
  }

  @override
  Future<int?> getWifiRSSI() async {
    final wifiRSSI = await methodChannel.invokeMethod<int>('getWifiRSSI');
    return wifiRSSI;
  }
}
