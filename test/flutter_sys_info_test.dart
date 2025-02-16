// import 'package:flutter_test/flutter_test.dart';
// import 'package:flutter_sys_info/flutter_sys_info.dart';
// import 'package:flutter_sys_info/flutter_sys_info_platform_interface.dart';
// import 'package:flutter_sys_info/flutter_sys_info_method_channel.dart';
// import 'package:plugin_platform_interface/plugin_platform_interface.dart';

// class MockFlutterSysInfoPlatform
//     with MockPlatformInterfaceMixin
//     implements FlutterSysInfoPlatform {
//   @override
//   Future<String?> getPlatformVersion() => Future.value('42');

//   @override
//   Future<int?> getBatteryLevel() {
//     // TODO: implement getBatteryLevel
//     throw UnimplementedError();
//   }

//   @override
//   Future<String?> getDeviceModel() {
//     // TODO: implement getDeviceModel
//     throw UnimplementedError();
//   }

//   @override
//   Future<int?> getSdkVersion() {
//     // TODO: implement getSdkVersion
//     throw UnimplementedError();
//   }

//   @override
//   Future<Map<Object?, Object?>?> getStorageInfo() {
//     // TODO: implement getStorageInfo
//     throw UnimplementedError();
//   }

//   @override
//   Future<int?> getTotalMemory() {
//     // TODO: implement getTotalMemory
//     throw UnimplementedError();
//   }

//   @override
//   Future<String?> getWifiSSID() {
//     // TODO: implement getWifiSSID
//     throw UnimplementedError();
//   }
// }

// void main() {
//   final FlutterSysInfoPlatform initialPlatform =
//       FlutterSysInfoPlatform.instance;

//   test('$MethodChannelFlutterSysInfo is the default instance', () {
//     expect(initialPlatform, isInstanceOf<MethodChannelFlutterSysInfo>());
//   });

//   test('getPlatformVersion', () async {
//     FlutterSysInfo flutterSysInfoPlugin = FlutterSysInfo();
//     MockFlutterSysInfoPlatform fakePlatform = MockFlutterSysInfoPlatform();
//     FlutterSysInfoPlatform.instance = fakePlatform;

//     expect(await flutterSysInfoPlugin.getPlatformVersion(), '42');
//   });
// }
