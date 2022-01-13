package ADT;

import java.io.FileWriter;
import java.io.IOException;

// Ryan Johnson
// 09/25/21

// QuadTable class
// Contains the opcode and 3 operands for each line of code
public class QuadTable {
	// size is the maximum size and next is where the next quad will be added (current size)
	int size;
	int next = 0;
	// lists for the opcodes and operands
	int opcodes[];
	int op1s[];
	int op2s[];
	int op3s[];
	
	// constructor with size
	public QuadTable(int maxSize)
	{
		size = maxSize;
		opcodes = new int[size];
		op1s = new int[size];
		op2s = new int[size];
		op3s = new int[size];
	}
	
	// NextQuad returns the index of the next quad to be added 
	public int NextQuad()
	{
		return next;
	}
	
	// AddQuad adds a new quad with all relevant data
	public void AddQuad(int opcode, int op1, int op2, int op3)
	{
		if(next < size)
		{
			opcodes[next] = opcode;
			op1s[next] = op1;
			op2s[next] = op2;
			op3s[next] = op3;
			next++;
		}
	}
	
	// GetQuad returns the one integer stored at index and column
	// column = 0,1,2,3 returns opcode,op1,op2,op3
	public int GetQuad(int index, int column)
	{
		if(column == 0)
		{
			return opcodes[index];
		}
		if(column == 1)
		{
			return op1s[index];
		}
		if(column == 2)
		{
			return op2s[index];
		}
		if(column == 3)
		{
			return op3s[index];
		}
		return -1;
	}
	
	// UpdateQuad changes a quad at index with new data
	public void UpdateQuad(int index, int opcode, int op1, int op2, int op3)
	{
		opcodes[index] = opcode;
		op1s[index] = op1;
		op2s[index] = op2;
		op3s[index] = op3;
	}
	
	// PrintQuadTable prints out the QuadTable to a file
	public void PrintQuadTable(String fileName)
	{
		final String FIRSTLINE = "Index\t|\tOpcode\t|\tOp1\t|\tOp2\t|\tOp3\n";
		final String SECONDLINE = "--------|---------------|---------------|---------------|-----------\n";
		// Try in case of IOException
		try
		{
			// Initiate FileWriter to write to the file and begin with the first line and second lines
			FileWriter writer = new FileWriter(fileName);
			writer.write(FIRSTLINE);
			writer.write(SECONDLINE);
			// Print each line in the table
			for(int x = 0; x < next; x++)
			{
				// Make sure there's an existing variable for each output
				// Generate the line to be outputed in the format
				// index (tab) | (tab) {name in size 20} (tab) | (tab) value
				String nextLine = Integer.toString(x + 1);
				nextLine += "\t|\t";
				nextLine += opcodes[x];
				nextLine += "\t|\t";
				nextLine += op1s[x];
				nextLine += "\t|\t";
				nextLine += op2s[x];
				nextLine += "\t|\t";
				nextLine += op3s[x];
				nextLine += "\n";
				// Output the line to the file
				writer.write(nextLine);
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
