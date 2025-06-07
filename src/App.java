
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import AST.Ast;
import AST.exceptions.*;

public class App {
    public static void main(String[] args) throws Exception {
        BufferedReader fr = new BufferedReader(new FileReader(args[0]));

        Ast avl = new Ast();

        boolean finished = false;
        String nextLine = fr.readLine();
        int lineCount = 1;
        try{


            while(nextLine != null){
                nextLine = nextLine.replaceFirst("^\\s+", "");
                if(nextLine.compareTo("") != 0){
                    finished = avl.addCode(nextLine);
                }
                nextLine = fr.readLine();
                lineCount ++;
            }


            avl.printCode();
        }
        catch(UnknownInstructionException | SyntaxException e){
            System.err.println(e + " [Line " + lineCount + "]");
        }

    }
}
