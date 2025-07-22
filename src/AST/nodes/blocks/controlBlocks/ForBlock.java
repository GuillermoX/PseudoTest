package AST.nodes.blocks.controlBlocks;

import java.util.ArrayList;

import AST.nodes.blocks.Block;
import AST.nodes.instructions.AssignationInstruct;

public class ForBlock extends Block{
     
    private AssignationInstruct assignment;
    private String max;
    private String incr;
    
    public ForBlock(AssignationInstruct assign, String max, String increment){
        super();
        this.assignment = assign;
        this.max = max;
        this.incr = increment;
    }
    
    
    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        String assig = assignment.getAssignated();
        code.add(ident + "for " + "(" + assignment.toString() + " " + assig + " <= " + max + "; " + assig + " = " + assig + incr + ")");
        code.add(ident + "{");

        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

    }

}

 
