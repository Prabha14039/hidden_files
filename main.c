#ifdef __unix__

#include<stdio.h>
#include<stdlib.h>
#include"header/headers.h"
#include<errno.h>
#include<sys/statvfs.h>
#include<string.h>

#elif defined(_WIN32) || defined(WIN32) || defined(_WIN64) || defined(WIN64)

#define IS_WINDOWS 1
#include<stdio.h>
#include<stdlib.h>
#include"header/headers.h"
#include<errno.h>
#include<string.h>

#endif

int main(int argc,char **argv)
{
    char * path;
    int cluster_size;

    if(argc<2)
    {
        fprintf(stderr,"provide the necessary agrument\n");
        exit(1);
    }

#ifdef IS_WINDOWS
    path = "C:\\";
    cluster_size = win_cluster_size(path);
#else
    path = "/";
    cluster_size = get_cluster_size(path);
#endif

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
    generation_and_sequencing(cluster_size, parts,rem );
    return 0;
}
