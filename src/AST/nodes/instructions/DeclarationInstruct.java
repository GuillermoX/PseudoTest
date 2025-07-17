package AST.nodes.instructions;

import AST.nodes.NodeAVL;
import AST.Enums.Types;
import AST.exceptions.SyntaxException;

import java.util.ArrayList;
import java.util.Arrays;

public class DeclarationInstruct extends NodeAVL {
    ArrayList<String> vars; 
    Types type;
    String typeName;                //Name for the type in case it's a struct
    ArrayList<String> dims;         //In case the new(s) variable(s) is an array


    //Multiple variable declaration
    public DeclarationInstruct(ArrayList<String> vars, Types type){
        this.vars = vars;
        this.type = type;
        this.dims = null;
    }

    //Array variable declaration
    public DeclarationInstruct(ArrayList<String> vars, Types type, ArrayList<String> dims){
        this.vars = vars;
        this.type = type;
        this.dims = dims;
    }

    public String getVarName(int index){
        try{
            return vars.get(index);

        }
        catch(IndexOutOfBoundsException e){
            return "";
        }
    }

    public ArrayList<String> getVars(){
        return this.vars;
    }

    public Types getType(){
        return type;
    }

    public void setTypeName(String name){
        this.typeName = name;
    }


    public void printNode(ArrayList<String> code){
        String ident = super.getLvlIdent(super.getLvl());

        //Add the dimensions into one String
        String dimStr = "";
        if(this.dims != null){

            for(String d : this.dims){
                dimStr += "[" + d + "]";
            }
        }

        //Add the variables in one String
        String varStr = "";
        for(int i = 0; i < vars.size(); i++){
            varStr += vars.get(i);
            if(this.dims != null){
                varStr += dimStr;
            }
            
            if(i != vars.size()-1) varStr += ", ";

        }

        String typePrint = type.print();
        if(this.type == Types.STRUCT) typePrint = typeName;

        code.add(ident + typePrint + " " + varStr + ";");
    }


    public String printAsParam(boolean isIO){

        //Add the dimensions into one String
        String dimStr = "";
        if(this.dims != null){

            for(String d : this.dims){
                dimStr += "[" + d + "]";
            }
        }

        //Add the variables in one String
        String varStr = "";
        for(int i = 0; i < vars.size(); i++){
            varStr += vars.get(i);
            if(this.dims != null){
                varStr += dimStr;
            }
            
            if(i != vars.size()-1) varStr += ", ";

        }

        String retStr = type.print();
        if(this.type == Types.STRUCT) retStr = typeName;
        if(isIO) retStr += "*";
        retStr += " " + varStr;

        return retStr;
 
    }


    public static DeclarationInstruct getDeclarationInstruct(String[] tokens) throws SyntaxException{

        int posDec = getDeclarationDelim(tokens);
        //Remove ":" from tokens
        tokens[posDec] = tokens[posDec].replace(":", "");

        
        //Get all variables into array
        String[] vars = String.join("", Arrays.copyOfRange(tokens, 0, posDec+1)).split(",");
        ArrayList<String> varsArray = new ArrayList<>();
        for(int i = 0; i < vars.length; i++) varsArray.add(vars[i]);

        //Get the type of variable/s
        Types type = Types.getType(tokens[tokens.length-1]);

        ArrayList<String> tableValues = isTableDeclaration(tokens);     //TODO: Add inicializated values table

        DeclarationInstruct decInstr;
        if(tableValues == null){
            decInstr = new DeclarationInstruct(varsArray, type);
        }
        else{
            decInstr = new DeclarationInstruct(varsArray, type, tableValues);
        }

        if(type == Types.STRUCT) decInstr.setTypeName(tokens[tokens.length-1]);     //Set the name for the type of the instruction if it's a struct

        return decInstr;
    }


    public static boolean isDeclaration(String[] tokens){

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

        return i != -1;
    }

    private static int getDeclarationDelim(String[] tokens) throws SyntaxException{

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

                if(!found) throw new SyntaxException("Bad variable/parameter declaration, expected array");
            }
            else if(diff == 0) i = -1;      //it must be switch case
        }
        else{
           throw new SyntaxException("Bad variable/parameter declaration, no \":\" found"); 
        }

        return i;
    }


    private static ArrayList<String> isTableDeclaration(String[] tokens){
        boolean fi = false;
        int i = tokens.length-1;
        while(!fi && i>0){
            if(tokens[i].contains("taula")){
                return getTableValues(String.join(" ", Arrays.copyOfRange(tokens, i-1, tokens.length)));
            }
            fi = tokens[i].contains(":");
            i--;
        }

        return null;
    }

    private static ArrayList<String> getTableValues(String token){

        ArrayList<String> values = new ArrayList<>();
        boolean inBraces = false;
        String value = "";
        for(int i = 0; i<token.length(); i++){
            if(token.charAt(i) == '['){
                inBraces = true;
            }
            else if(token.charAt(i) == ']'){
                inBraces = false;
                values.add(value);
                value = "";
            }
            else if(inBraces) value += token.charAt(i);

        }

        return values;
    }
}
