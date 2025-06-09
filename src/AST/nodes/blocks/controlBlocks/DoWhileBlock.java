package AST.nodes.blocks.controlBlocks;

import java.util.ArrayList;

import AST.nodes.blocks.Block;


public class DoWhileBlock extends Block{
    
    private String condition;       //TODO: Change to a new structure
    
    public DoWhileBlock(){
        super();
    }

    public void addCond(String cond){
        this.condition = cond;
    }
    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "do\n" + ident + "{");

        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "} while " + "(" + condition + ");");

    }
}
