import java.util.Scanner;

/**
 * Classe main
 * Demande au user quel algo il veut utiliser, génère l'objet qui à l'algo d'implementé, puis éxecute l'algoryithme.
 * Date: 16 Mai 2020
 * Authors: Adam Zahiri, Sébastien Fauteux, Rabah Boubchir
 */

public class main {
    public static void main(String[] args) {
        Scanner algoChoisi = new Scanner(System.in);
        System.out.println("Choisissez un algorithme: lzw, huff ou opt.");
        String choix = algoChoisi.nextLine();
        validator(choix);
    }

    private static void validator(String choix) {
        switch (choix) {
            case "lzw":
                //généré objet qui implemente lzw
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
