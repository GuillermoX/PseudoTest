package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Queue;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URISyntaxException;

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
import app.AppGUI;
import app.PseudoTest;

public class Ast {

    private NodeAVL root;
    private NodeAVL current;
    private NodeAVL currentFunct;
    private NodeAVL lastCurrent;    //To save current when changing to other spaces
    private NodeAVL cnst;           //Constant block
    private ArrayList<FunctionBlock> functions;

    public Ast(){
        initializeAst();
    }


    /**
     * Initializes the Ast structure
     */
    public void initializeAst(){

        root = new Block();
        root.setLvl(-1);
        current = root;
        ConstantDefBlock constBlock = new ConstantDefBlock();
        ((Block)current).addBodyPart(constBlock);
        cnst = constBlock;
        functions = new ArrayList<>();
    }

    /**
     * Adds a code line to the Ast structure
     * @param String codeLine The current line of code
     * @return boolean finished True if the code has reached the last line
     */
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


    /**
     * Adds the tokenized instruction to the Ast
     * @param tokens Tokenized instruction
     * @throws UnknownInstructionException
     * @throws SyntaxException
     * @throws UnknownFunctionCallException
     */
    public void addInstruct(String[] tokens) throws UnknownInstructionException, SyntaxException, UnknownFunctionCallException{

        Queue<NodeAVL> newNodes = new ArrayDeque<>();        //Stack to add at the end all the new Nodes
        Queue<String> newInstruct = new ArrayDeque<>();        //Stack to add at the end all the new Nodes
        //NodeAVL node = null;
        boolean correct = true;
        int finishedBlocks = 0;


        if(tokens[0].compareToIgnoreCase("") == 0) return;     //Coment line (no tokens)

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
                   ((tokens[0].compareToIgnoreCase("fper") == 0) && (current instanceof ForBlock)) ||
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

                if(!(this.current instanceof VarDefBlock || this.current instanceof StructureBlock)) throw new SyntaxException("needed a declaration / struct block to declare variables");
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
            //For loop 
            else if((tokens.length >= 5) && tokens[0].compareToIgnoreCase("per") == 0){
                AssignationInstruct assignInst;
                String max;
                String incr = "+1";

                int i = 0;
                boolean found = false;
                while(!found && i < tokens.length){
                    found = tokens[i].compareToIgnoreCase("fins") == 0;
                    i++;
                }
                if(!found) throw new SyntaxException("no \"fins\" delimitator in \"per\" loop");

                tokens[0] = replaceFormatExpresion(tokens[0]);
                String assignated;
                try{

                    assignated = String.join(" ", Arrays.copyOfRange(tokens, 3, i-1));
                }
                catch(IllegalArgumentException e){
                    throw new SyntaxException("\"for loop\" must have an initial assignation");
                }
                assignated = replaceFormatExpresion(assignated);
                assignInst = new AssignationInstruct(tokens[1], assignated);

                //Search for incrementation specification
                int j = i;
                found = false;
                while(!found && j < tokens.length){
                    found = tokens[j].contains("pas");
                    j++;
                }
                if(found){ 
                    tokens[j-1] = tokens[j-1].replaceAll("\\bpas\\b", "");
                    incr = String.join(" ", Arrays.copyOfRange(tokens, j-1, tokens.length-1));
                }

                max = String.join(" ", Arrays.copyOfRange(tokens, i, j-1));
                
                newNodes.add(new ForBlock(assignInst, max, incr));


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
                String functName = tokens[0];
                ArrayList<String> paramsCorrect = getParamsCorrected(functName, params);

                //Changed names to functions
                functName = changeNameFunction(functName);
                if(functName.compareTo("") == 0) functName = tokens[0];
                

                newNodes.add(new FunctionCallInstruct(functName, paramsCorrect));
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


    /**
     * Returns all the Ast structure into the form of C language
     * @return String The code translated into C 
     * @throws IOException
     * @throws UnknownFunctionCallException
     */
    public String printCodeString() throws IOException, UnknownFunctionCallException{

        String sw = "";

        ArrayList<String> code = new ArrayList<>();
        
        sw = "#include <stdbool.h>\n";
        sw += "#include <stdio.h>\n";

        root.printNode(code);

        for(String s : code){ 
            sw += s + "\n";
        }

        //Check if auxiliar functions are needed
        String auxFunct = "";
        for(FunctionBlock funct : this.functions){
            if(funct.isDefOnly()){
                try{
                    Path initCode = Paths.get(PseudoTest.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                    String path;
                    if(initCode.toString().endsWith(".jar")){
                        path = initCode.getParent().resolve("./code/" + funct.getName() + ".c").toAbsolutePath().toString();
                    }
                    else{
                        path = "./code/" + funct.getName() + ".c";
                    }

                    auxFunct += AppGUI.getStringFromFile(path); 
                }
                catch(URISyntaxException e){ throw new UnknownFunctionCallException("impossible to load array functions");}

            }
        }

        if(auxFunct.compareToIgnoreCase("") != 0){
            sw += "\n\n/* ------ Auxiliar Functions (DO NOT MODIFY) ------ */\n\n";
            sw += auxFunct;
            sw += "/* ------------------------------------------------ */";
        }

        return sw;

        
    }

    /**
     * Loads the Ast with the pseudocode from a the file specified
     * @param path  File path
     * @throws UnknownFunctionCallException
     * @throws UnknownInstructionException
     * @throws SyntaxException
     * @throws IOException
     */ 
    public void loadCodeFromFile(String path) throws UnknownFunctionCallException, UnknownInstructionException, SyntaxException, IOException{


        this.initializeAst();

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
            throw new UnknownFunctionCallException(e.getMessage() + "[Line " + lineCount + "]");
        }
        catch(UnknownInstructionException e){
            throw new UnknownInstructionException(e.getMessage() + "[Line " + lineCount + "]");
        }
        catch(SyntaxException e){
            throw new SyntaxException(e.getMessage() + "[Line " + lineCount + "]");
        }
        finally{
            fr.close();
        }
            

    }

    /**
     * Loads the Ast with the pseudocode in a String
     * @param code String The pseudocode
     * @throws UnknownFunctionCallException
     * @throws UnknownInstructionException
     * @throws SyntaxException
     * @throws IOException
     */
    public void loadCode(String code) throws UnknownFunctionCallException, UnknownInstructionException, SyntaxException, IOException{
        this.initializeAst();

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
            throw new UnknownFunctionCallException("Unknown function call: " + e.getMessage() + " [Line " + lineCount + "]");
        }
        catch(UnknownInstructionException e){
            throw new UnknownInstructionException("Unknown instruction: " + e.getMessage() + " [Line " + lineCount + "]");
        }
        catch(SyntaxException e){
            throw new SyntaxException("Syntax error: " + e.getMessage() + " [Line " + lineCount + "]");
        }

    }


    /**
     * Replaces some format differences between PseudoCode and C
     * @param line
     * @return
     * @throws UnknownFunctionCallException
     * @throws SyntaxException
     */
    private String replaceFormatExpresion(String line) throws UnknownFunctionCallException, SyntaxException{

        line = line.replace(" i ", " && ");
        line = line.replace(" o ", " || ");
        line = line.replace("=", "==");
        line = line.replaceAll("\\bdiv\\b", "/");   //Replace "div" with "/" only when is not in a word
        line = line.replaceAll("\\bmod\\b", "%");   //Replace "div" with "/" only when is not in a word
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

    /**
     * Returns the expected finishing block when finishing-block statements don't match to the current block context
     * @param block
     * @return  String Expected finishing block
     */
    private static String getExpectedFinishBlock(NodeAVL block){
        String finish = "";
        if(block instanceof IfBlock) finish = "\"fsi\"";
        else if (block instanceof WhileBlock) finish = "\"fmentre\"";
        else if (block instanceof FunctionBlock) finish = "\"ffuncio / faccio / falgorisme\"";
        else if (block instanceof VarDefBlock) finish = "\"fvar\"";
        else finish = "nothing";
        return finish;
    }

    /**
     * Splits a code line in tokens
     * @param text PseudoCode line
     * @return  ArrayList<String> Tokenized code line
     */
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
            else if(tokens.size() > 0 && tokens.get(tokens.size()-1).length() > 0) prevChar = tokens.get(tokens.size()-1).charAt(tokens.get(tokens.size()-1).length()-1);

            if (c == '$'){           //Coment line
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
    

    private static boolean isValidChar(char c){
        return Character.isLetter(c) || Character.isDigit(c) || c == ']';
    }


    /**
     * Corrects the parameters syntax depending if params are I/O or not
     * @param functName Name of the function
     * @param params    Parameters
     * @return          Parameters corrected
     * @throws UnknownFunctionCallException
     * @throws SyntaxException
     */
    private ArrayList<String> getParamsCorrected(String functName, String[] params) throws UnknownFunctionCallException, SyntaxException {

        ArrayList<String> retParams = new ArrayList<>();

        //Write function
        if(functName.compareToIgnoreCase("escriure") == 0){
            String str = "";
            for(int i = 0; i < params.length; i++){
                if(params[i].contains("\"")) str += params[i].replace("\"", "");
                else{
                    Types type = getVarType(params[i]);
                    str += Types.getFormatSpecifier(type);
                    retParams.add(params[i]);
                }
            }

            retParams.add(0,"\"" + str + "\"");
        }
        //Read function
        else if(functName.compareToIgnoreCase("llegir") == 0){
            if(params.length != 1) throw new SyntaxException("read function can only receive one parameter");

            Types type = getVarType(params[0]);
            retParams.add("\"" + Types.getFormatSpecifier(type) + "\"");
            retParams.add("&(" + params[0] + ")");
        }
        //Read from file function
        else if(functName.compareToIgnoreCase("llegir_dada_fitxer") == 0){
            if(params.length != 2) throw new SyntaxException("read from file function must receive 2 parameters");

            Types type = getVarType(params[1]);
            retParams.add(params[0]);
            retParams.add("\"" + Types.getFormatSpecifier(type) + "\"");
            retParams.add("&(" + params[1] + ")");

        }
        //Open file to read
        else if(functName.compareToIgnoreCase("obrir_fitxer_per_escriure") == 0){
            if(params.length != 1) throw new SyntaxException("open file function can only receive one parameter");

            retParams.add(params[0]);
            retParams.add("\"w\"");
        }
        //Open file to write
        else if(functName.compareToIgnoreCase("obrir_fitxer_per_llegir") == 0){
            if(params.length != 1) throw new SyntaxException("open file function can only receive one parameter");

            retParams.add(params[0]);
            retParams.add("\"r\"");
        }
        else if(isUserFunction(functName)){

            //Add library function if necessary
            addLibFunction(functName);

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
            int paramsLenght = params.length;
            if(params[0].compareToIgnoreCase("") == 0) paramsLenght --;
            if(paramsLenght != paramsFunct.size()) throw new SyntaxException("number of parameters not maching the function definition");
            else{
                i = 0;
                while(i < params.length && i < paramsFunct.size()){
                    if(paramsFunct.get(i).isIO()) params[i] = "&" + "(" + params[i] + ")";
                    //If load matrix function it's needed a cast
                    if((functName.compareToIgnoreCase("obtenir_dades_matriu") == 0) && i == 0) params[i] = "(int*)" + params[i];
                    i++;
                }
            }


            for(int j = 0; j < params.length; j++){
                retParams.add(params[j]);
            }

        }
        else{
            for(int i = 0; i < params.length; i++){
                retParams.add(params[i]);
            }
        }

        return retParams;
    }

    /**
     * Adds the library function definition in the Ast if the function called is from library
     * @param name  Name of the function called
     * @throws SyntaxException
     */
    private void addLibFunction(String name) throws SyntaxException{
        FunctionBlock newFunct = null;
        ArrayList<String>[] params = null;
        if(name.compareToIgnoreCase("obtenir_dades_vector") == 0){
            params = new ArrayList[3];
            params[0] = new ArrayList<String>();
            params[0].add("dades");
            params[0].add(":");
            params[0].add("taula[]");
            params[0].add("d'enters");
            params[1] = new ArrayList<String>();
            params[1].add("max");
            params[1].add(":");
            params[1].add("enter");
            params[2] = new ArrayList<String>();           
            params[2].add("var");
            params[2].add("n_elems");
            params[2].add(":");
            params[2].add("enter");
        }
        else if(name.compareToIgnoreCase("obtenir_dades_senti") == 0){
            params = new ArrayList[3];
            params[0] = new ArrayList<String>();
            params[0].add("dades");
            params[0].add(":");
            params[0].add("taula[]");
            params[0].add("d'enters");
            params[1] = new ArrayList<String>();
            params[1].add("max");
            params[1].add(":");
            params[1].add("enter");
            params[2] = new ArrayList<String>();
            params[2].add("senti");
            params[2].add(":");
            params[2].add("enter");
        }
        else if(name.compareToIgnoreCase("obtenir_dades_matriu") == 0){
            params = new ArrayList[5];
            params[0] = new ArrayList<String>();
            params[0].add("var");
            params[0].add("dades");
            params[0].add(":");
            params[0].add("enter");
            params[1] = new ArrayList<String>();
            params[1].add("maxf");
            params[1].add(":");
            params[1].add("enter");
            params[2] = new ArrayList<String>();
            params[2].add("maxc");
            params[2].add(":");
            params[2].add("enter");
            params[3] = new ArrayList<String>();
            params[3].add("var");
            params[3].add("nf");
            params[3].add(":");
            params[3].add("enter");
            params[4] = new ArrayList<String>();
            params[4].add("var");
            params[4].add("nc");
            params[4].add(":");
            params[4].add("enter");
            
        }


        if(params != null){
            newFunct = new FunctionBlock(name, params);
            newFunct.setDefOnly(true);
            this.functions.add(0,newFunct);
            ((Block)this.root).getBody().add(0,newFunct);
        }
    }

    /**
     * Checks if the function specified is a user function
     * @param name
     * @return
     */
    private static boolean isUserFunction(String name){
        return !(name.compareToIgnoreCase("obrir_fitxer_per_llegir") == 0 ||
                name.compareToIgnoreCase("obrir_fitxer_per_escriure") == 0 ||
                name.compareToIgnoreCase("obrir_fitxer_llegir") == 0 ||
                name.compareToIgnoreCase("obrir_fitxer_escriure") == 0 ||
                name.compareToIgnoreCase("final_fitxer") == 0 ||
                name.compareToIgnoreCase("tancar_fitxer") == 0);
    }

    /**
     * Returns the C name for I/O functions used in pseudo
     * @param functName PseudoCode function name
     * @return          C name of the function
     */
    private static String changeNameFunction(String functName){
        if(functName.compareToIgnoreCase("escriure") == 0) return "printf";
        else if(functName.compareToIgnoreCase("llegir") == 0) return "scanf";
        else if(functName.compareToIgnoreCase("final_fitxer") == 0) return "feof";
        else if(functName.compareToIgnoreCase("tancar_fitxer") == 0) return "fclose";
        else if(functName.compareToIgnoreCase("obrir_fitxer_per_llegir") == 0 ||
                        functName.compareToIgnoreCase("obrir_fitxer_per_escriure") == 0 ||
                        functName.compareToIgnoreCase("obrir_fitxer_llegir") == 0 ||
                        functName.compareToIgnoreCase("obrir_fitxer_escriure") == 0) return "fopen"; 
        else if(functName.compareToIgnoreCase("llegir_dada_fitxer") == 0) return "fscanf";
        else return "";
    }

    /**
     * Checks the type of a variable using it's name and definition
     * @param var   Variable name
     * @return      Variable Type
     * @throws SyntaxException  If variable not declared
     */
    private Types getVarType(String var) throws SyntaxException{
        Types type = Types.STRUCT;  //Initialize to avoid return error (no impact)
        var = var.replace(" ", "");

        ArrayList<NodeAVL> functBlocks = ((FunctionBlock)this.currentFunct).getBody();
        boolean found = false;

        //Find variable def block
        int i = 0;
        while((i < functBlocks.size()) && !found){
            found = functBlocks.get(i) instanceof VarDefBlock;
            i++;
        }
        if(!found) throw new SyntaxException("variable \"" + var + "\" not defined");

        //Find variable
        ArrayList<NodeAVL> definitionBody = ((Block)functBlocks.get(i-1)).getBody();
        i = 0;
        found = false;
        while((i < definitionBody.size()) && !found){
            NodeAVL node = definitionBody.get(i);
            if(node instanceof DeclarationInstruct){
                ArrayList<String> vars = ((DeclarationInstruct)node).getVars();
                int j = 0;
                while(j < vars.size() && !found){
                    found = ((DeclarationInstruct)node).getVarName(j).compareToIgnoreCase(var) == 0;
                    j++;
                }
                type = ((DeclarationInstruct)node).getType();
            }
            i++;
        }
        if(!found) throw new SyntaxException("variable \"" + var + "\" not defined");

        return type;

    }

    /**
     * Checks for a function call inside a code line and corrects it
     * @param codeLine  Pseudocode line
     * @return  String The corrected line
     * @throws UnknownFunctionCallException
     * @throws SyntaxException
     */
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
                
                ArrayList<String> correctParams = getParamsCorrected(functName.toString(), paramsArray);
                String joinedParams = "";
                for(int k = 0; k < correctParams.size(); k++){
                    joinedParams += correctParams.get(k);
                    if(k != correctParams.size()-1) joinedParams += ", ";
                }

                line.append("(" + joinedParams + ")");
                
                String name = changeNameFunction(functName.toString());
                if(name.compareToIgnoreCase("") != 0){
                    int start = line.indexOf(functName.toString());
                    int end = start + functName.toString().length();
                    line.replace(start, end, name);
                }
                i = j;
                functName.setLength(0);


            }
            else{
                if((currChar >= 'a' && currChar <= 'z') || (currChar >= 'A' && currChar <= 'Z') || (currChar >= '0' && currChar <= '9')  || currChar == '_'){
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

        String lineStr = line.toString();

        return lineStr;
    }

    /**
     * Splits the String of parameters of a functions in individual parameter strings
     * @param tokenParam    All parameters in a String
     * @return ArrayList<String>    Parameters split
     */
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