import ADT.SymbolTable;
import ADT.Lexical;
import ADT.*;
/**
 *
 * @author abrouill SPRING 2021
 */
public class Main {
    public static void main(String[] args) {
    	final String filestart = "";
    	final String file1 = filestart + "GoodSyntaxAFA21.txt";
    	final String file2 = filestart + "BadSyntax-1-AFA21.txt";
    	final String file3 = filestart + "BadSyntax-2-AFA21.txt";
    	final String finalfile = filestart + "CodeGenFULL-FA21.txt";
    	System.out.println("Ryan Johnson");
    	System.out.println("7071");
    	System.out.println("CS 4100, Fall 2021");
    	System.out.println("Compiler IDE used: Eclipse Neon");
        String filePath = finalfile;
        boolean traceon = true;
        Syntactic parser = new Syntactic(filePath, traceon);
        String output = "output.txt";
        parser.parse();
        parser.printQuads("quads" + output);
        parser.run(output);
        System.out.println("Done.");
    }
}