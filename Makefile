
all:Main

Main:
	javac -d out -sourcepath src src/FileProcessorGUI.java src/Main.java src/utils/FileUtils.java

clean:
	rm -rf build
