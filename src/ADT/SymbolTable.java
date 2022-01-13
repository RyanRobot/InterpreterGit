package ADT;

import java.io.FileWriter;
import java.io.IOException;

// Ryan Johnson
// 09/25/21

// Class SymbolTable to store symbols (variables, constants, and labels)
// in a table format
public class SymbolTable {
	// list of symbols and kinds
	String symbols[];
	char kinds[];
	// three lists for the values depending on the data type
	int valuesInts[];
	String valuesStrings[];
	double valuesDoubles[];
	// list of datatypes used as well as the size of the table and the index of the next new symbol
	char dataTypes[];
	int size;
	int nextNew = 0;
	
	// constructor with size input
	public SymbolTable(int size)
	{
		this.size = size;
		symbols = new String[size];
		kinds = new char[size];
		valuesInts = new int[size];
		valuesDoubles = new double[size];
		valuesStrings = new String[size];
		dataTypes = new char[size];
		AddSymbol("0",'c', 0);
	}
	
	// AddSymbol overloaded function
	// Adds a symbol and its kind and value to the table
	
	// version with integer value:
	public int AddSymbol(String symbol, char kind, int value)
	{
		// check to make sure we don't add two copies of the same symbol
		int existingIndex = LookupSymbol(symbol);
		if(existingIndex != -1)
		{
			return existingIndex;
		}
		// check that we have room for a new symbol
		if(nextNew <= size)
		{
			// add data
			symbols[nextNew] = symbol;
			kinds[nextNew] = kind;
			valuesInts[nextNew] = value;
			dataTypes[nextNew] = 'i';
			nextNew++;
			return nextNew - 1;
		}
		return -1;
	}
	
	// version with string value
	public int AddSymbol(String symbol, char kind, String value)
	{
		// check to make sure we don't add two copies of the same symbol
		int existingIndex = LookupSymbol(symbol);
		if(existingIndex != -1)
		{
			return existingIndex;
		}
		// check that we have room for a new symbol
		if(nextNew <= size)
		{
			// add data
			symbols[nextNew] = symbol;
			kinds[nextNew] = kind;
			valuesStrings[nextNew] = value;
			dataTypes[nextNew] = 's';
			nextNew++;
			return nextNew - 1;
		}
		return -1;
	}
	
	// version with double value
	public int AddSymbol(String symbol, char kind, double value)
	{
		// check to make sure we don't add two copies of the same symbol
		int existingIndex = LookupSymbol(symbol);
		if(existingIndex != -1)
		{
			return existingIndex;
		}
		// check that we have room for a new symbol
		if(nextNew <= size)
		{
			// add data
			symbols[nextNew] = symbol;
			kinds[nextNew] = kind;
			valuesDoubles[nextNew] = value;
			dataTypes[nextNew] = 'd';
			nextNew++;
			return nextNew - 1;
		}
		return -1;
	}
	
	// LookupSymbol returns index of the symbol you're searching for
	// also used to make sure we don't add duplicate symbols
	public int LookupSymbol(String symbol)
	{
		for(int x = 0; x < nextNew; x++)
		{
			if(symbol.compareToIgnoreCase(symbols[x]) == 0)
			{
				return x;
			}
		}
		return -1;
	}
	
	// GetSymbol returns symbol at index
	public String GetSymbol(int index)
	{
		return symbols[index];
	}
	
	// GetKind returns kind at index
	public char GetKind(int index)
	{
		return kinds[index];
	}
	
	// GetDataType returns the datatype at index
	public char GetDataType(int index)
	{
		return dataTypes[index];
	}
	
	// GetString returns the value at index if it's a string
	public String GetString(int index)
	{
		if(dataTypes[index] == 's')
		{
			return valuesStrings[index];
		}
		return "ERROR_WRONG_DATATYPE_IN_SYMBOL_TABLE";
	}
	
	// GetInteger returns the value at index if it's an integer
	public int GetInteger(int index)
	{
		if(dataTypes[index] == 'i')
		{
			return valuesInts[index];
		}
		return -1;
	}
	
	// GetDouble returns the value at index if it's a double
	public double GetDouble(int index)
	{
		if(dataTypes[index] == 'd')
		{
			return valuesDoubles[index];
		}
		return -1.0;
	}
	
	// UpdateSymbol changes the data stored at index with a new kind and value
	
	// int value version
	public void UpdateSymbol(int index, char kind, int value)
	{
		kinds[index] = kind;
		valuesInts[index] = value;
		dataTypes[index] = 'i';
	}
	
	// string value version
	public void UpdateSymbol(int index, char kind, String value)
	{
		kinds[index] = kind;
		valuesStrings[index] = value;
		dataTypes[index] = 's';
	}
	
	// double value version
	public void UpdateSymbol(int index, char kind, double value)
	{
		kinds[index] = kind;
		valuesDoubles[index] = value;
		dataTypes[index] = 'd';
	}
	
	// PrintSymbolTable prints out the symbol table to a file
	public void PrintSymbolTable(String fileName)
	{
		final String FIRSTLINE = "Index\t|\tSymbols\t\t\t|\tKind\t|\tType\t|\tValue\n";
		final String SECONDLINE = "--------|-------------------------------|---------------|---------------|------------\n";
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
				if(symbols[x] != null)
				{
					// Generate the line to be outputed in the format
					// index (tab) | (tab) {name in size 20} (tab) | (tab) value
					String nextLine = Integer.toString(x + 1);
					nextLine += "\t|\t";
					nextLine += symbols[x];
					for(int y = 0; y < 2 - (symbols[x].length() / 8); y++)
					{
						nextLine += "\t";
					}
					nextLine += "\t|\t";
					nextLine += kinds[x];
					nextLine += "\t|\t";
					nextLine += dataTypes[x];
					nextLine += "\t|\t";
					if(dataTypes[x] == 'i')
					{
						nextLine += valuesInts[x];
					}
					if(dataTypes[x] == 's')
					{
						nextLine += valuesStrings[x];
					}
					if(dataTypes[x] == 'd')
					{
						nextLine += valuesDoubles[x];
					}
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
