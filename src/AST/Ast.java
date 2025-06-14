package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Queue;

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


            ArrayList<String> tokensList = new ArrayList<>();
            tokensList = splitTokens(codeLine);

            String[] tokens = new String[tokensList.size()];
            for(int i = 0; i < tokensList.size(); i++){
                String tokenString = tokensList.get(i);
                //tokens[i] = tokenString.replaceAll("^\\(|\\)$", "");
                tokens[i] = tokenString;
            } 

            //Add the new instruction to AST
            this.addInstruct(tokens);


        }
        

        if(current == null) finished = true;
        return finished;
    
    }



    public void addInstruct(String[] tokens) throws UnknownInstructionException, SyntaxException{

        Queue<NodeAVL> newNodes = new ArrayDeque<>();        //Stack to add at the end all the new Nodes
        Queue<String> newInstruct = new ArrayDeque<>();        //Stack to add at the end all the new Nodes
        //NodeAVL node = null;
        boolean correct = true;
        int finishedBlocks = 0;


        if(tokens.length == 5){
            //If it is not conditional it is function
            if(tokens[0].compareToIgnoreCase("funcio") == 0){
                tokens[2] = tokens[2].substring(1, tokens[2].length()-1);
                newNodes.add(new FunctionBlock(tokens[1], tokens[2], tokens[4]));
            }  
            else{
                correct = false;
            }
        }
        else if(tokens.length == 3)
        {

            //Conditional structure
            String cond = replaceFormatExpresion(tokens[1]);
            cond = cond.substring(1, cond.length()-1);
            //If structure
            if(tokens[0].compareToIgnoreCase("si") == 0){
                if(tokens[tokens.length-1].compareToIgnoreCase("llavors") != 0) throw new SyntaxException("missing \"llavors\" in \"si ... llavors\" structure");
                newNodes.add(new IfBlock(cond));
            } 
            //While structure
            else if(tokens[0].compareToIgnoreCase("mentre") == 0){
                if(tokens[tokens.length-1].compareToIgnoreCase("fer") != 0) throw new SyntaxException("missing \"fer\" in \"mentre ... fer\" loop");
                newNodes.add(new WhileBlock(cond));                       //Else create while block
            }
            //Void function
            else if(tokens[0].compareToIgnoreCase("accio") == 0){   
                tokens[2] = tokens[2].substring(1, tokens[2].length()-1);
                newNodes.add(new FunctionBlock(tokens[1], tokens[2]));
            }
            else{
                correct = false;
            }

        } 
        else if(tokens.length == 2){

            //Conditional structure
            String cond = replaceFormatExpresion(tokens[1]);
            cond = cond.substring(1, cond.length()-1);
            //Finishing do while block
            if((tokens[0].compareToIgnoreCase("mentre") == 0) && (this.current instanceof DoWhileBlock)){
                ((DoWhileBlock)current).addCond(cond);      //Add the condition to the block
                finishedBlocks = 1;                               //If inside of a do-while block finish block
            } 
            //Switch structure
            else if(tokens[0].compareToIgnoreCase("opcio") == 0){
                newNodes.add(new SwitchBlock(cond));
            }
            else{
                correct = false;
            }
        }
        //One token word
        else if(tokens.length == 1){
            //Main function
            if(tokens[0].compareTo("algorisme") == 0) newNodes.add(new FunctionBlock("main", "", "enter"));
            
            //Do while block
            else if(tokens[0].compareToIgnoreCase("fer") == 0) newNodes.add(new DoWhileBlock());


            //Finished block
            else if(tokens[0].charAt(0) == 'f'){
                if(((tokens[0].compareToIgnoreCase("fsi") == 0) && (current instanceof IfBlock)) ||
                   ((tokens[0].compareToIgnoreCase("fmentre") == 0) && (current instanceof WhileBlock)) ||
                   ((tokens[0].compareToIgnoreCase("ffuncio") == 0) && (current instanceof FunctionBlock)) ||
                   ((tokens[0].compareToIgnoreCase("faccio") == 0) && (current instanceof FunctionBlock)) ||
                   ((tokens[0].compareToIgnoreCase("falgorisme") == 0) && (current instanceof FunctionBlock))){

                    finishedBlocks = 1;     //If closing block instruction return to previous block
                }
                else if(((tokens[0].compareToIgnoreCase("fopcio") == 0) && ((current instanceof SwitchBlock) || (current instanceof SwitchCaseBlock)))){
                    if(current instanceof SwitchCaseBlock) finishedBlocks = 2;
                    else finishedBlocks = 1;
                }
                else{
                    throw new SyntaxException("expected " + getExpectedFinishBlock(current));
                }
            }
            else{
                correct = false;
            }
               

        }
        else{
            correct = false;
        }

        //If not any of the prev instructions
        if(!correct){
            correct = true;
            //Assignation instruction
            if(tokens.length >= 3 && ((tokens[1].compareTo(":=") == 0) || (tokens[1].compareTo("<-") == 0))){
                String assignated = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));
                assignated = replaceFormatExpresion(assignated);
                newNodes.add(new AssignationInstruct(tokens[0], assignated));
            }
            //Switch structure case
            else if(tokens[0].charAt(tokens[0].length()-1) == ':'){
                //TODO: Add the first instruction of case
                if(current instanceof SwitchBlock || current instanceof SwitchCaseBlock){
                    if(current instanceof SwitchCaseBlock) finishedBlocks = 1;
                    newNodes.add(new SwitchCaseBlock(tokens[0].substring(0, tokens[0].length()-1)));
                    if(tokens.length > 1){ 
                        String instruct = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));
                        newInstruct.add(instruct);
                    }
                }
                else{   //If case but not in Switch block
                    throw new SyntaxException("missing \"opcio\" structure to create case");
                }
            }
            else{
                correct = false;
            }
        }




        if(!correct){
            //If it's not any of the previous cases the expression/instruction doesn't exist
            throw new UnknownInstructionException(String.join(" ", Arrays.copyOfRange(tokens, 0, tokens.length)));
        }


        //Return to previous blocks
        for(int i = 0; i < finishedBlocks; i++){
            current = current.getDad();
        }

        //Add the new nodes if necessary
        while(!newNodes.isEmpty()){
            NodeAVL node = newNodes.poll();
            ((Block)current).addBodyPart(node);
            if(node instanceof Block) current = node;
        }

        //Add new instructions inside this one (only switch case block for the moment)
        while(!newInstruct.isEmpty()){
            this.addCode(newInstruct.poll());
        }

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
        line = line.replace("=", "==");
        line = line.replaceAll("\\bno\\b", "!");   //Replace "no" with "!" only when is not in a word
        line = line.replace("! ", "!");            //Special case


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

    private static ArrayList<String> splitTokens(String text) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean insideParentheses = false;
        int parenthesesLevel = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '(') {
                if (parenthesesLevel >= 0) currentToken.append(c);
                parenthesesLevel++;
                insideParentheses = true;
            } else if (c == ')') {
                parenthesesLevel--;
                currentToken.append(c);
                if (parenthesesLevel == 0) {
                    insideParentheses = false;
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
            } else if (Character.isWhitespace(c) && !insideParentheses) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
            } else {
                currentToken.append(c);
            }
        }

        // Add the last token if any
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }

}