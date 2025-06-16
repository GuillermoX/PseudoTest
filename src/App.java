
import java.io.BufferedReader;
import java.io.FileReader;

import AST.Ast;
import AST.exceptions.*;

public class App {
    public static void main(String[] args) throws Exception {
        BufferedReader fr = new BufferedReader(new FileReader(args[0]));

        Ast avl = new Ast();

        String nextLine = fr.readLine();
        int lineCount = 1;
        try{


            while(nextLine != null){
                nextLine = nextLine.trim();
                if(nextLine.compareTo("") != 0){
                    avl.addCode(nextLine);
                }
                nextLine = fr.readLine();
                lineCount ++;
            }


            avl.printCode(args[1]);
        }
        catch(UnknownInstructionException | SyntaxException e){
            System.err.println(e + " [Line " + lineCount + "]");
        }

        fr.close();

    }
}
