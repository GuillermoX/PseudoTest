package AST.nodes.blocks.definitionBlocks.structureBlock;

import java.util.ArrayList;

import AST.nodes.blocks.Block;

public class StructureDefBlock extends Block{
    

    public StructureDefBlock(){
        super();
    }

    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "//Structure definition") ;
        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }


    }
}
