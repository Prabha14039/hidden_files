
all:Main

Main:
	javac -d build  -sourcepath src src/FileProcessorGUI.java src/Main.java src/utils/FileUtils.java

clean:
	rm -rf build
