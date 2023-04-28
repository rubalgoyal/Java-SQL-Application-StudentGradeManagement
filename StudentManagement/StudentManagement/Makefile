GRADLEW	?=	./gradlew
@hostName	?=	default_value
@port	?=	default_value
@dbName	?=	default_value
@username	?=	default_value
@password	?=	default_value

build:
		@echo "build"
		$(GRADLEW)	clean	shadowJar
clean:
		@echo "clean"
		$(GRADLEW) clean
#runApp:
#		@echo "run"
#		java -cp build/libs/StudentManagement-1.0-SNAPSHOT-all.jar EnrollmentApp $(@hostName) $(@port) $@(dbName) $(@username) $(@password)
runApp:
		@echo "run"
		java -cp build/libs/StudentManagement-1.0-SNAPSHOT-all.jar EnrollmentApp $(hostName) $(port) $(dbName) $(username) $(password)
