package AST.nodes.blocks.controlBlocks;

import java.util.ArrayList;

import AST.nodes.blocks.Block;

public class IfBlock extends Block{
    
    private String condition;       //TODO: Change to a new structure
    private int elseBodyIndex;
    
    public IfBlock(String cond){
        super();
        this.condition = cond;
        elseBodyIndex = -1;     //If "if block" doesn't have "else" body the index is -1
    }

    //Sets an else body space for new instructions
    public void addElseBody(){
        elseBodyIndex = super.numBodyParts();
    }
    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        code.add(ident + "if " + "(" + condition + ")");
        code.add(ident + "{");
        
        int max = super.numBodyParts();
        if(elseBodyIndex != -1) max = elseBodyIndex;
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

        if(elseBodyIndex != -1){
            code.add(ident + "else\n" + ident + "{");

            max = super.numBodyParts();
            for(int i = elseBodyIndex; i < max; i++){
                super.getBodyPart(i).printNode(code);
            }

            code.add(ident + "}");
        }

    }

}
