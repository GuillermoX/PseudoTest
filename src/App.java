
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import AVL.Avl;

public class App {
    public static void main(String[] args) throws Exception {
        BufferedReader fr = new BufferedReader(new FileReader(args[0]));

        Avl avl = new Avl();

        boolean finished = false;
        String nextLine = fr.readLine();
        while(nextLine != null){
            nextLine = nextLine.replaceFirst("^\\s+", "");
            if(nextLine.compareTo("") != 0){
                finished = avl.addCode(nextLine);
            }
            nextLine = fr.readLine();
        }

        avl.printCode();

    }
}
