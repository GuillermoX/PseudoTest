package AST.nodes.blocks.controlBlocks;

import java.util.ArrayList;

import AST.nodes.blocks.Block;


public class SwitchCaseBlock extends Block{
    
    private String cas;

    public SwitchCaseBlock(String cas){
        super();
        this.cas = cas;
    }


    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "case " + cas + ":");
         
        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "\tbreak;");

    }
}
