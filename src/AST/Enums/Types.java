package AST.Enums;

public enum Types {
    INT, FLOAT, CHAR, VOID, STRUCT;

    public String print(){
        if(this == Types.INT) return "int";
        else if(this == Types.FLOAT) return "float";
        else if(this == Types.CHAR) return "char";
        else if(this == Types.VOID) return "void";
        else return "struct";
    }
}
