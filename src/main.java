/**
 * Classe main
 * Demande au user quel algo il veut utiliser, génère l'objet qui à l'algo d'implementé, puis éxecute l'algorithme.
 * Date: 16 Mai 2020
 * Authors: Adam Zahiri, Sébastien Fauteux, Rabah Boubchir
 */

public class main {
    public static void main(String[] args) {

//        String choixAlgo = args[0].replace("-","");
//        String choixCompress = args[1].replace("-","");
//        String fileIn = args[2];
//        String fileOut = args[3];
        
        String choixAlgo = "lzw";
        String choixCompress = "d";
        String fileIn = "C:/Users/Swicy/Desktop/lab1-320-algos-compress/src/testc.txt";
        String fileOut = "C:/Users/Swicy/Desktop/lab1-320-algos-compress/src/testd.txt";
        Utilitaire.validator(choixAlgo, choixCompress, fileIn, fileOut);
    }
}
