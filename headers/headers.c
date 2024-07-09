#ifdef __unix__

#include "headers.h"
#include<stddef.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<errno.h>
#include <sys/stat.h>
#include <sys/statvfs.h>
#include<SDL.h>
#include<stdbool.h>
#include<SDL_ttf.h>

int get_cluster_size(char *path) {
    struct statvfs stat;

    if (statvfs(path, &stat) != 0) {
        perror("statvfs");
        exit(EXIT_FAILURE);
    }
    unsigned long cluster_size = stat.f_frsize;
    printf("Cluster size: %lu bytes\n", cluster_size);
    return cluster_size;
}

#elif defined(_WIN32) || defined(WIN32) || defined(_WIN64) || defined(WIN64)

#include "headers.h"
#include<stddef.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<errno.h>
#include<windows.h>
#include <direct.h>
#include<SDL.h>
#include<stdbool.h>
#include<SDL_ttf.h>

typedef struct {
	sdl_rect rect;
	sdl_color color;
	sdl_color hovercolor;
	sdl_color activecolor;
	int ishovered;
	int isactive;
	const char *text;
}button;

int win_cluster_size( char *path) {
    DWORD sectorsPerCluster;
    DWORD bytesPerSector;
    DWORD numberOfFreeClusters;
    DWORD totalNumberOfClusters;

    if (!GetDiskFreeSpaceA(path, &sectorsPerCluster, &bytesPerSector, &numberOfFreeClusters, &totalNumberOfClusters)) {
        fprintf(stderr, "GetDiskFreeSpace failed. Error: %lu\n", GetLastError());
        return -1;
    }

    DWORD cluster_size = sectorsPerCluster * bytesPerSector;
    printf("Cluster size: %lu bytes\n", cluster_size);
    return cluster_size;
}

#endif

size_t read_file(char *file_path)
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

void generation_and_sequencing(size_t cluster_size,int parts,int rem_size)
{
    char buffer [cluster_size];
    char rem_file[100];
    char output_file[100];
    FILE * outputfile;
    FILE * rem;

#ifdef _WIN64
   _mkdir("files");
#elif _WIN32
   _mkdir("files");
#else
    mkdir("files",0777);
#endif

    for (int i=0;i<parts;i++)
    {
        snprintf(output_file ,sizeof(output_file), "./files/A_%03d" ,i+1);
        outputfile =fopen(output_file,"wb");
        if (outputfile == NULL)
        {
            fprintf(stderr,"ERROR: the File %s could not be read: %s\n"
                    ,output_file,strerror(errno));
            exit(1);
        }
        for (size_t j = 0; j < cluster_size; j++) {
            buffer[j] = 1;
        }
        fwrite(buffer,1 ,cluster_size,outputfile);
        fclose(outputfile);

        snprintf(output_file ,sizeof(output_file), "./files/D_%03d", i+1);
        outputfile=fopen(output_file,"wb");
        if (outputfile == NULL)
        {
            fprintf(stderr,"ERROR: the File %s could not be read: %s\n"
                    ,output_file,strerror(errno));
            exit(1);
        }
        for (size_t j = 0; j < cluster_size; j++) {
            buffer[j] = 0;
        }
        fwrite(buffer,1 ,cluster_size,outputfile);
        fclose(outputfile);

    }

    if (rem_size>0)
    {
        snprintf(rem_file ,sizeof(rem_file), "./files/AR_00");
        rem=fopen(rem_file,"wb");
        if (rem == NULL)
        {
            fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
                    ,rem_file,strerror(errno));
            exit(1);
        }

        for (size_t j = 0; j < cluster_size; j++) {
            buffer[j] = 1;
        }

        fwrite(buffer,1 ,cluster_size,rem);
        fclose(rem);

        snprintf(rem_file ,sizeof(rem_file), "./files/DR_00");
        rem=fopen(rem_file,"wb");
        if (rem == NULL)
        {
            fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
                    ,rem_file,strerror(errno));
            exit(1);
        }

        for (size_t j = 0; j < cluster_size; j++) {
            buffer[j] = 0;
        }

        fwrite(buffer,1 ,cluster_size,rem);
        fclose(rem);

        snprintf(rem_file ,sizeof(rem_file), "./files/AR_01");
        rem=fopen(rem_file,"wb");
        if (rem == NULL)
        {
            fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
                    ,rem_file,strerror(errno));
            exit(1);
        }

        for (size_t j = 0; j < cluster_size; j++) {
            buffer[j] = 1;
        }

        fwrite(buffer,1 ,cluster_size,rem);
        fclose(rem);

    }

#ifdef _WIN64
    printf("The dummy files have sucessfully been generated: \n %d :size -- %zu bytes \n 1 :size -- %d bytes \n",parts,cluster_size,rem_size);
    printf("The additional files have sucessfully been generated: \n %d :size -- %zu bytes \n 2 :size -- %d bytes \n",parts,cluster_size,rem_size);
#elif _WIN32
    printf("The dummy files have sucessfully been generated: \n %d :size -- %zu bytes \n 1 :size -- %d bytes \n",parts,cluster_size,rem_size);
    printf("The additional files have sucessfully been generated: \n %d :size -- %zu bytes \n 2 :size -- %d bytes \n",parts,cluster_size,rem_size);
#else
    printf("The dummy files have sucessfully been generated: \n %d :size -- %zi bytes \n 1 :size -- %d bytes \n",parts,cluster_size,rem_size);
    printf("The additional files have sucessfully been generated: \n %d :size -- %zi bytes \n 2 :size -- %d bytes \n",parts,cluster_size,rem_size);
#endif
}

 void file_splitter(char * file_path,size_t cluster_size,int parts,int rem_size)
{
	FILE *f=fopen(file_path,"rb");
	if (f == NULL)
	{
		fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
	  ,file_path,strerror(errno));
		exit(1);
	}

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

#ifdef _WIN64
	printf("the %s file have sucessfully been split : \n %d :size -- %zu bytes \n 1 :size -- %d bytes \n",file_path,parts,cluster_size,rem_size);
#elif _WIN32
	printf("the %s file have sucessfully been split : \n %d :size -- %zu bytes \n 1 :size -- %d bytes \n",file_path,parts,cluster_size,rem_size);
#else
	printf("the %s file have sucessfully been split : \n %d :size -- %zi bytes \n 1 :size -- %d bytes \n",file_path,parts,cluster_size,rem_size);
#endif
}

void text_render(SDL_Renderer *renderer,Button *button,SDL_Surface *textSurface,SDL_Texture *textTexture)
{
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
		SDL_Surface *textSurface = TTF_RenderText_Solid(font, button->text, ( SDL_Color ){0,0,0,255}); //Black
		if (textSurface) {
			SDL_Texture *textTexture = SDL_CreateTextureFromSurface(renderer, textSurface);
			if(textTexture){
				text_render(renderer,button,textSurface,textTexture);
			}
			SDL_FreeSurface(textSurface);
		}
	}
}

void heading_renderer(SDL_Renderer *renderer)
{
	const char *font_path="/usr/share/fonts/truetype/ubuntu/Ubuntu-B.ttf";
	TTF_Font *font = TTF_OpenFont(font_path,40);
	if (!font) {
		fprintf(stderr, "Error loading font: %s\n", TTF_GetError());
		exit(1);
	}

    SDL_Color color = {255,255,255,255};
    if (font)
    {
        SDL_Surface *textSurface = TTF_RenderText_Solid(font,"hidden_files",color);
        if(textSurface)
        {
            SDL_Texture *textTexture = SDL_CreateTextureFromSurface(renderer, textSurface);
            if (textTexture) {
                SDL_Rect destRect = {300, 70, textSurface->w, textSurface->h};
                SDL_RenderCopy(renderer, textTexture, NULL, &destRect);
                SDL_DestroyTexture(textTexture);
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
