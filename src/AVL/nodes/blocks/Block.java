package AVL.nodes.blocks;

import java.util.ArrayList;

import AVL.nodes.NodeAVL;

public class Block extends NodeAVL{
   
    private ArrayList<NodeAVL> body;    

    public Block(){
        super();
        body = new ArrayList<>();
    }

    public ArrayList<Boolean> addBodyPart(String newCodeLine){
        
        ArrayList<Boolean> ret = new ArrayList<>();        
        ret.add(false);
        ret.add(false);

        String[] tokens = newCodeLine.split(" ");
        NodeAVL node = NodeAVL.getNode(tokens);

        if(node == null){

            ret.set(0, true);   //Finished block
        }
        else{
            node.setDad(this);
            node.setLvl(super.getLvl()+1);
            body.add(node);
            if(node instanceof Block) ret.set(1, true);
        }

        return ret; 
    }

    public NodeAVL getBodyPart(int i){
        return body.get(i);
    }

    public int numBodyParts(){
        return body.size();
    }



    public void printNode(ArrayList<String> code){

        int max = numBodyParts();
        for(int i = 0; i < max; i++){
            getBodyPart(i).printNode(code);
        }

    }

}
