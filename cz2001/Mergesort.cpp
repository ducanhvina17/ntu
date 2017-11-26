#include "main.h"

/*
* Runtime	:	O(n log n)
*/
void Merge(int arr[], int low, int high)
{
	int leftIndex, i, rightIndex, b[10000], mid = (high + low) / 2;
	leftIndex = low;
	i = low;
	rightIndex = mid + 1;

	while ((leftIndex <= mid) && (rightIndex <= high))
	{
		if (arr[leftIndex] < arr[rightIndex])
		{
			b[i] = arr[leftIndex];
			leftIndex++;
		}
		else if (arr[leftIndex] > arr[rightIndex])
		{
			b[i] = arr[rightIndex];
			rightIndex++;
		}
		else
		{
			b[i] = arr[rightIndex];
			rightIndex++;
			b[++i] = arr[leftIndex];
			leftIndex++;
		}
		i++;
	}

	if (leftIndex > mid)
	{
		for (int k = rightIndex; k <= high; k++)
		{
			b[i] = arr[k];
			i++;
		}
	}
	else
	{
		for (int k = leftIndex; k <= mid; k++)
		{
			b[i] = arr[k];
			i++;
		}
	}

	for (int k = low; k <= high; k++)
		arr[k] = b[k];
}

void Mergesort(int arr[], int low, int high)
{
	if (high - low <= 0)
		return;

	int mid = (low + high) / 2;
	Mergesort(arr, low, mid);
	Mergesort(arr, mid + 1, high);
	Merge(arr, low, high);
}

void RunMergesort(int arr[], int n)
{
	Mergesort(arr, 0, n-1);

	cout << endl;
	cout << "------ Mergesort ------" << endl;
	for (int i = 0; i < n; i++)
		cout << arr[i] << ", ";
	cout << endl;
}
