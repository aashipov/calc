BUN := bun
NODE_MODULES := node_modules
TARGET := calc
TARGET_TS := $(TARGET).ts
TARGET_IT := $(TARGET)-it
TARGET_CLUSTER := $(TARGET)-cluster.ts

.PHONY: all clean test test-cluster run run-cluster

all: $(TARGET) test test-cluster

clean:
	rm -rf $(NODE_MODULES)
	rm -f $(TARGET)
	rm -f $(TARGET_IT)

$(NODE_MODULES):
	$(BUN) install

$(TARGET): $(NODE_MODULES)
	$(BUN) build $(TARGET_TS) --compile

test: $(TARGET)
	cp $(TARGET) $(TARGET_IT)
	./$(TARGET_IT) &
	sleep 5
	../test/calc-test.sh
	pkill -f $(TARGET_IT)

test-cluster: $(NODE_MODULES)
	bun $(TARGET_CLUSTER) &
	sleep 5
	../test/calc-test.sh
	pkill -f $(TARGET_TS)

run:
	bun $(TARGET_CLUSTER)
