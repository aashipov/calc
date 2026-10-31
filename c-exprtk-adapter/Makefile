# Variables
CXX               := g++
TARGET_LIB        := libc-exprtk-adapter.so
SOURCES           := c-exprtk-adapter.cpp
HEADERS           := c-exprtk-adapter.h
EXPRTK_HPP        := exprtk.hpp

# New integrated binary target
EXPRTK_CALCULATOR  := exprtk-calculator
CALC_TARGET        := $(EXPRTK_CALCULATOR)/$(EXPRTK_CALCULATOR)
CALC_SOURCES       := $(EXPRTK_CALCULATOR)/$(EXPRTK_CALCULATOR).cpp

# Profile Configuration (Defaults to release)
PROFILE           ?= release

ifeq ($(PROFILE),Debug)
    PROFILE_FLAGS := -O0 -g -DDEBUG
    BUILD_DIR     := build/Debug
else
    PROFILE_FLAGS := -O3 -DNDEBUG
    BUILD_DIR     := build/Release
endif

# Combined flags
CXXFLAGS     := -std=c++20 -Wall -Wextra -fPIC -I. $(PROFILE_FLAGS)
OBJECTS      := $(addprefix $(BUILD_DIR)/, $(SOURCES:.cpp=.o))
TARGET       := $(BUILD_DIR)/$(TARGET_LIB)

# Objects and binary path for calculator
CALC_OBJECTS := $(addprefix $(BUILD_DIR)/, $(CALC_SOURCES:.cpp=.o))
CALC_BINARY  := $(BUILD_DIR)/$(CALC_TARGET)

# Installation paths
PREFIX       ?= /usr/local
INCLUDEDIR   := $(PREFIX)/include
LIBDIR       := $(PREFIX)/lib
BINDIR       := $(PREFIX)/bin

# Utilities
INSTALL      := install
INSTALL_DATA := $(INSTALL) -m 644
INSTALL_PROG := $(INSTALL) -m 755

.PHONY: all clean install uninstall download-exprtk

# Default target builds everything
all: download-exprtk $(TARGET) $(CALC_BINARY)

# Build the shared library inside the profile directory
$(TARGET): $(OBJECTS)
	$(CXX) -shared -o $@ $^

# Build the sole calculator executable inside the profile directory
$(CALC_BINARY): $(CALC_OBJECTS) $(TARGET)
	$(CXX) $(CXXFLAGS) -o $@ $(CALC_OBJECTS) -L$(BUILD_DIR) -lc-exprtk-adapter -Wl,-rpath,'$$ORIGIN'

# Compile source files into the profile directory
$(BUILD_DIR)/%.o: %.cpp
	@mkdir -p $(dir $@)
	$(CXX) $(CXXFLAGS) -MMD -MP -c $< -o $@

# Include compiler-generated dependency tracking files
-include $(OBJECTS:.o=.d) $(CALC_OBJECTS:.o=.d)

# Clean all build artifacts
clean:
	rm -rf build

# Download exprtk.hpp locally into the build context or vendor directory if missing
download-exprtk:
	@if [ ! -f $(EXPRTK_HPP) ]; then \
		echo "Downloading exprtk.hpp..."; \
		curl -L -o $(EXPRTK_HPP) https://raw.githubusercontent.com/ArashPartow/exprtk/refs/heads/master/$(EXPRTK_HPP) ; \
	fi

# Install headers, library, and the calculator binary using standard system tools
install: all
	$(INSTALL) -d $(DESTDIR)$(INCLUDEDIR) $(DESTDIR)$(LIBDIR) $(DESTDIR)$(BINDIR)
	$(INSTALL_DATA) $(HEADERS) $(DESTDIR)$(INCLUDEDIR)/
	$(INSTALL_DATA) $(EXPRTK_HPP) $(DESTDIR)$(INCLUDEDIR)/
	$(INSTALL_PROG) $(TARGET) $(DESTDIR)$(LIBDIR)/
	$(INSTALL_PROG) $(CALC_BINARY) $(DESTDIR)$(BINDIR)/$(EXPRTK_CALCULATOR)

# Remove installed files
uninstall:
	rm -f $(DESTDIR)$(INCLUDEDIR)/$(HEADERS)
	rm -f $(DESTDIR)$(LIBDIR)/$(TARGET_LIB)
	rm -f $(DESTDIR)$(BINDIR)/$(EXPRTK_CALCULATOR)
	rm -f $(DESTDIR)$(INCLUDEDIR)/$(EXPRTK_HPP)
