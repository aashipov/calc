JAVA_BUILD_CMD := mvn clean package
SHARED_LIBRARY_HARNESS ?= JNI

.PHONY: mvn-clean-package run

mvn-clean-package:
	$(JAVA_BUILD_CMD)
	SHARED_LIBRARY_HARNESS=FFM $(JAVA_BUILD_CMD)

run:
	SHARED_LIBRARY_HARNESS=$(SHARED_LIBRARY_HARNESS) java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005 -jar target/calc-shaded.jar
