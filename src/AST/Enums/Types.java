package AST.Enums;

import AST.exceptions.SyntaxException;

public enum Types {
    INT, FLOAT, CHAR, BOOL, VOID, STRUCT, FILE;

    public String print(){
        if(this == Types.INT) return "int";
        else if(this == Types.FLOAT) return "float";
        else if(this == Types.CHAR) return "char";
        else if(this == Types.BOOL) return "bool";
        else if(this == Types.VOID) return "void";
        else if(this == Types.FILE) return "FILE*";
        else return "struct";
    }

    public static Types getType(String type){
        if(type.compareToIgnoreCase("enter") == 0 ||
           type.compareToIgnoreCase("enters") == 0 ||
           type.compareToIgnoreCase("d'enter") == 0 ||
           type.compareToIgnoreCase("d'enters") == 0) return Types.INT;
        else if(type.compareToIgnoreCase("real") == 0 ||
                type.compareToIgnoreCase("reals") == 0) return Types.FLOAT;
        else if(type.compareToIgnoreCase("caracter") == 0 ||
                type.compareToIgnoreCase("caracters") == 0 ||
                type.compareToIgnoreCase("caràcter") == 0 ||
                type.compareToIgnoreCase("caràcters") == 0) return Types.CHAR;
        else if(type.compareToIgnoreCase("boolea") == 0 ||
                type.compareToIgnoreCase("booleans") == 0 ||
                type.compareToIgnoreCase("booleà") == 0) return Types.BOOL;
        else if(type.compareToIgnoreCase("arxiu") == 0) return Types.FILE; 
        else return Types.STRUCT;
    }

    public static String getFormatSpecifier(Types type) throws SyntaxException{
        if(type == INT) return "%d";
        else if(type == FLOAT) return "%f";
        else if(type == CHAR) return "%c";
        else throw new SyntaxException("variable type not allowed");
    }
}
