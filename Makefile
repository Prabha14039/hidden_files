GCC=-Wall -Wextra
GC=gcc


all:main

main:main.c
	$(GC) $(GCC) -o build/main main.c header/headers.c

clean:
	rm -rf build/*

build:
	mkdir build
