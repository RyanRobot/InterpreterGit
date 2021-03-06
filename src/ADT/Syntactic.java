
/*
SAMPLE syntactic CODE FOR FALL 21 Compiler Class.

See TEMPLATE at end of this file for the framework to be used for
ALL non-terminal methods created.
Two methods shown below are added to LEXICAL, where reserve and mnemonic
tables are accessible:
 
public int codeFor(String mnemonic){
    return mnemonics.LookupName(mnemonic);
}
public String reserveFor(String mnemonic){
    return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
}
public void setPrintToken(boolean on){
    printToken = on;
}
Add 2 lines which prints each token found by GetNextToken:
            if (printToken) {
                System.out.println("\t" + result.mnemonic + " | \t" + 
String.format("%04d", result.code) + " | \t" + result.lexeme);
            }
 */
package ADT;
/**
 * 
 * @author abrouill
 */
// Ryan Johnson's assignment
public class Syntactic {
    private String filein;              //The full file path to input file
    private SymbolTable symbolList;     //Symbol table storing ident/const
    private Lexical lex;                //Lexical analyzer 
    private Lexical.token token;        //Next Token retrieved 
    private boolean traceon;            //Controls tracing mode 
    private int level = 0;              //Controls indent for trace mode
    private boolean anyErrors;          //Set TRUE if an error happens
    private QuadTable quads;			//Quad table
    private Interpreter interp;
    private int maxQuads = 1000;
    private final int symbolSize = 250;
    int currentTemp = 0;
    int currentLabel = 0;
    public Syntactic(String filename, boolean traceOn) {
        filein = filename;
        traceon = traceOn;
        symbolList = new SymbolTable(symbolSize);
        lex = new Lexical(filein, symbolList, true);
        lex.setPrintToken(traceOn);
        quads = new QuadTable(maxQuads);
        anyErrors = false;
        interp = new Interpreter();
    }
    
    public void printQuads(String filename)
    {
    	quads.PrintQuadTable(filename);
    }
    
    public void run(String filename)
    {
    	if(!anyErrors)
    	{
	    	if(traceon)
	    	{
	    		symbolList.PrintSymbolTable("symbols1" + filename);
	    	}
	    	interp.InterpretQuads(quads, symbolList, traceon, filename);
	    	if(traceon)
	    	{
	    		symbolList.PrintSymbolTable("symbols2" + filename);
	    	}
    	}
    }
    
//The interface to the syntax analyzer, initiates parsing
// Uses variable RECUR to get return values throughout the non-terminal methods    
    public void parse() {
        int recur = 0;
// prime the pump
        token = lex.GetNextToken();
// call PROGRAM
        recur = Program();
    }
//Non Terminal     
    private int ProgIdentifier() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        // This non-term is used to uniquely mark the program identifier
        if (token.code == lex.codeFor("IDNT")) {
            // Because this is the progIdentifier, it will get a 'p' type to 
        	//prevent re-use as a var
            symbolList.UpdateSymbol(symbolList.LookupSymbol(token.lexeme), 'p', 0);
            //move on
            token = lex.GetNextToken();
        }
        return recur;
    }
//Non Terminals
    private int Program() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Program", true);
        if (token.code == lex.codeFor("MODL")) {
            token = lex.GetNextToken();
            recur = ProgIdentifier();
            if (token.code == lex.codeFor("SEMI")) {
                token = lex.GetNextToken();
                recur = Block();
                if (token.code == lex.codeFor("PERD")) {
                    if (!anyErrors) {
                        System.out.println("Success.");
                    } else {
                        System.out.println("Compilation failed.");
                    }
                } else {
                    error(lex.reserveFor("PERD"), token.lexeme);
                }
            } else {
                error(lex.reserveFor("SEMI"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("MODL"), token.lexeme);
        }
        trace("Program", false);
        return recur;
    }
//Non Terminal    
    private int Block() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Block", true);
        if (token.code == lex.codeFor("BGIN")) {
            token = lex.GetNextToken();
            recur = Statement();
            while ((token.code == lex.codeFor("SEMI")) && (!lex.EOF()) && (!
anyErrors)) {
                token = lex.GetNextToken();
                recur = Statement();
            }
            if (token.code == lex.codeFor("END_")) {
                token = lex.GetNextToken();
            } else {
                error(lex.reserveFor("END_"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("BGIN"), token.lexeme);
        }
        trace("Block", false);
        return recur;
    }
//Not a NT, but used to shorten Statement code body   
    //<variable> $COLON-EQUALS <simple expression>
    private int handleAssignment() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("handleAssignment", true);
        //have ident already in order to get to here, handle as Variable
        int var = Variable();  //Variable moves ahead, next token ready
        if (token.code == lex.codeFor("ASGN")) {
            token = lex.GetNextToken();
            recur = SimpleExpression();
        } else {
            error(lex.reserveFor("ASGN"), token.lexeme);
        }
        quads.AddQuad(5, recur, 0, var);
        trace("handleAssignment", false);
        return var;
    }
// NT This is dummied in to only work for an identifier.  MUST BE 
//  COMPLETED TO IMPLEMENT CFG <simple expression>
    private int SimpleExpression() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("SimpleExpression", true);
        // first we recurse down into term as we always expect simple expressions to start with a term
        recur = Term();
        // next we check for a + or - that would signal a continuation of the simple expression
        if(token.code == lex.codeFor("PLUS") || token.code == lex.codeFor("MINS"))
        {
        	int operation = 3;
        	if(token.code == lex.codeFor("PLUS"))
        	{
       			operation = 4;
       		}
        	int op1 = recur;
        	// let's just recurse back into simple expression again since that's the easiest way to handle the rest after a + or -
        	token = lex.GetNextToken();
        	int op2 = SimpleExpression();
        	recur = symbolList.AddSymbol("$temp" + String.valueOf(currentTemp), 'V', 0);
        	quads.AddQuad(operation, op1, op2, recur);
        }
        trace("SimpleExpression", false);
        return recur;
    }
//Non Terminal    
    private int Statement() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Statement", true);
        if (token.code == lex.codeFor("IDNT")) {  //must be an ASSUGNMENT
            recur = handleAssignment();
        } else {
            if (token.code == lex.codeFor("_IF_")) {  //must be an ASSUGNMENT
                // this would handle the rest of the IF statment IN PART B
            	recur = ifStatement();
            } else // if/elses should look for the other possible statement 
            		//starts...  
            //  but not until PART B
            	if(token.code == lex.codeFor("WRTN"))
            	{
            		printStatement();
            	}
            	else
            	{
            		if(token.code == lex.codeFor("WHLE"))
            		{
            			whileStatement();
            		}
            		else
            		{
            			if(token.code == lex.codeFor("UNTL"))
            			{
            				untilStatement();
            			}
            			else
            			{
            				error("Statement start", token.lexeme);
            			}
            		}
            	}
        }
        trace("Statement", false);
        return recur;
    }
/**
 * *************************************************
*/
    /*     UTILITY FUNCTIONS USED THROUGHOUT THIS CLASS */
// error provides a simple way to print an error statement to standard output
//     and avoid reduncancy
    private void error(String wanted, String got) {
        anyErrors = true;
        System.out.println("ERROR: Expected " + wanted + " but found " + got);
    }
// trace simply RETURNs if traceon is false; otherwise, it prints an
    // ENTERING or EXITING message using the proc string
    private void trace(String proc, boolean enter) {
        String tabs = "";
        if (!traceon) {
            return;
        }
        if (enter) {
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("--> Entering " + proc);
            level++;
        } else {
            if (level > 0) {
                level--;
            }
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("<-- Exiting " + proc);
        }
    }
// repeatChar returns a string containing x repetitions of string s; 
//    nice for making a varying indent format
    private String repeatChar(String s, int x) {
        int i;
        String result = "";
        for (i = 1; i <= x; i++) {
            result = result + s;
        }
        return result;
    }
/*  Template for all the non-terminal method bodies
private int exampleNonTerminal(){
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("NameOfThisMethod", true);
// unique non-terminal stuff
        trace("NameOfThisMethod", false);
        return recur;
}  
    
    */
    
    private int Variable()
    {
    	int recur = 0;
    	if(anyErrors) {
    		return -1;
    	}
    	trace("Variable", true);
    	// check if we hove the variable (identifier token)
    	if(token.code == lex.codeFor("IDNT"))
    	{
    		// and return the index of that token
    		recur = symbolList.LookupSymbol(token.lexeme);
    	}
    	// or get an error
    	else
    	{
    		error(lex.reserveFor("IDNT"), token.lexeme);
    	}
    	trace("Variable", false);
    	token = lex.GetNextToken();
    	return recur;
    }
    

    private int Term()
    {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Term", true);
        // terms always start with an expected factor
        recur = Factor();
        // check for a continuation with a * or /
        if(token.code == lex.codeFor("MULT") || token.code == lex.codeFor("DIVD"))
        {
        	int operation = 1;
        	if(token.code == lex.codeFor("MULT"))
        	{
        		operation = 2;
        	}
        	// recurse through term again because we found a * or /
        	token = lex.GetNextToken();
        	int op1 = recur;
        	int op2 = Term();
        	recur = symbolList.AddSymbol("$temp" + String.valueOf(currentTemp), 'V', 0);
        	quads.AddQuad(operation, op1, op2, recur);
        	currentTemp += 1;
        }
        trace("Term", false);
        return recur;
    }
    private int Factor()
    {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Factor", true);
        boolean negative = false;
        // check for the initial sign that may or may not be there
        if(token.code == lex.codeFor("PLUS") || token.code == lex.codeFor("MINS"))
        {
        	// we don't want to do anything with that yet so just continue
        	if(token.code == lex.codeFor("MINS"))
        	{
        		negative = true;
        	}
        	token = lex.GetNextToken();
        }
        // now we're looking for an identifier (variable), an integer constant, or a float constant as the main factor
        if(token.code == lex.codeFor("IDNT") || token.code == lex.codeFor("ICNS") || token.code == lex.codeFor("FCNS"))
        {
        	// return the symbol table value of whatever factor we found
        	recur = symbolList.LookupSymbol(token.lexeme);
        }
        // if there's no var/int/float check for parentheses before calling an error
        else
        {
        	if(token.code == lex.codeFor("LPAR"))
            {
        		// we have parentheses so we go back to simple expression
            	token = lex.GetNextToken();
            	recur = SimpleExpression();
            	// remember to check for the closing right parenthesis! We're not done without it!
            	if(token.code == lex.codeFor("RPAR"))
            	{
         
            	}
            	// If there's no parenthesis, error
            	else
            	{
            		error(lex.reserveFor("RPAR"), token.lexeme);
            	}
            }
        	// If there's no var/int/float and no parentheses, error
        	else
        	{
        		// There are a lot of things we could be expecting here for the error feedback but I'll just say integer
        		error("Integer", token.lexeme);
        	}
        }
        if(negative)
        {
        	int old = recur;
        	recur = symbolList.AddSymbol("$temp" + String.valueOf(currentTemp), 'V', 0);
        	currentTemp += 1;
        	quads.AddQuad(3, 0, old, recur);
        }
        trace("Factor", false);
        token = lex.GetNextToken();
        return recur;
    }
    
    private int ifStatement()
    {
    	int recur = 0;
    	if(anyErrors)
    	{
    		return -1;
    	}
    	trace("if statement", true);
    	token = lex.GetNextToken();
    	int afterif = comparison();
    	if(token.code == lex.codeFor("THEN"))
    	{
    		token = lex.GetNextToken();
    		recur = Statement();
    		symbolList.UpdateSymbol(afterif, 'l', quads.NextQuad());
    		if(token.code == lex.codeFor("ELSE"))
    		{
    			token = lex.GetNextToken();
    			int endif = symbolList.AddSymbol("$LABEL" + currentLabel, 'l', 0);
    			quads.AddQuad(9, 0, 0, endif);
    			symbolList.UpdateSymbol(afterif, 'l', quads.NextQuad());
    			recur = Statement();
    			symbolList.UpdateSymbol(endif, 'l', quads.NextQuad());
    		}
    	}
    	else
    	{
    		error(lex.reserveFor("THEN"), token.lexeme);
    	}
    	trace("if statement", false);
    	return recur;
    }
    
    private int condition()
    {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Condition", true);
// unique non-terminal stuff
        if(38 <= token.code && token.code <= 43)
    	{
    		recur = token.code;
    		token = lex.GetNextToken();
    	}
    	else
    	{
    		error(lex.reserveFor("EQLS"), token.lexeme);
    	}
        trace("Condition", false);
        return recur;
    }
    
    private int comparison()
    {
    	int recur = 0;
    	if(anyErrors)
    	{
    		return -1;
    	}
    	trace("comparison", true);
    	int val1 = SimpleExpression();
    	int operation = condition();
    	int val2 = SimpleExpression();
    	int gate = symbolList.AddSymbol("$temp" + String.valueOf(currentTemp), 'V', 0);
    	quads.AddQuad(3, val1, val2, gate);
    	currentTemp += 1;
    	int quadOp = 0;
    	// reverse the operation for a jump if not true
    	switch(operation)
    	{
    	case 38:
    		// >
    		quadOp = 14;
    		break;
    	case 39:
    		// <
    		quadOp = 15;
    		break;
    	case 40:
    		// >=
    		quadOp = 12;
    		break;
    	case 41:
    		// <=
    		quadOp = 11;
    		break;
    	case 42:
    		// ==
    		quadOp = 13;
    		break;
    	case 43:
    		// !=
    		quadOp = 10;
    		break;
    	}
    	// return the label we will jump to if the conditional is FALSE.
    	recur = symbolList.AddSymbol("$label" + String.valueOf(currentLabel), 'l', 0);
    	currentLabel += 1;
    	currentTemp += 1;
    	quads.AddQuad(quadOp, gate, 0, recur);
    	trace("comparison", false);
    	return recur;
    }
    
    private int whileStatement()
    {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("while", true);
        token = lex.GetNextToken();
// unique non-terminal stuff
        int whileGate = comparison();
        // check for do
        if(token.code == lex.codeFor("_DO_"))
        {
        	token = lex.GetNextToken();
        }
        else
        {
        	error(lex.reserveFor("_DO_"), token.lexeme);
        }
        int whileStart = symbolList.AddSymbol("$LABEL" + String.valueOf(currentLabel), 'l', quads.NextQuad() - 2);
        currentLabel += 1;
        recur = Block();
        quads.AddQuad(9, 0, 0, whileStart);
        symbolList.UpdateSymbol(whileGate, 'l', quads.NextQuad());
        trace("while", false);
        return recur;
    }
    
    private int printStatement()
    {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("print statement", true);
// unique non-terminal stuff
        if(token.code == 9)
        {
        	token = lex.GetNextToken();
        	if(token.code == lex.codeFor("LPAR"))
        	{
        		token = lex.GetNextToken();
        		recur = stringExpression();
        		if(token.code != lex.codeFor("RPAR"))
        		{
        			error(lex.reserveFor("RPAR"), token.lexeme);
        		}
        		token = lex.GetNextToken();
        	}
        	else
        	{
        		error(lex.reserveFor("LPAR"), token.lexeme);
        	}
        }
        else
        {
        	error(lex.reserveFor("WRTN"), token.lexeme);
        }
        quads.AddQuad(6, recur, 0, 0);
        trace("print statement", false);
        return recur;
    }
    
    private int stringExpression()
    {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("String Expression", true);
// unique non-terminal stuff
        if(token.code == 53)
        {
        	recur = symbolList.LookupSymbol(token.lexeme);
        	token = lex.GetNextToken();
        }
        else
        {
        	recur = SimpleExpression();
        }
        trace("String Expression", false);
        return recur;
    }
    
    private int untilStatement()
    {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("until", true);
        token = lex.GetNextToken();
// unique non-terminal stuff
        int untilGate = comparison();
        // check for do
        token = lex.GetNextToken();
        // use this structure for simplicity:
        // 0 branch not condition 2
        // 1 br 4
        // 2 until body
        // 3 br 0
        // 4 until end
        int untilStart = symbolList.AddSymbol("$LABEL" + String.valueOf(currentLabel), 'l', quads.NextQuad() - 1);
        currentLabel += 1;
        int untilEnd = symbolList.AddSymbol("$LABEL" + String.valueOf(currentLabel), 'l', 0);
        quads.AddQuad(9, 0, 0, untilEnd);
        symbolList.UpdateSymbol(untilGate, 'l', quads.NextQuad());
        currentLabel += 1;
        recur = Block();
        quads.AddQuad(9, 0, 0, untilStart);
        trace("until", false);
        return recur;
    }
}
