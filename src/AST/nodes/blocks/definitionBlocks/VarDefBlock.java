package AST.nodes.blocks.definitionBlocks;

import java.util.ArrayList;

import AST.nodes.blocks.Block;

public class VarDefBlock extends Block {

    
    public VarDefBlock(){
        super();
    }

    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "/*  -- Variable definitions  -- */") ;
        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }
        code.add(ident + "/* ----------------------------- */") ;


    }

}
