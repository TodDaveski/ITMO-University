#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <float.h>

bool equals(float left, float right, float precision) 
{
	return fabsf(left - right) <= FLT_EPSILON * precision;
}


void swap(float* matrix, int left, int right, int size) 
{
	for (int i = 0; i < size + 1; i++) {
		float temp = matrix[left * (size + 1) + i];
		matrix[left * (size + 1) + i] = matrix[right * (size + 1) + i];
		matrix[right * (size + 1) + i] = temp;
	}
}



int gauss(float* matrix, float* ans, int size, float precision) 
{
	int row = 0, col = 0;
	while (col < size && row < size) {
		col = row;
		while (col < size) {
			int maxcol = row;
			for (int j = row; j < size; j++) {
				if (matrix[j * (size + 1) + col] > matrix[maxcol * (size + 1) + col]) {
					maxcol = j;
				}
			}
			if (equals(matrix[maxcol * (size + 1) + col], 0.0f, precision)) {
				col++;
				continue;
			}
			swap(matrix, row, maxcol, size);
			for (int i = row + 1; i < size; i++) {
				float c = matrix[i * (size + 1) + col] / matrix[row * (size + 1) + col];
				bool zeroline = true;
				matrix[i * (size + 1) + col] = 0.0f;
				for (int j = col + 1; j < size + 1; j++) {
					matrix[i * (size + 1) + j] -= matrix[row * (size + 1) + j] * c;
					if (equals(matrix[i * (size + 1) + j], 0.0f, precision)) {
						matrix[i * (size + 1) + j] = 0.0f;
					}
					else {
						if (j != size) {
							zeroline = false;
						}
					}
				}
				if (zeroline) {
					if (!equals(matrix[i * (size + 1) + size], 0.0f, precision))
						return 0;
				}
			}
			break;
		}
		row++;
	}
	for (row = 0; row < size; row++) {
		bool zeroline = true;
		for (col = 0; col < size; col++) {
			if (!equals(matrix[row * (size + 1) + col], 0.0f, precision))
				zeroline = false;
		}
		if (zeroline)
			return 2;
	}
	for (row = size - 1; row >= 0; row--) {
		ans[row] = matrix[row * (size + 1) + size] / matrix[row * (size + 1) + row];
		for (int j = row - 1; j >= 0; j--) {
			matrix[j * (size + 1) + size] -= ans[row] * matrix[j * (size + 1) + row];
		}
	}
	return 1;
}
int main(int argc, char** argv) 
{
	if (argc != 3) {
		printf("There was no unexpected error, please try again");
		return 1;
	}
	FILE* in = fopen(argv[1], "r");
	if (!in) {
		printf("Not found input file exception");
		fclose(in);
		return 1;
	}
	int n;
	fscanf(in, "%i", &n);
	float* ans = malloc(sizeof(float) * n);
	if (ans == NULL) {
		printf("Memory allocation error");
		fclose(in);
		return 2;
	}
	float* matrix = malloc(sizeof(float) * n * (n + 1));
	if (matrix == NULL) {
		printf("Memory allocation error");
		fclose(in);
		return 2;
	}
	float minimum = FLT_MAX;
	float maximum = FLT_MIN;
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n + 1; j++) {
			if (fscanf(in, "%f", &matrix[i * (n + 1) + j]) != 1) {
				printf("Error reading from file");
				fclose(in);
				free(matrix);
				return 2;
			}
			if (matrix[i * (n + 1) + j] < minimum)
				minimum = matrix[i * (n + 1) + j];
			if (matrix[i * (n + 1) + j] > maximum)
				maximum = matrix[i * (n + 1) + j];
		}
	}
	fclose(in);
	FILE* out = fopen(argv[2], "w");
	if (!out) {
		printf("Error opening output file");
		return 2;
	}
	int res = gauss(matrix, ans, n, maximum - minimum);
	if (res == 0) {
		fprintf(out, "no solution");
	}
	if (res == 1) {
		for (int i = 0; i < n; i++) {
			fprintf(out, "%g\n", ans[i]);
		}
	}
	if (res == 2) {
		fprintf(out, "many solutions");
	}
	free(matrix);
	free(ans);
	fclose(out);
	return 0;
}