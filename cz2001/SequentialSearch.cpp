#include "main.h"

/*
* Best case		:	1 comparison
* Worse case	:	n comparisons
* Average case	:	(n + 1) / 2 comparison
*/
int SequentialSearch(int arr[], int n, int key)
{
	for (int i = 0; i < n; i++)
	{
		if (key == arr[i])
			return i;
	}

	return -1;
}

void RunSequentialSearch(int arr[], int n, int key)
{
	int comparison = 0;

	cout << endl;
	cout << "------ Sequential Search ------" << endl;
	cout << "Item " << key << " is at index " <<
		SequentialSearch(arr, n, key) << endl;
}