package AVL.nodes;

import java.util.ArrayList;

import AVL.nodes.blocks.controlBlocks.*;
import AVL.nodes.blocks.functionBlocks.FunctionBlock;
import AVL.nodes.instructions.AssignationInstruct;

public abstract class NodeAVL {
    private NodeAVL dad;
    //Sons aren't stored in generic Node because there are some nodes who can't have sons (instruction nodes)
    private int lvl;    //To know how many identations are needed;

    public NodeAVL(){
    }

    public void setDad(NodeAVL dad){
        this.dad = dad;
    }

    public NodeAVL getDad(){
        return this.dad;
    }

    public void setLvl(int lvl){
        this.lvl = lvl;
    }

    public int getLvl(){
        return lvl;
    }

    public static NodeAVL getNode(String[] tokens){

        if((tokens.length == 3) && (tokens[1].compareTo(":=") == 0)) return new AssignationInstruct(tokens[0], tokens[2]);
        else if(tokens[0].compareToIgnoreCase("si") == 0) return new IfBlock(tokens[1]);
        else if(tokens[0].compareToIgnoreCase("mentre") == 0) return new WhileBlock(tokens[1]);
        else if(tokens[0].compareToIgnoreCase("funcio") == 0) return new FunctionBlock(tokens[1], "prueba_type");
        else if(tokens[0].compareToIgnoreCase("accio") == 0) return new FunctionBlock(tokens[1]);
        else if(tokens[0].compareToIgnoreCase("algorisme") == 0) return new FunctionBlock("main", "enter");
        else return null;
    }

    public String getLvlIdent(int lvl){
        String ident = "";
        for(int i = 0; i < lvl; i++){
            ident += "\t";
        }

        return ident;
    }

    public abstract void printNode(ArrayList<String> code);

}
