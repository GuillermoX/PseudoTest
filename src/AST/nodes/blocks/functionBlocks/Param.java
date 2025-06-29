package AST.nodes.blocks.functionBlocks;

import AST.Enums.Types;
import AST.exceptions.SyntaxException;
import AST.nodes.blocks.Block;
import AST.nodes.instructions.DeclarationInstruct;

public class Param{
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
