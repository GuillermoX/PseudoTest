package AST.nodes.blocks.functionBlocks;

import java.util.ArrayList;
import java.util.Arrays;

import AST.Enums.Types;
import AST.exceptions.SyntaxException;
import AST.nodes.blocks.Block;
import AST.nodes.instructions.DeclarationInstruct;
import AST.nodes.blocks.functionBlocks.Param;

public class FunctionBlock extends Block {
    

    private String name;
    private ArrayList<Param> params;       //TODO: Use params
    private Types type; 
    private boolean isDefOnly;

    
    public FunctionBlock(String name, ArrayList<String>[] params, Types type) throws SyntaxException{
        super();
        this.name = name;
        this.params = new ArrayList<>();
        this.parseParams(params);
        this.type = type;
        //TODO: TypeError
    }


    public FunctionBlock(String name, ArrayList<String>[] params) throws SyntaxException{
        super();
        this.name = name; 
        this.params = new ArrayList<>();
        this.parseParams(params);
        this.type = Types.VOID;
    }


    public FunctionBlock(String name) throws SyntaxException{
        super();
        this.name = name; 
        this.params = null;
        this.type = Types.VOID;
    }


    public FunctionBlock(String name, Types type) throws SyntaxException{
        super();
        this.name = name;
        this.params = null;
        this.type = type;
        //TODO: TypeError
    }


    public String getName(){
        return this.name;
    }

    public ArrayList<Param> getParams(){
        return params;
    }

    public ArrayList<String> getIOParamsNames(){
        ArrayList<String> paramNames = new ArrayList<>();

        if(params != null){
            for(Param p : this.params){
                if(p.isIO()) paramNames.add(p.getDeclaration().getVarName(0));
            }
        }

        return paramNames;
    }

    public boolean isDefOnly(){
        return this.isDefOnly;
    }


    public void setDefOnly(boolean set){
        isDefOnly = set;
    }





    @Override
    public void printNode(ArrayList<String> code) {
        
        String ident = super.getLvlIdent(super.getLvl());
        String cType = type.print();


        String paramStr = "";
        if(params != null){

            for(int i = 0; i < this.params.size(); i ++){
                paramStr += this.params.get(i).getDeclaration().printAsParam(this.params.get(i).isIO());
                if(i != (this.params.size()-1)) paramStr += ", ";
            }
        }

        String def = ident + cType + " " + name + "(" + paramStr + ")";
        if(isDefOnly) def += ";";
        code.add(def);

        if(!isDefOnly){
            code.add(ident + "{");

            int max = super.numBodyParts();
            for(int i = 0; i < max; i++){
                super.getBodyPart(i).printNode(code);
            }

            code.add(ident + "}");
        }

    }


    private void parseParams(ArrayList<String>[] params) throws SyntaxException{

                boolean isInOut;
                for(int i = 0; i < params.length; i++){
                    if(params[i].get(0).compareTo("var") == 0){
                        isInOut = true;
                        params[i].remove(0);
                    }                    
                    else{
                        isInOut = false;
                    }

                    String[] tokensParams = new String[params[i].size()];
                    int j = 0;
                    for(String s : params[i]){
                        tokensParams[j] = s;
                        j++;
                    }
                    DeclarationInstruct dec = DeclarationInstruct.getDeclarationInstruct(tokensParams);   //Get the declaration of the param 
                    Param param = new Param(dec, isInOut);
                    this.params.add(param);
                }


    }


}
