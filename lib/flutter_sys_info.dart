import 'package:flutter_sys_info/constants/enums/enums.dart';
import 'package:flutter_sys_info/helper/switch_memory_type.dart';
import 'flutter_sys_info_platform_interface.dart';

export 'constants/enums/enums.dart';

class FlutterSysInfoNetwork {
  Future<String?> getWifiSSID() {
    return FlutterSysInfoNetworkPlatform.instance.getWifiSSID();
  }

  Future<String?> getWifiBSSID() {
    return FlutterSysInfoNetworkPlatform.instance.getWifiBSSID();
  }

  Future<int?> getWifiNetworkSpeed() {
    return FlutterSysInfoNetworkPlatform.instance.getWifiNetworkSpeed();
  }

  Future<int?> getWifiIP() {
    return FlutterSysInfoNetworkPlatform.instance.getWifiIP();
  }

  Future<int?> getWifiRSSI() {
    return FlutterSysInfoNetworkPlatform.instance.getWifiRSSI();
  }
}

class FlutterSysInfo {

  Future<String?> getBatteryTemperature() {
    return FlutterSysInfoPlatform.instance.getBatteryTemperature();
  }



  Future<String?> getPlatformVersion() {
    return FlutterSysInfoPlatform.instance.getPlatformVersion();
  }

  Future<int?> getBatteryLevel() {
    return FlutterSysInfoPlatform.instance.getBatteryLevel();
  }

  Future<String?> getDeviceModel() {
    return FlutterSysInfoPlatform.instance.getDeviceModel();
  }

  Future<int?> getSdkVersion() {
    return FlutterSysInfoPlatform.instance.getSdkVersion();
  }

  Future<int?> getTotalMemory({required MemoryUnit memoryUnit}) async {
    final int? memory = await FlutterSysInfoPlatform.instance.getTotalMemory();
    if (memory == null) return null;

    return switchMemoryType(memoryUnit, memory);
  }

  Future<int?> getStorageInfo({
    required StorageInfoType storageInfoType,
    required MemoryUnit memoryUnit,
  }) async {
    Map<Object?, Object?>? data =
        await FlutterSysInfoPlatform.instance.getStorageInfo();
    Object? value;

    if (data == null) return null;

    switch (storageInfoType) {
      case StorageInfoType.TOTAL:
        value = data[StorageInfoType.TOTAL.value];
        break;

      case StorageInfoType.AVAILABLE:
        value = data[StorageInfoType.AVAILABLE.value];
        break;
    }

    return switchMemoryType(memoryUnit, int.parse(value.toString()));
  }
}
//which enum name is the best explaining the purpose of the enum?
