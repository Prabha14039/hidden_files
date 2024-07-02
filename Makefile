GCC=-Wall -Wextra
GC=gcc


all:main

main:main.c
	$(GC) $(GCC) -o build/main main.c

clean:
	rm -rf build/./

compile:
	./build/main critical_file.txt
