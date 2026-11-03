DENO := deno
TARGET := calc
TARGET_TS := $(TARGET).ts
TARGET_IT := $(TARGET)-it

.PHONY: all clean do-install test run

all: $(TARGET) test

clean:
	rm -f $(TARGET)
	rm -f $(TARGET_IT)

do-install:
	$(DENO) install

$(TARGET): do-install
	$(DENO) run build

test: $(TARGET)
	cp $(TARGET) $(TARGET_IT)
	./$(TARGET_IT) &
	sleep 5
	../test/calc-test.sh
	pkill -f $(TARGET_IT)

run:
	./$(TARGET) --parallel
