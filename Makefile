PLUGIN_PATH := $(shell locate plugin.jar | head -n 1)

default: build

build : clean
	@echo "Building LaunchCraft.jar..."
	@if [ -f $(PLUGIN_PATH) ]; then \
		javac -Xlint:none -sourcepath . -classpath $(PLUGIN_PATH) LaunchCraft.java; \
		find . -name '*.class' -print > classes.list; \
		jar cvf LaunchCraft.jar @classes.list; \
		rm classes.list; \
	fi

sign : build
	@echo "Signing LaunchCraft.jar..."
	@jarsigner -keystore keystore.db LaunchCraft.jar signFiles

clean :
	@echo "Cleaning built products..."
	@find . -iname "*.class" | xargs rm -f
	@find . -iname "*.jar" | xargs rm -f
