#ifdef __unix__

#include<stdio.h>
#include<stdlib.h>
#include"headers/headers.h"
#include<errno.h>
#include<sys/statvfs.h>
#include<string.h>
#include<SDL.h>
#include<stdbool.h>
#include<SDL_ttf.h>

#elif defined(_WIN32) || defined(WIN32) || defined(_WIN64) || defined(WIN64)

#define IS_WINDOWS 1
#include<stdio.h>
#include<stdlib.h>
#include"headers/headers.h"
#include<errno.h>
#include<string.h>
#include<SDL.h>
#include<stdbool.h>
#include<SDL_ttf.h>

#endif

#define BUTTON_WIDTH 200
#define BUTTON_HEIGHT 50
#define SCREEN_HEIGHT 800
#define SCREEN_WIDTH 600

int main(int argc,char **argv)
{
    char * path;
    int cluster_size;

	if(argc<2)
	{
		fprintf(stderr,"provide the necessary agrument\n");
		exit(1);
	}
	char * input_file_path = argv[1];

#ifdef IS_WINDOWS
    path = "C:\\";
    cluster_size = win_cluster_size(path);
#else
    path = "/";
    cluster_size = get_cluster_size(path);
#endif

    long content_size =read_file(input_file_path);
    if (content_size == -1)
    {
        fprintf(stderr,"ERROR: the file %s could not be read: %s\n",input_file_path,strerror(errno));
        exit(1);
    }

    int parts= content_size/cluster_size;
    int rem = content_size%cluster_size;

	Button button = {{ SCREEN_HEIGHT/ 2 - BUTTON_WIDTH / 2,  SCREEN_HEIGHT/ 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT},{255, 255, 255, 255}, {255, 255, 255, 255}, {255, 255, 255, 255}, 0, 0,"click to start!!!!"};
	if (TTF_Init() < 0) {
		fprintf(stderr, "Unable to initialize SDL_ttf: %s\n", TTF_GetError());
		SDL_Quit();
		exit(1);
	}

	const char *font_path="/usr/share/fonts/truetype/ubuntu/Ubuntu-B.ttf";
	TTF_Font *font = TTF_OpenFont(font_path,30);
	if (!font) {
		fprintf(stderr, "Error loading font: %s\n", TTF_GetError());
		exit(1);
	}

	SDL_Window * window = scp(SDL_CreateWindow("Faker",
					     0, 0,SCREEN_HEIGHT,
					     SCREEN_WIDTH, SDL_WINDOW_RESIZABLE));
	SDL_Renderer * renderer= scp(SDL_CreateRenderer(window,-1,SDL_RENDERER_ACCELERATED));
	bool quit = false ;
	while(!quit)
	{
		SDL_Event event = {0};
		while (SDL_PollEvent(&event)) {
			switch (event.type) {
				case SDL_QUIT: {
					quit = true;
				} break;
				case SDL_MOUSEMOTION:{
					button.isHovered=is_mouse_in_button(&button,event.motion.x,event.motion.y);
				} break;
				case SDL_MOUSEBUTTONDOWN:{
					if(button.isHovered)
					{
						button.isActive=1;
					}
				}break;
				case SDL_MOUSEBUTTONUP: {
					if(button.isActive) {
						button.isActive=0;
					}
					if(button.isHovered){
                        file_splitter(input_file_path, cluster_size, parts, rem);
                        generation_and_sequencing(cluster_size, parts,rem );
					}
				} break;
			}
		}
		SDL_SetRenderDrawColor(renderer,0,0,0,255);
		SDL_RenderClear(renderer);
		button_renderer(renderer,&button,font);
        heading_renderer(renderer);
		SDL_RenderPresent(renderer);
	}

    TTF_CloseFont(font);
    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    TTF_Quit();
    SDL_Quit();

}
