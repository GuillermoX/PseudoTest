package AST;

import java.util.ArrayList;
import java.util.Arrays;

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
        ret = ((Block)current).addBodyPart(codeLine, this);
        
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



    public NodeAVL getNode(String[] tokens) throws UnknownInstructionException, SyntaxException{

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
            if(tokens[0].compareToIgnoreCase("mentre") == 0){
                if(this.current instanceof DoWhileBlock){
                    ((DoWhileBlock)current).addCond(cond);      //Add the condition to the block
                    return null;                                //If inside of a do-while block finish block
                } 
                else return new WhileBlock(cond);                       //Else create while block
            }

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

        //One token word
        if(tokens.length == 1){
            //Main function
            if(tokens[0].compareTo("algorisme") == 0) return new FunctionBlock("main", "", "enter");
            
            //Do while block
            if(tokens[0].compareToIgnoreCase("fer") == 0) return new DoWhileBlock();

            //Finished block
            //TODO: Improve logical structure of comparisons
            if(tokens[0].compareToIgnoreCase("fsi") == 0){
                if(current instanceof IfBlock) return null;
                else throw new SyntaxException("expected " + getExpectedFinishBlock(current)); 
            }
            else if(tokens[0].compareToIgnoreCase("fmentre") == 0){
                if(current instanceof WhileBlock) return null;
                else throw new SyntaxException("expected "  + getExpectedFinishBlock(current)); 
            }
            else if(tokens[0].compareToIgnoreCase("ffuncio") == 0){
                if(current instanceof FunctionBlock) return null;
                else throw new SyntaxException("expected "  + getExpectedFinishBlock(current)); 
            }
            else if(tokens[0].compareToIgnoreCase("faccio") == 0){
                if(current instanceof FunctionBlock) return null;
                else throw new SyntaxException("expected "  + getExpectedFinishBlock(current)); 
            }
            else if(tokens[0].compareToIgnoreCase("falgorisme") == 0){
                if(current instanceof FunctionBlock) return null;
                else throw new SyntaxException("expected " + getExpectedFinishBlock(current)); 
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
        if(block instanceof IfBlock) finish = "fsi";
        else if (block instanceof WhileBlock) finish = "fmentre";
        else if (block instanceof FunctionBlock) finish = "ffuncio / faccio / falgorisme";
        return finish;
    }

}