package main

import "testing"

var (
	expression       = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
	expressionResult = "19.988432890485228"
	NaN              = "NaN"
)

func TestExprtkExpression(t *testing.T) {
	actual := CalculateViaExprtk(expression)
	if actual != expressionResult {
		t.Errorf("Expected %s but got %v", expressionResult, actual)
	}
}

func TestExprtkNaN(t *testing.T) {
	actual := CalculateViaExprtk(NaN)
	if actual != NaN {
		t.Errorf("Expected %s but got %v", NaN, actual)
	}
}
