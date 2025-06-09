package AST.nodes.blocks;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import AST.exceptions.SyntaxException;
import AST.exceptions.UnknownInstructionException;
import AST.nodes.NodeAVL;
import AST.Ast;

public class Block extends NodeAVL{
   
    private ArrayList<NodeAVL> body;    

    public Block(){
        super();
        body = new ArrayList<>();
    }

    public int addBodyPart(String newCodeLine, Ast ast) throws UnknownInstructionException, SyntaxException{
        
        int ret = 0;

        //Get tokens 
        ArrayList<String> tokensList = new ArrayList<>();
        tokensList = splitTokens(newCodeLine);

        String[] tokens = new String[tokensList.size()];
        for(int i = 0; i < tokensList.size(); i++){
            String tokenString = tokensList.get(i);
            tokens[i] = tokenString.replaceAll("^\\(|\\)$", "");
        } 

        //Get new node type
        NodeAVL node = ast.getNode(tokens);


        if(node == null){
            return -1;
        }
        else{
            node.setDad(this);
            node.setLvl(super.getLvl()+1);
            body.add(node);
            if(node instanceof Block) ret = 1;
        }

        return ret; 
    }

    public NodeAVL getBodyPart(int i){
        return body.get(i);
    }

    public int numBodyParts(){
        return body.size();
    }



    public void printNode(ArrayList<String> code){

        int max = numBodyParts();
        for(int i = 0; i < max; i++){
            getBodyPart(i).printNode(code);
        }

    }

    public static ArrayList<String> splitTokens(String text) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean insideParentheses = false;
        int parenthesesLevel = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '(') {
                if (parenthesesLevel > 0) currentToken.append(c);
                parenthesesLevel++;
                insideParentheses = true;
            } else if (c == ')') {
                parenthesesLevel--;
                if (parenthesesLevel == 0) {
                    insideParentheses = false;
                    tokens.add("(" + currentToken.toString() + ")");
                    currentToken.setLength(0);
                } else {
                    currentToken.append(c);
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
