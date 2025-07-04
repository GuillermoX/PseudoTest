package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Queue;

import javax.sql.rowset.spi.SyncFactoryException;

import AST.Enums.Types;
import AST.exceptions.*;
import AST.nodes.NodeAVL;
import AST.nodes.blocks.Block;
import AST.nodes.blocks.controlBlocks.*;
import AST.nodes.blocks.functionBlocks.*;
import AST.nodes.instructions.*;
import AST.nodes.blocks.definitionBlocks.*;
import AST.nodes.blocks.definitionBlocks.structureBlock.*;

public class Ast {

    private NodeAVL root;
    private NodeAVL current;
    private NodeAVL currentFunct;
    private NodeAVL lastCurrent;    //To save current when changing to other spaces
    private NodeAVL cnst;   //Constant block
    private ArrayList<FunctionBlock> functions;

    public Ast(){
        root = new Block();
        root.setLvl(-1);
        current = root;
        ConstantDefBlock constBlock = new ConstantDefBlock();
        ((Block)current).addBodyPart(constBlock);
        cnst = constBlock;
        functions = new ArrayList<>();
    }

    public boolean addCode(String codeLine) throws UnknownInstructionException, SyntaxException, UnknownFunctionCallException{

        boolean finished = false;


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



    public void addInstruct(String[] tokens) throws UnknownInstructionException, SyntaxException, UnknownFunctionCallException{

        Queue<NodeAVL> newNodes = new ArrayDeque<>();        //Stack to add at the end all the new Nodes
        Queue<String> newInstruct = new ArrayDeque<>();        //Stack to add at the end all the new Nodes
        //NodeAVL node = null;
        boolean correct = true;
        int finishedBlocks = 0;


        if(tokens[0] == "") return;     //Coment line (no tokens)

        if(tokens.length == 5){
            //If it is not conditional it is function
            if(tokens[0].compareToIgnoreCase("funcio") == 0){
                tokens[2] = tokens[2].substring(1, tokens[2].length()-1);
                Types type = Types.getType(tokens[4]);

                NodeAVL newFunct = new FunctionBlock(tokens[1], functionParamsParser(tokens[2]), type);
                newNodes.add(newFunct);
                functions.add((FunctionBlock)newFunct);    //Add the function to the function list of the AST
                currentFunct = newFunct;
            }  
            else{
                correct = false;
            }
        }
        else if(tokens.length == 3)
        {

            //Conditional structure
            String cond = replaceFormatExpresion(tokens[1]);
            //Replace "()" to " "
            StringBuilder strBuild = new StringBuilder(cond);
            strBuild.setCharAt(cond.length()-1, ' ');
            strBuild.setCharAt(0, ' ');
            cond = strBuild.toString();
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
                NodeAVL newFunct = new FunctionBlock(tokens[1], functionParamsParser(tokens[2]));
                newNodes.add(newFunct);
                functions.add((FunctionBlock)newFunct);    //Add the function to the function list of the AST
                currentFunct = newFunct;
            }
            //Structure type definition
            else if(tokens[2].compareToIgnoreCase("registre") == 0){
                newNodes.add(new StructureBlock(tokens[0]));
            }
            else{
                correct = false;
            }

        } 
        else if(tokens.length == 2){

            //Conditional structure
            String cond = replaceFormatExpresion(tokens[1]);
            if(cond.length() >= 3){
                cond = cond.substring(1, cond.length()-1);
            }
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
            if(tokens[0].compareTo("algorisme") == 0){

                NodeAVL newFunct = new FunctionBlock("main", Types.INT);
                newNodes.add(newFunct);
                currentFunct = newFunct;

            } 
            
            //Do while block
            else if(tokens[0].compareToIgnoreCase("fer") == 0) newNodes.add(new DoWhileBlock());
            
            //Variable definition block
            else if(tokens[0].compareToIgnoreCase("var") == 0) newNodes.add(new VarDefBlock());

            //Constant variable definition block
            else if(tokens[0].compareToIgnoreCase("const") == 0){
                this.lastCurrent = this.current;
                this.current = this.cnst;  //Go to the const definition space
            } 

            //Structure definition block
            else if(tokens[0].compareToIgnoreCase("tipus") == 0) newNodes.add(new StructureDefBlock());


            //Constant, variable and starting code blocks (ignore)
            else if((tokens[0].compareToIgnoreCase("inici") == 0)){
                    //Do nothing to avoid else case
            }

            //Finished block
            else if(tokens[0].charAt(0) == 'f'){
                if(((tokens[0].compareToIgnoreCase("fconst") == 0) && (current instanceof ConstantDefBlock))){
                    this.current = this.lastCurrent;    //Return to the previous node before changing to constant space
                }
                else if(((tokens[0].compareToIgnoreCase("fsi") == 0) && (current instanceof IfBlock)) ||
                   ((tokens[0].compareToIgnoreCase("fmentre") == 0) && (current instanceof WhileBlock)) ||
                   ((tokens[0].compareToIgnoreCase("ffuncio") == 0) && (current instanceof FunctionBlock)) ||
                   ((tokens[0].compareToIgnoreCase("faccio") == 0) && (current instanceof FunctionBlock)) ||
                   ((tokens[0].compareToIgnoreCase("falgorisme") == 0) && (current instanceof FunctionBlock)) ||
                   ((tokens[0].compareToIgnoreCase("fvar") == 0) && (current instanceof VarDefBlock)) ||
                   ((tokens[0].compareToIgnoreCase("ftipus") == 0) && (current instanceof StructureDefBlock)) ||
                   ((tokens[0].compareToIgnoreCase("fregistre") == 0) && (current instanceof StructureBlock))){

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

        //Not defined lenght instructions
        if(!correct){
            correct = true;

            //Declaration instruction
            if(DeclarationInstruct.isDeclaration(tokens)){

                DeclarationInstruct decInstr = DeclarationInstruct.getDeclarationInstruct(tokens);
                newNodes.add(decInstr);
                

            }
            //Assignation instruction
            else if(tokens.length >= 3 && ((tokens[1].compareTo(":=") == 0) || (tokens[1].compareTo("<-") == 0))){
                tokens[0] = replaceFormatExpresion(tokens[0]);
                String assignated = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));
                assignated = replaceFormatExpresion(assignated);
                NodeAVL newNode;
                //If in Constant Definition Block add to the constants space of AST
                if(current instanceof ConstantDefBlock){
                    newNode = new DefineInstruct(tokens[0], assignated);
                }
                else{   //If not Constant definition Block
                    newNode = new AssignationInstruct(tokens[0], assignated);
                }
                newNodes.add(newNode);
            }
            //Return instruction
            else if(tokens[0].compareToIgnoreCase("retorna") == 0){
                String retur = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));
                retur = replaceFormatExpresion(retur);
                newNodes.add(new ReturnInstruct(retur));
            }
            //Switch structure case
            else if((tokens.length >= 2 && tokens[1].compareToIgnoreCase(":") == 0) || (tokens.length >= 3 && tokens[2].compareToIgnoreCase(":") == 0) ){
                if(current instanceof SwitchBlock || current instanceof SwitchCaseBlock){
                    boolean defaultCase = false;
                    if(current instanceof SwitchCaseBlock) finishedBlocks = 1;
                    //If default case move all tokens one position to left because "default" in pseudo uses two tokens
                    if((tokens[0].compareToIgnoreCase("altre") == 0) &&
                       (tokens[1].compareToIgnoreCase("cas") == 0)){
                        defaultCase = true;
                        newNodes.add(new SwitchCaseBlock());    //default case block
                    }
                    else{
                        newNodes.add(new SwitchCaseBlock(tokens[0]));
                    }
                    if((!defaultCase && tokens.length > 2) || (defaultCase && tokens.length > 3)){ 
                        int index = 2;
                        if(defaultCase) index = 3;
                        String instruct = String.join(" ", Arrays.copyOfRange(tokens, index, tokens.length));
                        newInstruct.add(instruct);
                    }
                }
                else{   //If case but not in Switch block
                    throw new SyntaxException("missing \"opcio\" structure to create case");
                }
            }
            //Function Call
            else if((tokens.length >= 2 && tokens[1].charAt(0) == '(' && tokens[1].charAt(tokens[1].length()-1) == ')') ||
                    (tokens.length >= 1 && tokens[0].contains("(") && (tokens[0].charAt(tokens[0].length()-1) == ')'))){
                        
                String cond = "";
                if(tokens.length >= 2) cond = replaceFormatExpresion(tokens[1]);
                else if(tokens.length >= 1) cond = replaceFormatExpresion(tokens[0]);
                cond = cond.substring(1, cond.length()-1);
                String[] params = cond.split(",");
                ArrayList<String> paramsCorrect = getParamsCorrected(tokens[0], params);
                newNodes.add(new FunctionCallInstruct(tokens[0], paramsCorrect));
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
            
            fw.write("#include <stdbool.h>");
            fw.newLine();

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


    public String printCodeString(){

        String sw = "";

        ArrayList<String> code = new ArrayList<>();
        
        sw = "#include <stdbool.h>\n";

        root.printNode(code);

        for(String s : code){ 
            sw += s + "\n";
        }

        return sw;

        
    }



    public void loadCodeFromFile(String path) throws UnknownFunctionCallException, UnknownInstructionException, SyntaxException, IOException{

        BufferedReader fr = new BufferedReader(new FileReader(path));

        int lineCount = 1;

        try{

            String nextLine = fr.readLine();

            while(nextLine != null){
                nextLine = nextLine.trim();
                if(nextLine.compareTo("") != 0){
                    this.addCode(nextLine);
                }
                nextLine = fr.readLine();
                lineCount ++;
            }


        }
        catch(UnknownFunctionCallException e){
            throw new UnknownFunctionCallException(e + "[Line " + lineCount + "]");
        }
        catch(UnknownInstructionException e){
            throw new UnknownInstructionException(e + "[Line " + lineCount + "]");
        }
        catch(SyntaxException e){
            throw new SyntaxException(e + "[Line " + lineCount + "]");
        }
        finally{
            fr.close();
        }
            

    }


    public void loadCode(String code) throws UnknownFunctionCallException, UnknownInstructionException, SyntaxException, IOException{
        BufferedReader br = new BufferedReader(new StringReader(code));

        int lineCount = 1;

        try{

            String nextLine = br.readLine();

            while(nextLine != null){
                nextLine = nextLine.trim();
                if(nextLine.compareTo("") != 0){
                    this.addCode(nextLine);
                }
                nextLine = br.readLine();
                lineCount ++;
            }


        }
        catch(UnknownFunctionCallException e){
            throw new UnknownFunctionCallException(e + "[Line " + lineCount + "]");
        }
        catch(UnknownInstructionException e){
            throw new UnknownInstructionException(e + "[Line " + lineCount + "]");
        }
        catch(SyntaxException e){
            throw new SyntaxException(e + "[Line " + lineCount + "]");
        }

    }




    private String replaceFormatExpresion(String line) throws UnknownFunctionCallException, SyntaxException{

        line = line.replace(" i ", " && ");
        line = line.replace(" o ", " || ");
        line = line.replace("=", "==");
        line = line.replaceAll("\\bdiv\\b", "/");   //Replace "div" with "/" only when is not in a word
        line = line.replaceAll("\\bno\\b", "!");   //Replace "no" with "!" only when is not in a word
        line = line.replace("! ", "!");            //Special case
        line = line.replaceAll("\\bcert\\b", "true");   //Replace "cert" with "true" only when is not in a word
        line = line.replaceAll("\\bfals\\b", "false");   //Replace "fals" with "false" only when is not in a word


        line = getInLineFunctionCallCorrected(line);

        //Add * to all IO parameters
        if((currentFunct instanceof FunctionBlock) && currentFunct != null){
            ArrayList<String> params = ((FunctionBlock)currentFunct).getIOParamsNames();
            if(params != null){
                for(String p : params){
                    line = line.replaceAll("\\b" + p + "\\b", "*" + p);
                }
            }
        }


        return line;
    }

    private static String getExpectedFinishBlock(NodeAVL block){
        String finish = "";
        if(block instanceof IfBlock) finish = "\"fsi\"";
        else if (block instanceof WhileBlock) finish = "\"fmentre\"";
        else if (block instanceof FunctionBlock) finish = "\"ffuncio / faccio / falgorisme\"";
        else if (block instanceof VarDefBlock) finish = "\"fvar\"";
        else finish = "nothing";
        return finish;
    }

    private static ArrayList<String> splitTokens(String text) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean insideParentheses = false;
        int parenthesesLevel = 0;

        char nextChar, prevChar, c;
        nextChar = prevChar = ' ';

        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);
            if(i < text.length() - 1) nextChar = text.charAt(i+1);
            if(currentToken.length() > 0 ) prevChar = currentToken.charAt(currentToken.length()-1);
            else if(tokens.size() > 0 && tokens.getLast().length() > 0) prevChar = tokens.getLast().charAt(tokens.getLast().length()-1);

            if (c == '/' && nextChar == '/'){           //Coment line
                tokens.add(currentToken.toString());
                return tokens;
            }
            if (c == '(') {

                if (currentToken.length() > 0 && !insideParentheses) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
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
            } 
            //Space separation
            else if((c == ' ' && (Character.isLetter(nextChar) || Character.isDigit(nextChar)) && isValidChar(prevChar)) && !insideParentheses){
                tokens.add(currentToken.toString());
                currentToken.setLength(0);
            }
            //Assignation separation            
            else if(( c == '<') && !insideParentheses){
                tokens.add(currentToken.toString());
                currentToken.setLength(0);
                currentToken.append(c);
            }
            else if((c == '-' && prevChar == '<') && !insideParentheses){
                currentToken.append(c);
                tokens.add(currentToken.toString());
                currentToken.setLength(0);
            }
            //Declaration or switch case (":")
            else if(c != ' ' && (c == ':' || prevChar == ':') && !insideParentheses){
                tokens.add(currentToken.toString());
                currentToken.setLength(0);
                currentToken.append(c);
            }
            else{
                if(c != ' ' || insideParentheses) currentToken.append(c);
            }

        }

        // Add the last token if any
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }

    private static boolean isSeparatorChar(char c, char nextChar, String prevToken){
        char prevChar = ' ';

        return false;
    }

    private static boolean isValidChar(char c){
        return Character.isLetter(c) || Character.isDigit(c) || c == ']';
    }

    private static int isDeclaration(String[] tokens){
        boolean found = false;
        int i = tokens.length-1;
        //Find ":"
        while(!found && i>=0){
            found = tokens[i].contains(":");
            i--;
        }
        i ++;

        if(found){
            int diff = (tokens.length - (i+1));
            if(diff > 1){   //check if it's table, because it may be a switch case
                found = false;
                int j = tokens.length-1;
                while(!found && j>=0){
                    found = tokens[j].contains("taula");
                    j--;
                }

                if(!found) i = -1;
            }
            else if(diff == 0) i = -1;      //it must be switch case
        }
        else{
            i = -1;
        }

        return i;
    }


    private ArrayList<String> getParamsCorrected(String functName, String[] params) throws UnknownFunctionCallException, SyntaxException {
        //Check if function exists
        boolean found = false;
        int i = 0;
        while(!found && (i < this.functions.size())){
            //Check for every function declared if matches to the current function call
            found = this.functions.get(i).getName().compareToIgnoreCase(functName) == 0;
            i++;
        }
        if(!found) throw new UnknownFunctionCallException(functName);
        i --;

        FunctionBlock currentFunct = this.functions.get(i);
        ArrayList<Param> paramsFunct = currentFunct.getParams();  //Get the function

        //Check for every parameter if needed IO indication (&)
        i = 0;
        while(i < params.length && i < paramsFunct.size()){
            //TODO: Check if variable is also IO param in function
            if(paramsFunct.get(i).isIO()) params[i] = "&" + "(" + params[i] + ")";
            i++;
        }
        if((i != paramsFunct.size()) || (i != params.length)) throw new SyntaxException("number of parameters not maching the function definition");


        ArrayList<String> retParams = new ArrayList<>();
        for(int j = 0; j < params.length; j++){
            retParams.add(params[j]);
        }

        return retParams;
    }

    private String getInLineFunctionCallCorrected(String codeLine) throws UnknownFunctionCallException, SyntaxException{
        StringBuilder line = new StringBuilder();
        StringBuilder functName = new StringBuilder();
        boolean validChar = false;

        for(int i = 0; i < codeLine.length(); i++){
            char currChar = codeLine.charAt(i);
            if((i != codeLine.length()-1) && (validChar && currChar == '(') && (functName.length() != 0)){
                ArrayList<String> params = new ArrayList<>();
                StringBuilder param = new StringBuilder();

                int j = i+1;
                currChar = codeLine.charAt(j);
                while(currChar != ')' && j < codeLine.length()){
                    if(currChar == ','){
                        params.add(param.toString());
                        param.setLength(0);
                    }
                    else{
                        param.append(currChar);
                    }

                    j++;
                    currChar = codeLine.charAt(j);
                }
                params.add(param.toString());

                String[] paramsArray = new String[params.size()];
                for(int k = 0; k < params.size(); k++) paramsArray[k] = params.get(k);
                
                String name = functName.toString();
                ArrayList<String> correctParams = getParamsCorrected(functName.toString(), paramsArray);
                String joinedParams = "";
                for(int k = 0; k < correctParams.size(); k++){
                    joinedParams += correctParams.get(k);
                    if(k != correctParams.size()-1) joinedParams += ", ";
                }

                line.append("(" + joinedParams + ")");
                i = j;
                functName.setLength(0);


            }
            else{
                if((currChar >= 'a' && currChar <= 'z') || (currChar >= 'A' && currChar <= 'Z') || (currChar >= '0' && currChar <= '9') ){
                    functName.append(currChar);
                    validChar = true;
                }
                else if(currChar != ' '){
                    functName.setLength(0);
                    validChar = false;
                }

                line.append(currChar);
            }
        }

        return line.toString();
    }


    private static ArrayList<String>[] functionParamsParser(String tokenParam){

        tokenParam = tokenParam.substring(0, tokenParam.length());
        String[] subtokens = tokenParam.split(",");
        ArrayList<String>[] tokensParamsList = new ArrayList[subtokens.length];
        for(int i = 0; i < subtokens.length; i++){
            tokensParamsList[i] = splitTokens(subtokens[i]);
        }

        return tokensParamsList;
    }




}