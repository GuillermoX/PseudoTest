package AST;

import java.util.ArrayList;

import AST.nodes.NodeAVL;
import AST.nodes.blocks.Block;

public class Ast {

    private NodeAVL root;
    private NodeAVL current;

    public Ast(){
        root = new Block();
        root.setLvl(-1);
        current = root;
    }

    public boolean addCode(String codeLine){

        boolean finished = false;

        int ret;
        ret = ((Block)current).addBodyPart(codeLine);
        
        if(ret == -1){
            current = current.getDad();
        }
        
        if(ret == 1){
            //Set the current block the newest block created
            current = ((Block)current).getBodyPart(((Block)current).numBodyParts()-1);
        }

        if(current == null) finished = true;
        return finished;
    
    }

    public void printCode(){
        ArrayList<String> code = new ArrayList<>();

        root.printNode(code);

        for(String s : code){
            System.out.println(s);
        }
    }

}