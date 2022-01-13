package ADT;

import java.io.FileWriter;
import java.io.IOException;

public class QuadTable {
	int size;
	int next = 0;
	int opcodes[];
	int op1s[];
	int op2s[];
	int op3s[];
	
	public QuadTable(int maxSize)
	{
		size = maxSize;
		opcodes = new int[size];
		op1s = new int[size];
		op2s = new int[size];
		op3s = new int[size];
	}
	
	public int NextQuad()
	{
		return next;
	}
	
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
	
	public void UpdateQuad(int index, int opcode, int op1, int op2, int op3)
	{
		opcodes[index] = opcode;
		op1s[index] = op1;
		op2s[index] = op2;
		op3s[index] = op3;
	}
	
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
