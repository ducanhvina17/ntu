#include "main.h"

/*
* Best case		:	Theta(n log n)	Input sorted
* Worse case	:	Thetha(n^2)	Input reversely sorted
* Average case	:	Thetha(n log n)
*/
int Partition(int arr[], int left, int right)
{
	int mid = (left + right) / 2;
	Swap(arr, left, mid);

	int pivot = left;
	int last = left;

	for (int i = left + 1; i <= right; i++)
	{
		if (arr[i] < arr[pivot])
		{
			last++;
			Swap(arr, i, last);
		}
	}
	
	Swap(arr, pivot, last);

	return last;
}

void Quicksort(int arr[], int left, int right)
{
	if (left >= right)
		return;
	
	int pivot = Partition(arr, left, right);
	Quicksort(arr, left, pivot - 1);
	Quicksort(arr, pivot + 1, right);
}

void RunQuicksort(int arr[], int n)
{	
	Quicksort(arr, 0, n - 1);

	cout << endl;
	cout << "------ Quicksort ------" << endl;
	for (int i = 0; i < n; i++)
		cout << arr[i] << ", ";
	cout << endl;
}
