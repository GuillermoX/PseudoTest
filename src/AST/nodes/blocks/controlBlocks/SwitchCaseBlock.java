package AST.nodes.blocks.controlBlocks;

import java.util.ArrayList;

import AST.nodes.blocks.Block;


public class SwitchCaseBlock extends Block{
    
    private String cas;

    public SwitchCaseBlock(String cas){
        super();
        this.cas = cas;
    }


    public SwitchCaseBlock(){
        super();
        this.cas = null;    //default case
    }

    public boolean isDefault(){
        if(cas == null) return true;
        else return false;
    }


    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        if(cas != null) code.add(ident + "case " + cas + ":");
        else code.add(ident + "default:");
         
        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "\tbreak;");

    }
}
