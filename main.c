#include<stdio.h>
#include<stdlib.h>
#include "header/headers.h"
#include<errno.h>
#include <sys/statvfs.h>
#include<string.h>

int main(int argc,char **argv)
{
	if(argc<2)
	{
		fprintf(stderr,"provide the necessary agrument\n");
		exit(1);
    }

    const char *path = "/";  // Path to the filesystem
    int cluster_size = get_cluster_size(path);

	char * input_file_path = argv[1];
	size_t content_size =read_file(input_file_path);

	if (content_size == 0)
	{
		fprintf(stderr,"ERROR: the file %s could not be read: %s\n"
	  ,input_file_path,strerror(errno));
		exit(1);
	}

	int parts= content_size/cluster_size;
	int rem = content_size%cluster_size;

	file_splitter(input_file_path,cluster_size,parts,rem);
	dummy_files_generator(cluster_size,parts,rem);
    return 0;
}
