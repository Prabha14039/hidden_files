
all:Main always

always:
	mkdir -p build

Main:
	javac -d build  -sourcepath src src/FileProcessorGUI.java src/Main.java src/Encryptor.java

run:
	cd build && java FileProcessorGUI

clean:
	del .\build\
