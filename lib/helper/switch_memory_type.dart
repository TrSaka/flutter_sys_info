import 'package:flutter_sys_info/constants/enums/enums.dart';

int switchMemoryType(MemoryUnit memoryUnit, int? memory) {
  switch (memoryUnit) {
    case MemoryUnit.KB:
      return memory! ~/ 1024;
    case MemoryUnit.MB:
      return memory! ~/ (1024 * 1024);
    case MemoryUnit.GB:
      return memory! ~/ (1024 * 1024 * 1024);
  }
}
