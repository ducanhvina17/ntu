#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;


int main()
{
	ofstream myfile;

	myfile.open("rand10000.txt", std::ios_base::app);

	for (int i = 10000; i >= 1; i--)
		myfile << (1 + (rand() % (int)(10000))) << " ";

	myfile.close();
	return 0;
}
