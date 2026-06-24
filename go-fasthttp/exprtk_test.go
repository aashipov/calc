package main

import "testing"

func TestCalculateViaExprtk(t *testing.T) {
	t.Helper()
	tests := []struct {
		name       string
		expression string
		expected   string
	}{
		{"simple addition", "2+2", "4"},
		{"complex expression", "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2", "19.988432890485228"},
		{"invalid expression", "invalid", NaN},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			actual := CalculateViaExprtk(tt.expression)
			if actual != tt.expected {
				t.FailNow()
			}
		})
	}
}
