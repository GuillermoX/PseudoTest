package AST.nodes.instructions;

import AST.nodes.NodeAVL;

import java.util.ArrayList;

public class ReturnInstruct extends NodeAVL{
   String ret;
   
   public ReturnInstruct(String ret){
        this.ret = ret;
   }

   public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(getLvl());
        code.add(ident + "return (" + ret + ");");
   }
}
