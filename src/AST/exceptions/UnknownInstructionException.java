package AST.exceptions;

public class UnknownInstructionException extends Exception{
    public UnknownInstructionException(String mensaje){
        super(mensaje);
    }
}