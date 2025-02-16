// ignore_for_file: constant_identifier_names

enum StorageInfoType {
  TOTAL('total'),
  AVAILABLE('available');

  final String value;
  const StorageInfoType(this.value);
}

enum MemoryUnit {
  KB,
  MB,
  GB,
}
