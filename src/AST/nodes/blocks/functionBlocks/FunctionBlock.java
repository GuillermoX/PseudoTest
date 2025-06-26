package AST.nodes.blocks.functionBlocks;

import java.util.ArrayList;

import AST.Enums.Types;
import AST.nodes.blocks.Block;

public class FunctionBlock extends Block {
    
    private class Param{
        private String name;
        private boolean isIO;   //To know if it's In/Out parameter

        public Param(String name, boolean isIO){
            this.name = name;
            this.isIO = isIO;
        }

        public String getName(){
            return name;
        }

        public boolean isIO(){
            return isIO;
        }
    }

    private String name;
    private String params;       //TODO: Use params
    private Types type; 

    
    public FunctionBlock(String name, String params, Types type){
        super();
        this.name = name;
        this.params = params;
        this.type = type;
        //TODO: TypeError
    }


    public FunctionBlock(String name, String params){
        super();
        this.name = name;
        this.params = params;
        this.type = Types.VOID;
    }

    public FunctionBlock(String name){
        super();
        this.name = name;
        this.type = Types.VOID;
    }

    @Override
    public void printNode(ArrayList<String> code) {
        
        String ident = super.getLvlIdent(super.getLvl());
        String cType = type.print();

        //TODO: Add the parameters
        code.add(ident + cType + " " + name + "(" + params + ")");
        code.add(ident + "{");

        int max = super.numBodyParts();
        for(int i = 0; i < max; i++){
            super.getBodyPart(i).printNode(code);
        }

        code.add(ident + "}");

    }


    private static void parseParams(String paramStr){

        String[] params = paramStr.split(",");

        for(int i = 0; i < params.length-1; i++){

            boolean paramIO = params[i].matches(".*\\bvar\\b.*");   //Check if it's IO
        }

    }


}
