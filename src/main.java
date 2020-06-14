/**
 * Classe main
 * Demande au user quel algo il veut utiliser, génère l'objet qui à l'algo d'implementé, puis éxecute l'algorithme.
 * Date: 16 Mai 2020
 * Authors: Adam Zahiri, Sébastien Fauteux, Rabah Boubchir
 */

public class main {
    public static void main(String[] args) {

        String choixAlgo = args[0].replace("-", "");
        String choixCompress = args[1].replace("-", "");
        String fileIn = args[2];
        String fileOut = args[3];

        Utilitaire.validator(choixAlgo, choixCompress, fileIn, fileOut);

    }
}
