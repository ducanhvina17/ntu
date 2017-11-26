#include "main.h"

/*
* Average and worst case	:	theta(log n)
*/
int BinarySearch(int arr[], int first, int last, int key)
{
	if (last < first)
		return -1;

	int mid = (last + first) / 2;

	if (key == arr[mid])
		return mid;
	
	return key < arr[mid] ?
		BinarySearch(arr, first, mid - 1, key) :
		BinarySearch(arr, mid + 1, last, key);
}

void RunBinarySearch(int arr[], int n, int key)
{
	int comparison = 0;

	cout << endl;
	cout << "------ Binary Search ------" << endl;
	cout << "Item " << key << " is at index " <<
		BinarySearch(arr, 0, n - 1, key) << endl;
}