import Bit.BitInputStream;
import Bit.BitOutputStream;

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
        
//        String choixAlgo = "lzw";
//        String choixCompress = "d";
//        String fileIn = "C:/Users/Swicy/Desktop/lab1-320-algos-compress/src/biblec.txt";
//        String fileOut = "C:/Users/Swicy/Desktop/lab1-320-algos-compress/src/bibled.txt";
//        Utilitaire.validator(choixAlgo, choixCompress, fileIn, fileOut);

        String choixAlgo = "huff";
        String choixCompress = "c";
        String fileIn = "D:/lab1-320-algos-compress/src/exemple.txt";
        String fileOut = "D:/lab1-320-algos-compress/src/Test/Huff/compressed_exemple.txt";
        Utilitaire.validator(choixAlgo, choixCompress, fileIn, fileOut);

//        String choixAlgo = "huff";
//        String choixCompress = "d";
//        String fileIn = "D:/lab1-320-algos-compress/src/Test/Huff/compressed_exemple.txt";
//        String fileOut = "D:/lab1-320-algos-compress/src/Test/Huff/uncompressed_exemple.txt";
//        Utilitaire.validator(choixAlgo, choixCompress, fileIn, fileOut);

    }
}
