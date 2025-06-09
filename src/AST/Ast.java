package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.FileWriter;

import AST.exceptions.SyntaxException;
import AST.exceptions.UnknownInstructionException;
import AST.nodes.NodeAVL;
import AST.nodes.blocks.Block;
import AST.nodes.blocks.controlBlocks.*;
import AST.nodes.blocks.definitionBlocks.*;
import AST.nodes.blocks.functionBlocks.*;
import AST.nodes.instructions.*;

public class Ast {

    private NodeAVL root;
    private NodeAVL current;

    public Ast(){
        root = new Block();
        root.setLvl(-1);
        current = root;
    }

    public boolean addCode(String codeLine) throws UnknownInstructionException, SyntaxException{

        boolean finished = false;

        int ret;


        //If "else" statement no new node to create
        if(codeLine.compareToIgnoreCase("sino") == 0){
            if(current instanceof IfBlock) ((IfBlock)current).addElseBody();
            else throw new SyntaxException("no \"si ... llavors\" structure to use \"sino\"");
        }
        else{

            ret = ((Block)current).addBodyPart(codeLine, this);

            if(ret == -1){
                current = current.getDad();
            }

            if(ret == 1){
                //Set the current block the newest block created
                current = ((Block)current).getBodyPart(((Block)current).numBodyParts()-1);
            }
        }
        

        if(current == null) finished = true;
        return finished;
    
    }



    public NodeAVL getNode(String[] tokens) throws UnknownInstructionException, SyntaxException{


        if(tokens.length >= 3){
            //Assignation instruction
            if((tokens[1].compareTo(":=") == 0) || (tokens[1].compareTo("<-") == 0)){
                String assignated = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));
                assignated = replaceFormatExpresion(assignated);
                return new AssignationInstruct(tokens[0], assignated);
            }
        }

        if(tokens.length == 5){

            //If it is not conditional it is function
            if(tokens[0].compareToIgnoreCase("funcio") == 0){

                return new FunctionBlock(tokens[1], tokens[2], tokens[4]);
            }  
        }


        if(tokens.length == 3)
        {

            //Conditional structure
            String cond = replaceFormatExpresion(tokens[1]);
            //If structure
            if(tokens[0].compareToIgnoreCase("si") == 0){
                if(tokens[tokens.length-1].compareToIgnoreCase("llavors") != 0) throw new SyntaxException("missing \"llavors\" in \"si ... llavors\" structure");
                return new IfBlock(cond);
            } 
            //While structure
            if(tokens[0].compareToIgnoreCase("mentre") == 0){
                if(tokens[tokens.length-1].compareToIgnoreCase("fer") != 0) throw new SyntaxException("missing \"fer\" in \"mentre ... fer\" loop");
                return new WhileBlock(cond);                       //Else create while block
            }


            if(tokens[0].compareToIgnoreCase("accio") == 0){
                return new FunctionBlock(tokens[1], tokens[2]);
            }

        } 


        if(tokens.length == 2){

            //Conditional structure
            String cond = replaceFormatExpresion(tokens[1]);
            if((tokens[0].compareToIgnoreCase("mentre") == 0) && (this.current instanceof DoWhileBlock)){
                ((DoWhileBlock)current).addCond(cond);      //Add the condition to the block
                return null;                                //If inside of a do-while block finish block
            } 
        }
        //One token word
        if(tokens.length == 1){
            //Main function
            if(tokens[0].compareTo("algorisme") == 0) return new FunctionBlock("main", "", "enter");
            
            //Do while block
            if(tokens[0].compareToIgnoreCase("fer") == 0) return new DoWhileBlock();


            //Finished block
            if(((tokens[0].compareToIgnoreCase("fsi") == 0) && (current instanceof IfBlock)) ||
               ((tokens[0].compareToIgnoreCase("fmentre") == 0) && (current instanceof WhileBlock)) ||
               ((tokens[0].compareToIgnoreCase("ffuncio") == 0) && (current instanceof FunctionBlock)) ||
               ((tokens[0].compareToIgnoreCase("faccio") == 0) && (current instanceof FunctionBlock)) ||
               ((tokens[0].compareToIgnoreCase("falgorisme") == 0) && (current instanceof FunctionBlock))){

                    return null;    //If closing block instruction is correct
            }
            else{
                throw new SyntaxException("expected " + getExpectedFinishBlock(current));
            }
               

        }


        //If it's not any of the previous cases the expression/instruction doesn't exist
        throw new UnknownInstructionException(String.join(" ", Arrays.copyOfRange(tokens, 0, tokens.length)));

    }

    public void printCode(){
        ArrayList<String> code = new ArrayList<>();

        root.printNode(code);

        for(String s : code){
            System.out.println(s);
        }
    }


    public int printCode(String filePath){
        try{

            BufferedWriter fw = new BufferedWriter(new FileWriter(filePath));
            ArrayList<String> code = new ArrayList<>();

            root.printNode(code);

            for(String s : code){
                fw.write(s);
                fw.newLine();
            }

            fw.close();

            return 0;
        }
        catch(IOException e){return -1;}
    }

    private static String replaceFormatExpresion(String line){

        line = line.replace(" i ", " && ");
        line = line.replace(" o ", " || ");
        line = line.replaceAll("\\bno\\b", "!");   //Replace "no" with "!" only when is not in a word
        line = line.replace("! ", "!");            //Special case
        line = line.replace("=", "==");


        return line;
    }

    private static String getExpectedFinishBlock(NodeAVL block){
        String finish = "";
        if(block instanceof IfBlock) finish = "\"fsi\"";
        else if (block instanceof WhileBlock) finish = "\"fmentre\"";
        else if (block instanceof FunctionBlock) finish = "\"ffuncio / faccio / falgorisme\"";
        else finish = "nothing";
        return finish;
    }

}