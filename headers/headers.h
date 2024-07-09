#ifndef HEADERS_HEADERS_H
#define HEADERS_HEADERS_H

#include<stddef.h>
#include<SDL.h>
#include<stdbool.h>
#include<SDL_ttf.h>

typedef struct {
	SDL_Rect rect;
	SDL_Color color;
	SDL_Color hoverColor;
	SDL_Color activeColor;
	int isHovered;
	int isActive;
	const char *text;
}Button;

int win_cluster_size( char *path);
int get_cluster_size( char *path);
size_t read_file(char *file_path);
void generation_and_sequencing(size_t cluster_size,int parts,int rem_size);
void file_splitter(char * file_path,size_t cluster_size,int parts,int rem_size);
void text_render(SDL_Renderer *renderer,Button *button,SDL_Surface *textSurface,SDL_Texture *textTexture);
int is_mouse_in_button(Button *button,int x,int y);
void button_renderer(SDL_Renderer *renderer,Button *button,TTF_Font *font);
void *scp (void *ptr);
void  scc(int code);
void heading_renderer(SDL_Renderer *renderer);

#endif
