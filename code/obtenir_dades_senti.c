void obtenir_dades_senti(int dades[],int max,int senti){
    int i;  int n_elems = max / 2;
    for(i = 0; i < n_elems-1; i++) dades[i] = i;
    dades[i] = senti;
}