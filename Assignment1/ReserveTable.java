package ADT;

import java.io.IOException;
import java.io.FileWriter;

// class ReserveTable to hold names and values of variables
// and allow  lookup of those names and values
public class ReserveTable {
	// names and values will be the two lists that hold our data
	private String[] names;
	private int[] values;
	// maxSize is the maximum number of entries in our table,
	// for both names and values
	private int maxSize;
	// nextNew doubles as both how many entries are currently in the table
	// and where we make our next new entry
	private int nextNew = 0;
	
	// Constructor for a given size of the reserve table
	public ReserveTable(int size)
	{
		maxSize = size;
		values = new int[maxSize];
		names = new String[maxSize];
	}
	
	// Add method to add a new variable will both name and value to the table
	public void Add(String name, int value)
	{
		// Do not exceed table size
		if(nextNew < maxSize)
		{
			// Add data
			names[nextNew] = name;
			values[nextNew] = value;
			// Increment our current size counter
			nextNew++;
		}
	}
	
	// Method LookupName to find the corresponding value of a varaiable name
	public int LookupName(String name)
	{
		// Search through names in the table using x to count through all entries
		for(int x = 0; x < nextNew; x++)
		{
			// Check if we found the name
			if(names[x].compareToIgnoreCase(name) == 0)
			{
				// Return the corresponding value
				return values[x];
			}
		}
		// -1 as our default if name not found
		return -1;
	}
	
	// Method LookupCode to find the first name that corresponds to a value in the table
	public String LookupCode(int code)
	{
		// Search through values in the table using x to count through all entries
		for(int x = 0; x < nextNew; x++)
		{
			// Check if we found the code
			if(values[x] == code)
			{
				// Return the corresponding name
				return names[x];
			}
		}
		// Default if code not found
		return "ERROR_RESERVE_TABLE_CODE_NOT_FOUND";
	}
	
	// Method PrintReserveTable to output the table to a text file
	public void PrintReserveTable(String fileName)
	{
		// Constant variables for maximum variable name and the first line of the table
		final int VARIABLENAMEMAXSIZE = 20;
		final String FIRSTLINE = "Index\t|\tName               \t|\tValue\n";
		final String SECONDLINE = "--------|-------------------------------|------------\n";
		// Try in case of IOException
		try
		{
			// Initiate FileWriter to write to the file and begin with the first line and second lines
			FileWriter writer = new FileWriter(fileName);
			writer.write(FIRSTLINE);
			writer.write(SECONDLINE);
			// Print each line in the table
			for(int x = 0; x < nextNew; x++)
			{
				// Make sure there's an existing variable for each output
				if(names[x] != null)
				{
					// Generate the line to be outputed in the format
					// index (tab) | (tab) {name in size 20} (tab) | (tab) value
					String nextLine = Integer.toString(x + 1);
					nextLine += "\t|\t";
					nextLine += names[x];
					for(int y = 0; y < VARIABLENAMEMAXSIZE - names[x].length(); y++)
					{
						nextLine += " ";
					}
					nextLine += "\t|\t";
					nextLine += values[x];
					nextLine += "\n";
					// Output the line to the file
					writer.write(nextLine);
				}
			}
			// Close the FileWriter now that the output is finished
			writer.close();
		}
		// catch in case of IOException
		catch (IOException e)
		{
			
		}
	}
}
