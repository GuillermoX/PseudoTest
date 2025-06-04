package AVL.nodes.instructions;

import AVL.nodes.NodeAVL;
import java.util.ArrayList;

public class AssignationInstruct extends NodeAVL{

    private String assignated;
    private String assignator;

    public AssignationInstruct(String assignated, String assignator){
        super();
        this.assignated = assignated;
        this.assignator = assignator;
    }



    public void printNode(ArrayList<String> code){
       code.add(super.getLvlIdent(super.getLvl()) + assignated + " = " + assignator + ";"); 
    }
    
}
