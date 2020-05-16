import Strategie.StrategieLZW;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Utilitaire {
    public static void validator(String choixAlgo, String choixCompress, String data, String compressedFilePath, String decompressedFilePath) {
        if ("compress".equalsIgnoreCase(choixCompress)) {
            if ("lzw".equalsIgnoreCase(choixAlgo)) {
                long startTime = System.currentTimeMillis();
                Utilitaire.writeToFile(StrategieLZW.compress(data).toString(), compressedFilePath);
                long endTime = System.currentTimeMillis();
                System.out.println("duree: " + (endTime - startTime) + "ms");
            }
        } else if ("decompress".equalsIgnoreCase(choixCompress)) {
            if ("lzw".equalsIgnoreCase(choixAlgo)) {
                long startTime = System.currentTimeMillis();
                String compressedData = readtxt(compressedFilePath);
                Utilitaire.writeToFile(StrategieLZW.decompress(convertStringToList(compressedData)), decompressedFilePath);
                long endTime = System.currentTimeMillis();
                System.out.println("duree: " + (endTime - startTime) + "ms");
            }
        } else {
            System.out.println();
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
        data = data.replace("[", "");
        data = data.replace("]", "");

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
    private static List<Integer> convertStringToList(String data) {
        List<String> stringList = new ArrayList<String>(Arrays.asList(data.split(",")));
        List<Integer> intList = new ArrayList<>();
        for (String string : stringList){
            intList.add(Integer.valueOf(string.replace(" ","").replace("\n","")));
        }
        return intList;
    }
}
