import 'dart:ffi';
import 'package:ffi/ffi.dart';

final DynamicLibrary cExprtkAdapter = DynamicLibrary.open(
  'libc-exprtk-adapter.so',
);

typedef CalculateC = Double Function(Pointer<Utf8> expr);

typedef CalculateDart = double Function(Pointer<Utf8> expr);

final CalculateDart calculateViaExprtkAdapter = cExprtkAdapter
    .lookup<NativeFunction<CalculateC>>('calculate')
    .asFunction();

double viaExprtk(String expr) {
  final Pointer<Utf8> pointer = expr.toNativeUtf8();
  double result = calculateViaExprtkAdapter(pointer);
  malloc.free(pointer);
  return result;
}

int calculate() {
  return 6 * 7;
}
