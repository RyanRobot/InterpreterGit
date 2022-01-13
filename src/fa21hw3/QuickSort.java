package fa21hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QuickSort {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter filename: ");
		String filename = input.nextLine();
		int[] array = readArrayFromFile(filename);
		System.out.println("Array:");
		for(int x = 0; x < array.length; x++)
		{
			System.out.println(array[x]);
		}
		quickSort(array,0,-1);
		System.out.println("QuickSorted:");
		for(int x = 0; x < array.length; x++)
		{
			System.out.println(array[x]);
		}
		input.close();
	}

	// QuickSort method
	// parameters: int[] array to be sorted
	// int start to mark the beginning of the array (defualt to 0, used in recursion)
	// int end to mark the end of the array (default to -1 then changed to array.length, used in recursion
	public static void quickSort(int[] array, int start, int end)
	{
		// assume start and end are 0,-1 to sort the whole array
		// or else start and end are the indexes of the start and end of the array
		if(end == -1)
		{
			end = array.length;
		}
		int arrayLength = end - start;
		// recursion base case
		if(arrayLength <= 1)
		{
			return;
		}
		// initialize pivot to the first value of the list,
		// and the pointers to the first value above the pivot and the current search comparison
		int pivot = array[start];
		int pivotIndex = start;
		int firstBiggerValueIndex = -1;
		int currentIndex = start + 1;
		// while we haven't finished sorting based on pivot
		while(currentIndex < end)
		{
			// check if we found our first value above the pivot to make switches
			if(array[currentIndex] > pivot && firstBiggerValueIndex == -1)
			{
				firstBiggerValueIndex = currentIndex;
			}
			// if we have a value above the pivot to switch and find a value below the pivot later,
			// we need to make the switch here.
			if(array[currentIndex] < pivot && firstBiggerValueIndex != -1)
			{
				// make the swap
				int temp = array[firstBiggerValueIndex];
				array[firstBiggerValueIndex] = array[currentIndex];
				array[currentIndex] = temp;
				// Change the pointer to the first big value which has now been moved here
				while(firstBiggerValueIndex < end && array[firstBiggerValueIndex] < pivot)
				{
					firstBiggerValueIndex++;
				}
				if(firstBiggerValueIndex > currentIndex)
				{
					currentIndex = firstBiggerValueIndex;
				}
			}
			currentIndex++;
		}
		// find the index of the second partition
		int secondPartitionIndex = -1;
		for(int x = start; x < end; x++)
		{
			if(array[x] > pivot)
			{
				secondPartitionIndex = x;
				break;
			}
		}
		// swap the pivot into the correct position if there is a second partition
		if(secondPartitionIndex != -1)
		{
			array[pivotIndex] = array[secondPartitionIndex - 1];
			array[secondPartitionIndex - 1] = pivot;
		}
		// else swap at the end
		else
		{
			array[pivotIndex] = array[end - 1];
			array[end - 1] = pivot;
			secondPartitionIndex = end;
		}
		// now recurse on the smaller half of the array
		// if secondPartitionIndex = start + 1 then the only thing before the second partition is the pivot
		// if secondPartitionIndex = start + 2 there is one value in the first partition to recurse and return itself
		// if secondPartitionIndex >= start + 3 there are two or more values in the first partition to recurse
		if(secondPartitionIndex - start >= 3)
		{
			quickSort(array, start, secondPartitionIndex - 1);
		}
		if(end - secondPartitionIndex >= 2)
		{
			quickSort(array, secondPartitionIndex, end);
		}
	}
	
	// method readArrayFromFile
	// inputs a filename and outputs the array stored in that file
	public static int[] readArrayFromFile(String filename)
	{
		File inFile = new File(filename);
		try
		{
			Scanner input = new Scanner(inFile);
			int size = 0;
			while(input.hasNext())
			{
				size++;
				input.nextLine();
			}
			input.close();
			input = new Scanner(inFile);
			int x = 0;
			int[] array = new int[size];
			while(input.hasNext())
			{
				array[x] = input.nextInt();
				input.nextLine();
				x++;
			}
			input.close();
			return array;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found.");
		}
		return new int[] {};
	}
}