#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<errno.h>
#ifdef _WIN32
	#include<windows.h>
#elid _linux_
	#include<unistd.h>
#endif
#define cluster_size 512
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
	long content_size =read_file(input_file_path);
	if (content_size == -1)
	{
		fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
	  ,input_file_path,strerror(errno));
		exit(1);
	}
	file_splitter(input_file_path,content_size);

	//printf("the size of %s file is :%ld bytes\n",input_file_path,content);
	
	//printf("hello this is the project");
}
