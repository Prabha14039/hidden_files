#ifndef HEADERS_HEADERS_H
#define HEADERS_HEADERS_H

#include<stddef.h>

int get_cluster_size( char *path);
size_t read_file(char *file_path);
void generation_and_sequencing(size_t cluster_size,int parts,int rem_size);
void file_splitter(char * file_path,size_t cluster_size,int parts,int rem_size);
int win_cluster_size( char *path);

#endif
