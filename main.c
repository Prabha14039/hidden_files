#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<errno.h>
#include<SDL.h>
#include<stdbool.h>
#include<SDL_ttf.h>

#ifdef _WIN32
	#include<windows.h>
#elid _linux_
	#include<unistd.h>
#endif

#define cluster_size 512
#define BUTTON_WIDTH 200
#define BUTTON_HEIGHT 50

typedef struct {
	SDL_Rect rect;
	SDL_Color color;
	SDL_Color hoverColor;
	SDL_Color activeColor;
	int isHovered;
	int isActive;
	const char *text;
}Button;

void text_render(SDL_Renderer *renderer,Button *button,SDL_Surface *textSurface,SDL_Texture *textTexture){
	int textWidth = textSurface->w;
	int textHeight = textSurface->h;
	int textX = button->rect.x + (button->rect.w - textWidth) / 2;
	int textY = button->rect.y + (button->rect.h - textHeight) / 2;

	// Render the text texture
	SDL_Rect textRect = { textX, textY, textWidth, textHeight };
	SDL_RenderCopy(renderer, textTexture, NULL, &textRect);

	// Clean up texture
	SDL_DestroyTexture(textTexture);
}

int is_mouse_in_button(Button *button,int x,int y)
{
	return x>= button->rect.x && x<=(button->rect.x +button->rect.w) &&
	y>= button->rect.y && y<=(button->rect.y +button->rect.h);
}

void button_renderer(SDL_Renderer *renderer,Button *button,TTF_Font *font)
{
	SDL_Color color = button->isActive ? button->activeColor : (button->isHovered ? button->hoverColor : button->color);
	SDL_SetRenderDrawColor(renderer, color.r, color.g, color.b, 255);
	SDL_RenderFillRect(renderer, &button->rect);
	
	if (font && button->text) {
		SDL_Surface *textSurface = TTF_RenderText_Solid(font, button->text, ( SDL_Color ){255, 255, 255, 255}); // White color
		if (textSurface) {
			SDL_Texture *textTexture = SDL_CreateTextureFromSurface(renderer, textSurface);
			if(textTexture){
				text_render(renderer,button,textSurface,textTexture);
			}
			SDL_FreeSurface(textSurface);
		}
	}
}
void  scc(int code)
{
	if(code < 0)
	{
		fprintf(stderr,"SDL ERROR :%s\n",SDL_GetError());
		exit(1);
	}
}

void *scp (void *ptr)
{
	if( ptr == NULL)
	{
		fprintf(stderr,"SDL ERROR :%s\n",SDL_GetError());
		exit(1);
	}
	return ptr;
}
long read_file(char *file_path) 
{
	char * buffer = NULL;
	FILE *critical_file=fopen(file_path,"rb");
	if (critical_file == NULL)
	{
		goto error;
	}

	if(fseek(critical_file,0,SEEK_END)<0)
	{
		goto error;
	}

	long position=ftell(critical_file);
	if(position <0)
	{
		goto error;
	}
	return position;
	error:
	if (critical_file) {
		fclose(critical_file);
	}

	if (buffer) {
		free(buffer);
	}

	return -1;
}

 void file_splitter(char * file_path,long m)
{
	FILE *f=fopen(file_path,"rb");
	if (f == NULL)
	{
		fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
	  ,file_path,strerror(errno));
		exit(1);
	}

	int parts = (m/cluster_size);
	int rem_size=(m%cluster_size);

	for (int i=0;i<parts;i++)
	{
		char output_file[100];
		snprintf(output_file ,sizeof(output_file), "%s.part%d" , file_path, i+1);
		FILE * outputfile=fopen(output_file,"wb");
		if (outputfile == NULL)
		{
			fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
	   ,output_file,strerror(errno));
			exit(1);

		}
		char buffer [cluster_size];
		size_t read_buffer=fread(buffer,1,sizeof(buffer),f);
		fwrite(buffer,1 ,read_buffer,outputfile);
		fclose(outputfile);
	}

	if (rem_size>0)
	{	
		char rem_file[100];
		snprintf(rem_file ,sizeof(rem_file), "%s.part_remfile" , file_path);
		FILE * rem=fopen(rem_file,"wb");
		if (rem == NULL)
		{
			fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
	   ,rem_file,strerror(errno));
			exit(1);
		}
		char buffer [rem_size];
		size_t read_buffer=fread(buffer,1,sizeof(buffer),f);
		fwrite(buffer,1 ,read_buffer,rem);
		fclose(rem);
	}

	fclose(f);
	printf("the %s file have sucessfully been split : \n %d :size -- %d bytes \n 1 :size -- %d bytes \n",file_path,parts,cluster_size,rem_size);
	
}
int main(int argc,char **argv)
{
	if(argc<2)
	{
		fprintf(stderr,"provide the necessary agrument\n");
		exit(1);
	}
	char * input_file_path = argv[1];
	Button button = {{ 800/ 2 - BUTTON_WIDTH / 2,  800/ 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT},{0, 0, 255, 255}, {0, 255, 0, 255}, {255, 0, 0, 255}, 0, 0,"click to start!!!!"};

	if (TTF_Init() < 0) {
		fprintf(stderr, "Unable to initialize SDL_ttf: %s\n", TTF_GetError());
		SDL_Quit();
		exit(1);
	}

	const char *path="/usr/share/fonts/truetype/ubuntu/Ubuntu-B.ttf";
	TTF_Font *font = TTF_OpenFont(path,18 );
	if (!font) {
		fprintf(stderr, "Error loading font: %s\n", TTF_GetError());
		exit(1);
	}

	SDL_Window * window = scp(SDL_CreateWindow("Faker",
					     0, 0, 800,
					     600, SDL_WINDOW_RESIZABLE));
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
						long content_size =read_file(input_file_path);
						if (content_size == -1)
						{
							fprintf(stderr,"ERROR: the file %s could not be read: %s\n",input_file_path,strerror(errno));
							exit(1);
						}
						file_splitter(input_file_path,content_size);
					}
				} break;
			}
		}
		SDL_SetRenderDrawColor(renderer,0,0,0,255);
		SDL_RenderClear(renderer);
		button_renderer(renderer,&button,font);
		SDL_RenderPresent(renderer);
	}
	SDL_DestroyRenderer(renderer);
	SDL_DestroyWindow(window);
	SDL_Quit();

	/*char * input_file_path = argv[1];
	long content_size =read_file(input_file_path);*/
	/*if (content_size == -1)
	{
		fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
	  ,input_file_path,strerror(errno));
		exit(1);
	}
	file_splitter(input_file_path,content_size);*/

	//printf("the size of %s file is :%ld bytes\n",input_file_path,content);

	//printf("hello this is the project");
}
