#include "main.h"

/*
* Best case		:	Theta(n)	Input sorted
* Worse case	:	Thetha(n^2)	Input reversely sorted
* Average case	:	Thetha(n^2)
*/
void InsertionSort(int arr[], int n)
{
	for (int i = 1; i < n; i++)
	{
		for (int j = i; j > 0; j--)
		{
			if (arr[j] < arr[j - 1])
				Swap(arr, j, j - 1);
			else
				break;
		}
	}
}

void RunInsertionSort(int arr[], int n)
{
	InsertionSort(arr, n);

	cout << endl;
	cout << "------ Quicksort ------" << endl;
	for (int i = 0; i < n; i++)
		cout << arr[i] << ", ";
	cout << endl;
}