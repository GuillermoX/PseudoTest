package AST.nodes.blocks.definitionBlocks.structureBlock;

import java.util.ArrayList;

import AST.nodes.blocks.Block;


public class StructureBlock extends Block{
    
    String name;

    public StructureBlock(String name){
        super();
        this.name = name;
    }

    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "typedef struct\n" + ident + "{");
        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }
        code.add(ident + "} " + name + ";");


    }
}
