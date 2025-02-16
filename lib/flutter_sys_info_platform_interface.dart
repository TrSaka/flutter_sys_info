import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_sys_info_method_channel.dart';

abstract class FlutterSysInfoPlatform extends PlatformInterface {
  FlutterSysInfoPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterSysInfoPlatform _instance = MethodChannelFlutterSysInfo();

  static FlutterSysInfoPlatform get instance => _instance;

  static set instance(FlutterSysInfoPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<int?> getBatteryLevel() {
    throw UnimplementedError('getBatteryLevel() has not been implemented.');
  }

  Future<String?> getDeviceModel() {
    throw UnimplementedError('getDeviceModel() has not been implemented.');
  }

  Future<int?> getSdkVersion() {
    throw UnimplementedError('getSdkVersion() has not been implemented.');
  }

  Future<int?> getTotalMemory() {
    throw UnimplementedError('getTotalMemory() has not been implemented.');
  }

  Future<Map<Object?, Object?>?> getStorageInfo() {
    throw UnimplementedError('getStorageInfo() has not been implemented.');
  }
}

abstract class FlutterSysInfoNetworkPlatform extends PlatformInterface {
  FlutterSysInfoNetworkPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterSysInfoNetworkPlatform _instance =
      MethodChannelFlutterSysInfoNetwork();

  static FlutterSysInfoNetworkPlatform get instance => _instance;

  static set instance(FlutterSysInfoNetworkPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getWifiSSID() {
    throw UnimplementedError('getWifiSSID() has not been implemented.');
  }

  Future<String?> getWifiBSSID() {
    throw UnimplementedError('getWifiBSSID() has not been implemented.');
  }

  Future<int?> getWifiNetworkSpeed() {
    throw UnimplementedError('getWifiNetworkSpeed() has not been implemented.');
  }

  Future<int?> getWifiIP() {
    throw UnimplementedError('getWifiIP() has not been implemented.');
  }

  Future<int?> getWifiRSSI() {
    throw UnimplementedError('getWifiRSSI() has not been implemented.');
  }
}
