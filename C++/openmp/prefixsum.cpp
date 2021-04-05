#include <iostream>
#include <omp.h>
#include <vector>
#include <fstream>
#include <ctime>
using namespace std;

int main(int argc, char* argv[])
{
    int number_of_threads;
    int n;
    if (argc < 3)
    {
        cout << "Not enough data: input file name or number of threads missing";
        return 1;
    }
    try
    {
        number_of_threads = atoi(argv[1]);
    }
    catch (...)
    {
        cout << argv[1] << " isn't a correct integer";
        return 1;
    }
    ifstream in(argv[2]);

    if (!in)
    {
        cout << "File not found";
        return 1;
    }
    vector <float> a, b;
    if (in.is_open())
    {
        in >> n;

        for (int i = 0; i < n; i++) {
            float x;
            in >> x;
            b.push_back(x);
            a.push_back(0.0f);
        }
        in.close();
    }
    else
    {
        cout << argv[2] << " file couldn't be open";
        return 1;
    }

    long afterRead = clock();

    float* sum_thread;
#pragma omp parallel num_threads(number_of_threads)
    {
        int ithread = omp_get_thread_num();    
        int nthreads = number_of_threads; 
#pragma omp single
        {
            sum_thread = new float[nthreads + 1];
            sum_thread[0] = 0;
        }
        float s = 0;
#pragma omp for schedule(static) nowait
        for (int i = 0; i < n; i++)
            s += b[i], a[i] = s;
        sum_thread[ithread + 1] = s;

#pragma omp barrier

        float offset = 0;
        for (int i = 0; i < (ithread + 1); i++)
            offset += sum_thread[i];

#pragma omp for schedule(static)
        for (int i = 0; i < n; i++)
            a[i] += offset;
    }
    
    float timew = (float)(clock() - afterRead) / CLOCKS_PER_SEC;

    if (argc < 4)
    {
        for (int i = 0; i < n; i++)
            cout << a[i] << " ";
    }
    else
    {
        ofstream out(argv[3]);
        if (out.is_open())
        {
            for (int i = 0; i < n; i++)
                out << a[i] << " ";
        }
        out.close();
    }
    cout<<timew;
    return 0;

}

