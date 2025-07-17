package AST.nodes.blocks;

import java.util.ArrayList;

import AST.nodes.NodeAVL;

public class Block extends NodeAVL{
   
    private ArrayList<NodeAVL> body;    

    public Block(){
        super();
        body = new ArrayList<>();
    }

    public void addBodyPart(NodeAVL node) {
        
        node.setDad(this);
        node.setLvl(super.getLvl()+1);
        body.add(node);
    }

    public ArrayList<NodeAVL> getBody(){
        return body;
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
