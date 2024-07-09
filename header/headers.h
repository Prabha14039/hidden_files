#ifndef HEADERS_HEADERS_H
#define HEADERS_HEADERS_H

#include<stddef.h>

int get_cluster_size(const char *path);
long read_file(char *file_path);
void dummy_files_generator(size_t cluster_size,int parts,int rem_size);
void file_splitter(char * file_path,size_t cluster_size,int parts,int rem_size);

#endif
