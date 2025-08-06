void obtenir_dades_vector(int dades[], int max, int* n_elems){
    *n_elems = max / 2;
    for(int i = 0; i < *n_elems; i++) dades[i] = i;
}