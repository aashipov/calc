JAVA_BUILD_CMD := mvn clean package
SHARED_LIBRARY_HARNESS ?= JNI

JAVA_BUILD_CMD := mvn install

.PHONY: all clean java-build run

all: clean java-build

clean:
	rm -rf target/

java-build:
	$(JAVA_BUILD_CMD)
	SHARED_LIBRARY_HARNESS=FFM $(JAVA_BUILD_CMD)

run:
	SHARED_LIBRARY_HARNESS=$(SHARED_LIBRARY_HARNESS) java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005 -jar target/calc-shaded.jar
