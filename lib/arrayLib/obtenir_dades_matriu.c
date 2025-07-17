void obtenir_dades_matriu(int* dades,int maxf,int maxc,int* nf,int* nc){
    *nf = maxf / 2;      *nc = maxc / 2;
    for(int f = 0; f < *nf; f ++){
        for(int c = 0; c < *nc; c ++){
            dades[f * (*nc) + c] = f + c;
        }
    }
}