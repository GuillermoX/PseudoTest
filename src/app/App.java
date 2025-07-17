package app;

import java.io.BufferedReader;
import java.io.FileReader;

import AST.Ast;
import AST.exceptions.*;

public class App {
    public static void main(String[] args) throws Exception {
        try{

            Ast avl = new Ast();
            avl.loadCodeFromFile(args[0]);
            avl.printCode(args[1]);
        }
        catch(UnknownFunctionCallException | SyntaxException | UnknownInstructionException e){
            System.err.println(e);
        }


    }
}
