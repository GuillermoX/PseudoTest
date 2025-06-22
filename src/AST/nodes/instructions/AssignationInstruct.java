package AST.nodes.instructions;

import java.util.ArrayList;

import AST.nodes.NodeAVL;

public class AssignationInstruct extends NodeAVL{

    private String assignated;
    private String assignator;

    public AssignationInstruct(String assignated, String assignator){
        super();
        this.assignated = assignated;
        this.assignator = assignator;
    }

    public String getAssignated(){
        return assignated;
    }

    public String getAssignator(){
        return assignator;
    }



    public void printNode(ArrayList<String> code){
       code.add(super.getLvlIdent(super.getLvl()) + assignated + " = " + assignator + ";"); 
    }
    
}
