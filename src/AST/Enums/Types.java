package AST.Enums;

public enum Types {
    INT, FLOAT, CHAR, BOOL, VOID, STRUCT;

    public String print(){
        if(this == Types.INT) return "int";
        else if(this == Types.FLOAT) return "float";
        else if(this == Types.CHAR) return "char";
        else if(this == Types.BOOL) return "bool";
        else if(this == Types.VOID) return "void";
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
        else return Types.STRUCT;
    }
}
