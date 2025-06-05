package AST.nodes;

import java.util.ArrayList;
import java.util.Arrays;

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

    public static NodeAVL getNode(String[] tokens){

        //Assignation instruction
        if((tokens.length >= 3) && ((tokens[1].compareTo(":=") == 0) ||
            (tokens[1].compareTo("<-") == 0))){
            String assignated = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));
            assignated = replaceFormatExpresion(assignated);
            return new AssignationInstruct(tokens[0], assignated);
        } 
       

        //Conditional structure type "if (cond)" and function type
        if((tokens.length >= 2)){
            String cond = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));   //Get the conditional part in one String
            cond = replaceFormatExpresion(cond);
            if(tokens[0].compareToIgnoreCase("si") == 0) return new IfBlock(cond);
            if(tokens[0].compareToIgnoreCase("mentre") == 0) return new IfBlock(cond);

            //If it is not conditional it is function
            if(tokens[0].compareToIgnoreCase("funcio") == 0){

                cond = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length-2));
                String[] params = cond.split("[(]");
                params = params[1].split("[])]");
                String[] name = tokens[1].split("[(]");
                return new FunctionBlock(name[0], params[0], tokens[tokens.length-1]);
            }  
            else if(tokens[0].compareToIgnoreCase("accio") == 0){

                cond = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));
                String[] params = cond.split("[(]");
                params = params[1].split("[])]");
                String[] name = tokens[1].split("[(]");
                return new FunctionBlock(name[0]);
            }

        } 

        //Main function or finished blocks
        if(tokens.length == 1){
            //Main function
            if(tokens[0].compareTo("algorisme") == 0) return new FunctionBlock("main", "", "enter");
            
            //Finished block
            if(tokens[0].charAt(0) == 'f') return null;
        }


        //TODO: Only return null when finished block
        return null;
    }

    public String getLvlIdent(int lvl){
        String ident = "";
        for(int i = 0; i < lvl; i++){
            ident += "\t";
        }

        return ident;
    }

    public abstract void printNode(ArrayList<String> code);

    private static String replaceFormatExpresion(String line){

        line = line.replace(" i ", " && ");
        line = line.replace(" o ", " || ");
        line = line.replaceAll("\\bno\\b", "!");   //Replace "no" with "!" only when is not in a word
        line = line.replace("! ", "!");            //Special case
        line = line.replace("=", "==");


        return line;
    }

}
