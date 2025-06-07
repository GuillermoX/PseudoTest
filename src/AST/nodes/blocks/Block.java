package AST.nodes.blocks;

import java.util.ArrayList;

import AST.exceptions.SyntaxException;
import AST.exceptions.UnknownInstructionException;
import AST.nodes.NodeAVL;
import AST.Ast;

public class Block extends NodeAVL{
   
    private ArrayList<NodeAVL> body;    

    public Block(){
        super();
        body = new ArrayList<>();
    }

    public int addBodyPart(String newCodeLine, Ast ast) throws UnknownInstructionException, SyntaxException{
        
        int ret = 0;

        String[] tokens = newCodeLine.split(" ");
        NodeAVL node = ast.getNode(tokens);

        if(node == null){
            return -1;
        }
        else{
            node.setDad(this);
            node.setLvl(super.getLvl()+1);
            body.add(node);
            if(node instanceof Block) ret = 1;
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
