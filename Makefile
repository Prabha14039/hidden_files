
all:Main

Main:
	javac -d build  -sourcepath src src/FileProcessorGUI.java src/Main.java src/Encryptor.java

clean:
	del .\build\
