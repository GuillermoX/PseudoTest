package AST.nodes.blocks.functionBlocks;

import java.util.ArrayList;
import java.util.Arrays;

import AST.Enums.Types;
import AST.exceptions.SyntaxException;
import AST.nodes.blocks.Block;
import AST.nodes.instructions.DeclarationInstruct;

public class FunctionBlock extends Block {
    
    private static class Param{
        private DeclarationInstruct dec;
        private boolean isIO;   //To know if it's In/Out parameter

        public Param(DeclarationInstruct dec, boolean isIO){
            this.dec = dec;
            this.isIO = isIO;
        }

        public DeclarationInstruct getDeclaration(){
            return dec;
        }
        


        public boolean isIO(){
            return isIO;
        }
    }

    private String name;
    private ArrayList<Param> params;       //TODO: Use params
    private Types type; 

    
    public FunctionBlock(String name, String params, Types type) throws SyntaxException{
        super();
        this.name = name;
        this.params = new ArrayList<>();
        this.parseParams(params);
        this.type = type;
        //TODO: TypeError
    }


    public FunctionBlock(String name, String params) throws SyntaxException{
        super();
        this.name = name; 
        this.params = new ArrayList<>();
        this.parseParams(params);
        this.type = Types.VOID;
    }

    public FunctionBlock(String name){
        super();
        this.name = name;
        this.type = Types.VOID;
    }

    public ArrayList<String> getIOParamsNames(){
        ArrayList<String> paramNames = new ArrayList<>();

        for(Param p : this.params){
            if(p.isIO) paramNames.add(p.getDeclaration().getVarName(0));
        }

        return paramNames;
    }


    @Override
    public void printNode(ArrayList<String> code) {
        
        String ident = super.getLvlIdent(super.getLvl());
        String cType = type.print();

        String paramStr = "";
        for(int i = 0; i < this.params.size(); i ++){
            paramStr += this.params.get(i).getDeclaration().printAsParam(this.params.get(i).isIO());
            if(i != (this.params.size()-1)) paramStr += ", ";
        }

        //TODO: Add the parameters
        code.add(ident + cType + " " + name + "(" + paramStr + ")");
        code.add(ident + "{");

        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

    }


    private void parseParams(String paramStr) throws SyntaxException{

        if(paramStr.compareTo("") != 0){

            String[] paramsArr = paramStr.split(",");

            for(int i = 0; i < paramsArr.length; i++){
                boolean isInOut = false;

                paramsArr[i] = paramsArr[i].replaceFirst("^\\s", "");   //Delete first white spaces
                paramsArr[i] = paramsArr[i].replaceAll("\\s+", " ");    //Delete repeated white spaces
                String[] tokens = paramsArr[i].split(" ");
                if(tokens[0].compareToIgnoreCase("var") == 0){      //If it's IO
                    isInOut = true;
                    tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
                }

                DeclarationInstruct dec = DeclarationInstruct.getDeclarationInstruct(tokens);   //Get the declaration of the param 
                Param param = new Param(dec, isInOut);
                this.params.add(param);

            }
        }

    }


}
