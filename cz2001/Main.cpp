#include <fstream>
#include <sstream>
#include <string>
#include "main.h"

using namespace std;

void ReadFile(string file, int arr[]);

int main()
{
	int input, order, size, n;
	int arr[10000];
	string file;

	cout << "1. Sequential Search\n2. Binary Search\n3. Insertion Sort\n4. Quicksort\n5. Mergesort\nChoose an option : ";
	cin >> input;

	if (input == 1 || input == 2)
	{
		cout << "Enter array size : ";
		cin >> size;

		cout << "Enter data : ";
		int i = 0;
		while (i < size)
		{
			cin >> arr[i];
			i++;
		}

		int key;
		cout << "Enter key to be searched : ";
		cin >> key;

		if (input == 1)
			RunSequentialSearch(arr, size, key);
		else if (input == 2)
			RunBinarySearch(arr, size, key);
	}
	else
	{
		cout << endl << "1. Ascending order\n2. Descending order\n3. Random\nChoose an option : ";
		cin >> order;

		cout << endl << "1. n = 2000\n2. n = 4000\n3. n = 6000\n4. n = 8000\n5. n = 10000\nChoose an option : ";
		cin >> size;

		// Order of array items
		switch (order)
		{
			case 1: file = "asc";
				break;
			case 2: file = "desc";
				break;
			case 3: file = "rand";
				break;
		}

		// Array size
		switch (size)
		{
			case 1:
				file += "2000.txt";
				n = 2000;
				break;
			case 2:file += "4000.txt";
				n = 4000;
				break;
			case 3:file += "6000.txt";
				n = 6000;
				break;
			case 4:file += "8000.txt";
				n = 8000;
				break;
			case 5:file += "10000.txt";
				n = 10000;
				break;
		}

		// Read data from text file
		ReadFile(file, arr);

		switch (input)
		{
		case 3:
			RunInsertionSort(arr, n);
			break;
		case 4:
			RunQuicksort(arr, n);
			break;
		case 5:
			RunMergesort(arr, n);
			break;
		}
	}	
}

void ReadFile(string file, int arr[])
{
	string line;

	// Get string from text file
	ifstream myfile(file);
	if (myfile.is_open())
	{
		getline(myfile, line);
		myfile.close();
	}

	stringstream  lineStream(line);
	int value, i = 0;

	// Read an integer at a time from the line. Each number is seperated by space.
	while (lineStream >> value)
	{
		arr[i] = value;
		i++;
	}
}
