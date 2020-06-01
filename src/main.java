import java.util.Scanner;

/**
 * Classe main
 * Demande au user quel algo il veut utiliser, génère l'objet qui à l'algo d'implementé, puis éxecute l'algoryithme.
 * Date: 16 Mai 2020
 * Authors: Adam Zahiri, Sébastien Fauteux, Rabah Boubchir
 */

public class main {
    public static void main(String[] args) {
        String filePath = "D:/lab1-320-algos-compress/src/loremipsum.txt";
        String compressedFilePath = "D:/lab1-320-algos-compress/src/compressedHuff.txt";
        String decompressedFilePath = "D:/lab1-320-algos-compress/src/decompressedHuff.txt";
        String convertedTxtFile = Utilitaire.readtxt(filePath);

        Scanner algoChoisi = new Scanner(System.in);
        System.out.println("Choisissez un algorithme: lzw, huff ou opt.");
        String choixAlgo = algoChoisi.nextLine();
        System.out.println("Voulez-vous effectuer un compress ou un decompress?");
        String choixCompress = algoChoisi.nextLine();

        Utilitaire.validator(choixAlgo, choixCompress, convertedTxtFile, compressedFilePath, decompressedFilePath);
    }
}
