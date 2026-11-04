# Variables
DOTNET := dotnet
PROFILE ?= Release
LOG_LEVEL ?= Warning

# Automatically find the executable path dynamically, handling net8.0, net9.0, etc.
# The shell command runs once and sets the exact path to the calc binary.
EXE_PATH := $(shell find ./bin/$(PROFILE) -type f -name "calc" -o -name "calc.exe" | head -n 1)

.PHONY: all build test run clean

all: test build

build:
	$(DOTNET) publish -c $(PROFILE) -p:PublishSingleFile=true

test:
	Logging__LogLevel__Default=$(LOG_LEVEL) $(DOTNET) test -c $(PROFILE)

run:
	@if [ -z "$(EXE_PATH)" ]; then \
		echo "Error: Binary not found. Try running 'make build' first."; \
		exit 1; \
	fi
	Logging__LogLevel__Default=$(LOG_LEVEL) $(EXE_PATH)

clean:
	$(DOTNET) clean
	find . -type d \( -name "obj" -o -name "bin" \) -exec rm -rf {} +
