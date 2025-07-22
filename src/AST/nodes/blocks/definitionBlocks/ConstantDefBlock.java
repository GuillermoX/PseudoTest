package AST.nodes.blocks.definitionBlocks;

import java.util.ArrayList;

import AST.nodes.blocks.Block;

public class ConstantDefBlock extends Block{
    

    public ConstantDefBlock(){
        super();
    }

    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        if(super.getBody().size() > 0){
            code.add(ident + "/* -- Constant variable definitions -- */") ;
            int max = super.numBodyParts();
            for(int i = 0; i < max; i++){
                super.getBodyPart(i).printNode(code);
            }
            code.add(ident + "/* ------------------------------------ */") ;
        }


    }
}
