package AST.nodes.instructions;

import java.util.ArrayList;

public class DefineInstruct extends AssignationInstruct{
    
    public DefineInstruct(String assignated, String assignator){
        super(assignated, assignator);
    }

    @Override
    public void printNode(ArrayList<String> code){
       code.add(super.getLvlIdent(super.getLvl()) + "#define " + super.getAssignated() + " " + super.getAssignator()); 
    }
}
