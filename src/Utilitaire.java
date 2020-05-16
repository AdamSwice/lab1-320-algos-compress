import Strategie.StrategieLZW;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Utilitaire {
    public static void validator(String choixAlgo, String choixCompress, String data, String newFilePath) {
        if ("compress".equalsIgnoreCase(choixCompress)) {
            switch (choixAlgo) {
                case "lzw":
                    StrategieLZW lzw = new StrategieLZW();
                    long startTime = System.currentTimeMillis();
                    writeToFile(lzw.compress(data).toString(), newFilePath);
                    long endTime = System.currentTimeMillis();
                    System.out.println("duree: " + (endTime - startTime) + "ms");
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

    public static String readtxt(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines (Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach( string -> stringBuilder.append(string).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static void writeToFile(String data, String path) {
        try {
            File compressedFile = new File(path);
            if (!compressedFile.exists()){
                compressedFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(compressedFile.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
