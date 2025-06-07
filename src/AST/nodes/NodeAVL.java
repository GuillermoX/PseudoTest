package AST.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import AST.exceptions.UnknownInstructionException;
import AST.nodes.blocks.controlBlocks.*;
import AST.nodes.blocks.functionBlocks.FunctionBlock;
import AST.nodes.instructions.AssignationInstruct;

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


    public String getLvlIdent(int lvl){
        String ident = "";
        for(int i = 0; i < lvl; i++){
            ident += "\t";
        }

        return ident;
    }

    public abstract void printNode(ArrayList<String> code);


}
