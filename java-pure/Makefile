JAVA_TARGET := java-build
JAVA_BUILD_CMD := mvn surefire-report:report package
SHARED_LIBRARY_HARNESS ?= JNI

.PHONY: all clean $(JAVA_TARGET) run

all: clean $(JAVA_TARGET)

clean:
	rm -rf target/

$(JAVA_TARGET):
	$(JAVA_BUILD_CMD)
	SHARED_LIBRARY_HARNESS=FFM $(JAVA_BUILD_CMD)

run:
	SHARED_LIBRARY_HARNESS=$(SHARED_LIBRARY_HARNESS) java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005 -jar target/calc-shaded.jar
