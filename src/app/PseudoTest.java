package app;
import AST.*;

public class PseudoTest { 

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        Ast ast = new Ast();
        java.awt.EventQueue.invokeLater(() -> new AppGUI(ast).setVisible(true));
    }
}
