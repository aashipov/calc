import 'package:calc/calc.dart';
import 'package:test/test.dart';

const String expression =
    "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";

const double expressionResult = 19.9884328904852281993953511118888854980469;

const String notAnExpression = "nan";

void main() {
  test('expressionViaExprtk', () {
    double result = viaExprtk(expression);
    expect(result, equals(expressionResult));
  });

  test('notAnexpressionViaExprtk', () {
    double result = viaExprtk(notAnExpression);
    expect(result, equals(double.nan));
  });
}
