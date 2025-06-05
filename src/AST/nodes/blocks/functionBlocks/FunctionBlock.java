package AST.nodes.blocks.functionBlocks;

import java.util.ArrayList;

import AST.Enums.Types;
import AST.nodes.blocks.Block;;

public class FunctionBlock extends Block {
    
    private String name;
    private String params;       //TODO: Use params
    private Types type;
    
    public FunctionBlock(String name, String params, String type){
        super();
        this.name = name;
        this.params = params;
        if(type.compareToIgnoreCase("enter") == 0) this.type = Types.INT;
        else if(type.compareToIgnoreCase("real") == 0) this.type = Types.FLOAT;
        else if(type.compareToIgnoreCase("caràcter") == 0 ||
                type.compareToIgnoreCase("caracter") == 0) this.type = Types.CHAR;
        //TODO: TypeError
    }

    public FunctionBlock(String name){
        super();
        this.name = name;
        this.type = Types.VOID;
    }

    @Override
    public void printNode(ArrayList<String> code) {
        
        String ident = super.getLvlIdent(super.getLvl());
        String cType = "void";
        if(type == Types.INT) cType = "int";
        else if(type == Types.FLOAT) cType = "float";
        else if(type == Types.CHAR) cType = "char";

        //TODO: Add the parameters
        code.add(ident + cType + " " + name + "(" + params + ")");
        code.add(ident + "{");

        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

    }

}
