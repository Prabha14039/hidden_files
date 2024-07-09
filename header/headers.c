#ifdef __unix__

#include "headers.h"
#include<stddef.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<errno.h>
#include <sys/stat.h>
#include <sys/statvfs.h>

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

	/*
	buffer = malloc(sizeof(char) * position);
	if(fseek(critical_file,0,SEEK_SET)<0)
	{
		goto error;
	}
	size_t m=fread(buffer,1,position,critical_file);

	if(ferror(critical_file)){
		goto error ;
	}
	if(size){
		*size=m;
	}
	fclose(critical_file);
	return buffer;
	*/

	error:
	if (critical_file) {
		fclose(critical_file);
	}

	if (buffer) {
		free(buffer);
	}

	return -1;
}

/*void dummy_files_generator(size_t cluster_size,int parts,int rem_size)
{

	char * file_path = "/home/prabha03/personal/hidden_files/build/dummy_generated";

	for (int i=0;i<parts;i++)
	{
		char output_file[100];
		snprintf(output_file ,sizeof(output_file), "%s.part%d" , file_path, i+1);
		FILE * outputfile=fopen(output_file,"wb");
		if (outputfile == NULL)
		{
			fprintf(stderr,"ERROR: the File %s could not be read: %s\n"
	   ,output_file,strerror(errno));
			exit(1);
		}
        char buffer [cluster_size];
        for (size_t j = 0; j < cluster_size; j++) {
            buffer[j] = 0;
        }
		fwrite(buffer,1 ,cluster_size,outputfile);
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
        char buffer [cluster_size];
        for (size_t j = 0; j < cluster_size; j++) {
            buffer[j] = 0;
        }
		fwrite(buffer,1 ,cluster_size,rem);
		fclose(rem);
	}
	printf("The dummy files have sucessfully been generated: \n %d :size -- %zi bytes \n 1 :size -- %d bytes \n",parts,cluster_size,rem_size);
}*/

void generation_and_sequencing(size_t cluster_size,int parts,int rem_size)
{
    char buffer [cluster_size];
    char rem_file[100];
    char output_file[100];
    FILE * outputfile;
    FILE * rem;

#ifdef _WIN64 || _WIN32
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

    printf("The additional files have sucessfully been generated: \n %d :size -- %zi bytes \n 2 :size -- %d bytes \n",parts,cluster_size,rem_size);
    printf("The dummy files have sucessfully been generated: \n %d :size -- %zi bytes \n 1 :size -- %d bytes \n",parts,cluster_size,rem_size);
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
	printf("the %s file have sucessfully been split : \n %d :size -- %zi bytes \n 1 :size -- %d bytes \n",file_path,parts,cluster_size,rem_size);
}
