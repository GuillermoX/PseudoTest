package AVL;

import java.util.ArrayList;

import AVL.nodes.NodeAVL;
import AVL.nodes.blocks.Block;

public class Avl {

    private NodeAVL root;
    private NodeAVL current;

    public Avl(){
        root = new Block();
        root.setLvl(-1);
        current = root;
    }

    public boolean addCode(String codeLine){

        boolean finished = false;

        ArrayList<Boolean> ret;
        ret = ((Block)current).addBodyPart(codeLine);
        
        if(ret.get(0)){
            current = current.getDad();
        }
        
        if(ret.get(1)){
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