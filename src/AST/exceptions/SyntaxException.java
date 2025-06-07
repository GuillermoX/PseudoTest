package AST.exceptions;

public class SyntaxException extends Exception{
    
    public SyntaxException(String mensaje){
        super("Sintax error: " + mensaje);
    }
}
