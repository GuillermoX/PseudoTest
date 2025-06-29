package AST.nodes.instructions;

import java.util.ArrayList;

import AST.nodes.NodeAVL;

public class FunctionCallInstruct extends NodeAVL{
    
    String name;
    ArrayList<String> params;


    public FunctionCallInstruct(String functName, ArrayList<String> params){
        this.name = functName;
        //TODO: Correct parameters if IO
        this.params = params;
    }


    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(getLvl());


        String paramStr = "";
        for(int i = 0; i < this.params.size(); i ++){
            paramStr += params.get(i);
            if(i != (this.params.size()-1)) paramStr += ", ";
        }

        code.add(ident + name + "(" + paramStr + ");");
   }
}
