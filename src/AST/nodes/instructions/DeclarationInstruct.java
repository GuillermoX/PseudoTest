package AST.nodes.instructions;

import AST.nodes.NodeAVL;
import AST.Enums.Types;

import java.util.ArrayList;

public class DeclarationInstruct extends NodeAVL {
    ArrayList<String> vars; 
    Types type;
    int lenght;         //In case the new(s) variable(s) is an array

    //One variable declaration
    public DeclarationInstruct(String var, Types type){
        this.vars = new ArrayList<>();
        vars.add(var);
        this.type = type;
        this.lenght = 1;
    }


    //One array variable declaration
    public DeclarationInstruct(String var, Types type, int lenght){
        this.vars = new ArrayList<>();
        vars.add(var);
        this.type = type;
        this.lenght = lenght;
    }

    //Multiple variable declaration
    public DeclarationInstruct(ArrayList<String> vars, Types type){
        this.vars = vars;
        this.type = type;
        this.lenght = 1;
    }

    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());
        String varStr = "";
        for(int i = 0; i < vars.size()-1; i++){
            varStr += vars.get(i) + ",";
        }
        varStr += vars.get(vars.size()-1);    //Add last variable without coma
        code.add(ident + type.print() + " " + varStr + ";");
    }
}
