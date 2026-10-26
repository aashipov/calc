# Build profiles (default to release)
PROFILE      ?= release
CARGO_FLAGS  :=
ifeq ($(PROFILE),release)
    CARGO_FLAGS += --release
endif

CARGO        := cargo
TARGET_DIR   := target

.PHONY: all clean build test

all: build test

clean:
	rm -rf $(TARGET_DIR)

build:
	$(CARGO) build $(CARGO_FLAGS)

test:
	$(CARGO) test $(CARGO_FLAGS)

run:
	$(TARGET_DIR)/$(PROFILE)/calc
