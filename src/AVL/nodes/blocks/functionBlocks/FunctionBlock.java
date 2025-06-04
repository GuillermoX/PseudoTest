package AVL.nodes.blocks.functionBlocks;

import java.util.ArrayList;

import AVL.nodes.blocks.Block;
import AVL.Enums.Types;;

public class FunctionBlock extends Block {
    
    private String name;
    private ArrayList<String> params;       //TODO: Use params
    private Types type;
    
    public FunctionBlock(String name, String type){
        super();
        this.name = name;
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
        code.add(ident + cType + " " + name + "()");
        code.add(ident + "{");

        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

    }

}
