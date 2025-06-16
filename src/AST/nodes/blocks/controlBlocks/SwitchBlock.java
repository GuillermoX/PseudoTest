package AST.nodes.blocks.controlBlocks;

import AST.nodes.blocks.Block;

import java.util.ArrayList;


public class SwitchBlock extends Block{
    
    
    private String var;
    
    public SwitchBlock(String var){
        super();
        this.var = var;
    }


    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "switch " + "(" + var + ")");
        code.add(ident + "{");
         
        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

    }

}
