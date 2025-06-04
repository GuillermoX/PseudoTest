package AVL.nodes.blocks.controlBlocks;

import java.util.ArrayList;

import AVL.nodes.blocks.Block;

public class IfBlock extends Block{
    
    private String condition;       //TODO: Change to a new structure
    
    public IfBlock(String cond){
        super();
        this.condition = cond;
    }
    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "if " + condition);
        code.add(ident + "{");

        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

    }

}
