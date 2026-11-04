# Project variables
TARGET_NAME = calc
TEST_TARGET_NAME = $(TARGET_NAME)_tests
HTTPLIB_H_NAME = httplib.h

# Core files (Looks for source files inside src/ or root)
SRC = $(TARGET_NAME).cpp
TEST_SRCS = $(wildcard tests/*.cpp)

# Compiler settings & flags
CXX = g++
CXXFLAGS = -std=c++17 -Wall -Wextra -Isrc -I.
LIBS = -lPocoFoundation -lPocoNet -lPocoUtil -lc-exprtk-adapter -lc-meval-adapter
TEST_LIBS = -lgtest -lgtest_main -lpthread

# Installation paths
PREFIX = /usr/local
BINDIR = $(PREFIX)/bin

# Build Profile Configuration (Default to Release)
PROFILE ?= Release
BUILD_DIR = $(PROFILE)

ifeq ($(PROFILE), Debug)
    CXXFLAGS += -g -O0 -DDEBUG
else
    CXXFLAGS += -O3 -DNDEBUG
endif

# Define object and binary locations based on current PROFILE
OBJ = $(addprefix $(BUILD_DIR)/, $(SRC:.cpp=.o))
TEST_OBJS = $(addprefix $(BUILD_DIR)/, $(TEST_SRCS:.cpp=.o))

TARGET = $(BUILD_DIR)/$(TARGET_NAME)
TEST_TARGET = $(BUILD_DIR)/$(TEST_TARGET_NAME)

.PHONY: all download-httplib clean test run

# Default target: builds main application and test binaries side-by-side
all: download-httplib $(TARGET) $(TEST_TARGET) test

# Link main executable
$(TARGET): $(OBJ)
	@mkdir -p $(dir $@)
	$(CXX) $(CXXFLAGS) -o $@ $^ $(LIBS)

# Link test executable (Excludes calc.o to avoid duplicate main functions with gtest_main)
$(TEST_TARGET): $(TEST_OBJS) $(filter-out $(BUILD_DIR)/calc.o, $(OBJ))
	@mkdir -p $(dir $@)
	$(CXX) $(CXXFLAGS) -o $@ $^ $(LIBS) $(TEST_LIBS)

# Compile source files into the build directory profile tree
$(BUILD_DIR)/%.o: %.cpp
	@mkdir -p $(dir $@)
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Build and run the test suite for the active profile
test: $(TEST_TARGET)
	@./$(TEST_TARGET)

# Wipe everything out completely
clean:
	rm -rf $(PROFILE)

download-httplib:
	@if [ ! -f $(HTTPLIB_H_NAME) ]; then \
		echo "Downloading $(HTTPLIB_H_NAME)..."; \
		curl -L -o $(HTTPLIB_H_NAME) https://raw.githubusercontent.com/yhirose/cpp-httplib/refs/heads/master/httplib.h ; \
	fi

run:
	./$(TARGET)