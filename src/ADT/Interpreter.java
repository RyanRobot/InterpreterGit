package ADT;

import ADT.QuadTable;
import ADT.ReserveTable;
import ADT.SymbolTable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Ryan Johnson
// Class to run assembly code as an interpreter using a reserve table for the opcodes,
// a quad table to store the program, and a symbol table to store variables, constants, and labels.

public class Interpreter
{
	// ReserveTable to hold opcodes and their meanings
	ReserveTable reserve;
	
	// constructor without parameters
	public Interpreter()
	{
		// construct a reserve table with 16 slots
		reserve = new ReserveTable(16);
		
		// initialize all our opcodes
		reserve.Add("STOP", 0);
		reserve.Add("DIV", 1);
		reserve.Add("MUL", 2);
		reserve.Add("SUB", 3);
		reserve.Add("ADD", 4);
		reserve.Add("MOV", 5);
		reserve.Add("PRINT", 6);
		reserve.Add("READ", 7);
		reserve.Add("BR", 8);
		reserve.Add("BINDR", 9);
		reserve.Add("BZ", 10);
		reserve.Add("BP", 11);
		reserve.Add("BN", 12);
		reserve.Add("BNZ", 13);
		reserve.Add("BNP", 14);
		reserve.Add("BNN", 15);
	}
	
	// method to initialize factorial program
	public boolean initializeFactorialTest(SymbolTable stable, QuadTable qtable)
	{
		// initialize symbol table
		stable.AddSymbol("n",'v',10);
		stable.AddSymbol("i",'v',0);
		stable.AddSymbol("product",'v',0);
		stable.AddSymbol("1",'c',1);
		stable.AddSymbol("$temp",'v',0);
		
		// initialize quad table
		qtable.AddQuad(5, 3, 0, 2);
		qtable.AddQuad(5, 3, 0, 1);
		qtable.AddQuad(3, 1, 0, 4);
		qtable.AddQuad(11, 4, 0, 7);
		qtable.AddQuad(2, 2, 1, 2);
		qtable.AddQuad(4, 1, 3, 1);
		qtable.AddQuad(8, 0, 0, 2);
		qtable.AddQuad(6, 2, 0, 0);
		
		return true;
	}
	
	// method to initialize summation program
	public boolean initializeSummationTest(SymbolTable stable, QuadTable qtable)
	{
		// initialize symbol table
		stable.AddSymbol("n",'v',10);
		stable.AddSymbol("i",'v',0);
		stable.AddSymbol("sum",'v',0);
		stable.AddSymbol("1",'c',1);
		stable.AddSymbol("$temp",'v',0);
		
		// initialize quad table
		qtable.AddQuad(5, 3, 0, 2);
		qtable.AddQuad(5, 3, 0, 1);
		qtable.AddQuad(3, 1, 0, 4);
		qtable.AddQuad(11, 4, 0, 7);
		qtable.AddQuad(4, 2, 1, 2);
		qtable.AddQuad(4, 1, 3, 1);
		qtable.AddQuad(8, 0, 0, 2);
		qtable.AddQuad(6, 2, 0, 0);
		
		return true;
	}
	
	
	// method InterpretQuads runs a function
	public void InterpretQuads(QuadTable Q, SymbolTable S, boolean TraceOn, String filename)
	{
		Scanner input = new Scanner(System.in);
		FileWriter output = null;
		try
		{
			output = new FileWriter(filename);
		}
		catch(IOException e)
		{
			System.out.println("ERROR: FILE <" + filename + "> NOT OPENED, EXCEPTION: \n\t" + e.toString());
		}
		// loop through quad table and run program
		// PC will change on branches so we don't have to worry about the for loop being short the whole program
		for(int PC = 0; PC <= Q.NextQuad(); PC++)
		{
			// initialize the operation string and all three operands
			String op = reserve.LookupCode(Q.GetQuad(PC, 0));
			int op1 = Q.GetQuad(PC, 1);
			int op2 = Q.GetQuad(PC, 2);
			int op3 = Q.GetQuad(PC, 3);
			
			// print command to file and standard out if traceon
			if(TraceOn)
			{
				String message = "PC = ";
				message += Integer.toString(PC);
				message += ": ";
				message += op;
				message += " ";
				message += Integer.toString(op1);
				message += " ";
				message += Integer.toString(op2);
				message += " ";
				message += Integer.toString(op3);
				try
				{
					try
					{
						output.write(message + "\n");
					}
					catch(NullPointerException e)
					{
						
					}
				}
				catch(IOException e)
				{
					
				}
				// print to standard out
				System.out.println(message);
			}
			// end printing if traceon
			
			// run the appropriate command
			// use switch to interpret each command
			switch(op)
			{
			// stop
			case "STOP":
				PC = Q.NextQuad();
				break;
			
			// MATH
				
			// div
			case "DIV":
				if(S.GetDataType(op1) == 'i')
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) / S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) / S.GetDouble(op2)));
					}
				}
				else
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) / S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) / S.GetDouble(op2)));
					}
				}
				break;
			// mul
			case "MUL":
				if(S.GetDataType(op1) == 'i')
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) * S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) * S.GetDouble(op2)));
					}
				}
				else
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) * S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) * S.GetDouble(op2)));
					}
				}
				break;
			// sub
			case "SUB":
				if(S.GetDataType(op1) == 'i')
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) - S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) - S.GetDouble(op2)));
					}
				}
				else
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) - S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) - S.GetDouble(op2)));
					}
				}
				break;
			// add
			case "ADD":
				if(S.GetDataType(op1) == 'i')
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) + S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetInteger(op1) + S.GetDouble(op2)));
					}
				}
				else
				{
					if(S.GetDataType(op2) == 'i')
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) + S.GetInteger(op2)));
					}
					else
					{
						S.UpdateSymbol(op3, 'v', (S.GetDouble(op1) + S.GetDouble(op2)));
					}
				}
				break;
			
			// mov
			case "MOV":
				// make sure we move the correct datatype
				switch(S.GetDataType(op1))
				{
				// integer
				case 'i':
					S.UpdateSymbol(op3, 'v', S.GetInteger(op1));
					break;
				// string
				case 's':
					S.UpdateSymbol(op3, 'v', S.GetString(op1));
					break;
				// double
				case 'd':
					S.UpdateSymbol(op3, 'v', S.GetDouble(op1));
					break;
				}
				break;
			// end mov
			
			// print
			case "PRINT":
				// set name to the name of the symbol to print and initialize value that will be the symbol's value
				String name = S.GetSymbol(op1);
				String value = "";
				// find the value's datatype and set our value string to the correct value
				switch(S.GetDataType(op1))
				{
				// integer
				case 'i':
					value = Integer.toString(S.GetInteger(op1));
					break;
				// double
				case 'd':
					value = Double.toString(S.GetDouble(op1));
					break;
				// string
				case 's':
					value = S.GetString(op1);
					break;
				}
				// print to standard out
				System.out.println(name + ": " + value);
				// attempt to print to file (if traceon):
				if(TraceOn)
				{
					try
					{
						try
						{
							output.write(name + ": " + value + "\n");
						}
						catch(NullPointerException e)
						{
							
						}
					}
					catch(IOException e)
					{
						
					}
				}
				break;
			// end print
			// read
			case "READ":
				S.UpdateSymbol(op3, 'v', input.nextInt());
				break;
				
			// BRANCHES
				
			// branch
			case "BR":
				PC = op3 - 1;
				break;
			// branch to label
			case "BINDR":
				PC = S.GetInteger(op3) - 1;
				break;
			// branch zero
			case "BZ":
				if(S.GetInteger(op1) == 0)
				{
					PC = S.GetInteger(op3) - 1;
				}
				break;
			// branch positive
			case "BP":
				if(S.GetInteger(op1) > 0)
				{
					PC = S.GetInteger(op3) - 1;
				}
				break;
			// branch negative
			case "BN":
				if(S.GetInteger(op1) < 0)
				{
					PC = S.GetInteger(op3) - 1;
				}
				break;
			// branch not zero
			case "BNZ":
				if(S.GetInteger(op1) != 0)
				{
					PC = S.GetInteger(op3) - 1;
				}
				break;
			// branch not positive
			case "BNP":
				if(S.GetInteger(op1) <= 0)
				{
					PC = S.GetInteger(op3) - 1;
				}
				break;
			// branch not negative
			case "BNN":
				if(S.GetInteger(op1) >= 0)
				{
					PC = S.GetInteger(op3) - 1;
				}
				break;
			}
		}
		// close our input and output
		input.close();
		try
		{
			try
			{
				output.close();
			}
			catch(NullPointerException e)
			{
				
			}
		}
		catch(IOException e)
		{
			
		}
	}
}
