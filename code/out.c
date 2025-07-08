#include <stdbool.h>
//Constant variable definitions
	#define C 2
	#define CONST_FLOAT 3.3
	#define CONST_CHAR 'a'
	#define C 2
	#define CONST_FLOAT 3.3
	#define CONST_CHAR 'a'
//Structure definition
	typedef struct
	{
		char nom[10];
		float numero;
		bool l;
	} tipus_prueba_t;
bool boolProva(int param1, float* param2)
{
	return (2+boolProva (1, &( param1)));
}
int pruebaFunc2(int param1, int* param2)
{
	boolProva(2, &( *param2));
	param1 = param1+1;
	return (param1);
}
bool pruebaFunc(int* param1, float* param2, bool* param3)
{
	//Variable definitions
		int a, b;
		char x;
		bool d;
		float f[20], g[20];
		int k[20][10], j[20][10];
		tipus_prueba_t j;
	*param2 = *param2+2;
	*param2 = pruebaFunc2 (43, &( *param2));
	if ( boolProva(a, &( f[0])) )
	{
		b = 1;
	}
	d = !k && !(k == 2) || (!(!(  k==9)));
	do
	{
		a = *param2 / 2;
		if ( *param2 == *param1 )
		{
			*param2 = 9;
		}
		b = *param1+3;
	} while (*param1 > *param2 + 9);
	d = true;
	d = false && a>3;
	if ( f[2] > 1 )
	{
		x = 'c';
	}
	return (b==1);
}
void pruebaAccio(int param1, int* param4)
{
	//Variable definitions
		int x, m;
		bool k;
		int param2;
	while ( param2 < *param4 )
	{
		param2 = param2+1;
	}
	switch (x)
	{
		case 1:
			if ( x > m )
			{
				x = 3;
			}
			x = 2;
			m = 9;
			if ( !(m + 2) == 3 )
			{
				x = 9;
			}
			break;
		case 43:
			m = 45;
			break;
	}
	pruebaFunc(&(*param4), &( m), &( k));
	if ( (!k) && !(k == 2) || (!(!(  k==9))) )
	{
		x = 2;
	}
	else
	{
		x = 3;
	}
}
int main()
{
	//Variable definitions
		int a, k, p, b, x;
		bool e;
	while ( (a == 3 && b == 4) || !(x == b) )
	{
		a = 34;
		e = (!k) && !(k == 2) || (!(!(  k==9)));
		a = 34;
		k = 34;
	}
	pruebaAccio(a, &( k));
	p = 90;
	if ( !(pruebaFunc(&(x), &( k), &( a))) )
	{
		a = 43+32;
	}
	else
	{
		a = 80;
	}
	switch (a)
	{
		case 3455:
			a = 9;
			break;
		case 23:
			b = 90;
			break;
		default:
			x = 78;
			k = ((x + 2)*89) /a;
			break;
	}
}
