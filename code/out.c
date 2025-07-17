#include <stdbool.h>
void obtenir_dades_matriu(int dades[][], int maxf, int maxc, int nf, int nc);
void obtenir_dades_senti(int dades[], int max, int senti);
void obtenir_dades_vector(int dades[], int max, int n_elem);
/* -- Constant variable definitions -- */
/* ------------------------------------ */
int main()
{
	/*  -- Variable definitions  -- */
		int t[10];
		int m[10][10];
		int max, n_elems;
		int maxf, maxc, nf, nc;
	/* ----------------------------- */
	obtenir_dades_vector(t,  max,  n_elems);
	obtenir_dades_senti(t,  max,  -1);
	obtenir_dades_matriu(m,  maxf,  maxc,  nf,  nc);
}
