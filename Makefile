CFLAGS=-Wall -Wextra -std=c11 -pedantic -ggdb `pkg-config --cflags sdl2`
GCC=gcc

LIBS=`pkg-config --libs sdl2` -lm
all:main

main:main.c
	$(GCC) $(CFLAGS) -o build/main main.c $(LIBS) -lSDL2_ttf -lm

clean:
	rm -rf build/*

move:
	cp main.c build/
