package AST.exceptions;

public class UnknownFunctionCallException extends Exception{
    
    public UnknownFunctionCallException(String mensaje){
        super(mensaje);
    }
}
