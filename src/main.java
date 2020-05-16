import Strategie.StrategieLZW;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Classe main
 * Demande au user quel algo il veut utiliser, génère l'objet qui à l'algo d'implementé, puis éxecute l'algoryithme.
 * Date: 16 Mai 2020
 * Authors: Adam Zahiri, Sébastien Fauteux, Rabah Boubchir
 */

public class main {
    public static void main(String[] args) {
        String filePath = "C:/Users/Swicy/Desktop/lab1-320-algos-compress/src/exemple.txt";
        String convertedTxtFile = readtxt(filePath);
        Scanner algoChoisi = new Scanner(System.in);
        System.out.println("Choisissez un algorithme: lzw, huff ou opt.");
        String choixAlgo = algoChoisi.nextLine();
        System.out.println("Voulez-vous effectuer un compress ou un decompress?");
        String choixCompress = algoChoisi.nextLine();
        validator(choixAlgo, choixCompress, convertedTxtFile);
    }

    private static void validator(String choixAlgo, String choixCompress, String data) {
        if ("compress".equalsIgnoreCase(choixCompress)) {
            switch (choixAlgo) {
                case "lzw":
                    StrategieLZW lzw = new StrategieLZW();
                    System.out.println(lzw.compress(data));
                    break;
                case "huff":
                    //généré objet qui implemente huff
                    break;
                case "opt":
                    //généré objet qui implemente algo opt
                    break;
                default:
                    System.out.println("L'algo que vous avez choisi n'est pas valide");
                    break;
            }
        }
    }

    private static String readtxt(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines (Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach( string -> stringBuilder.append(string).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
