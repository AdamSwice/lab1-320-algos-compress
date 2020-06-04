import Bit.BitInputStream;
import Strategie.StrategieHuff;
import Strategie.StrategieLZW;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Utilitaire {
    public static void validator(String choixAlgo, String choixCompress, String fileIn, String fileOut) {
        try {
            if ("c".equalsIgnoreCase(choixCompress)) {
                if ("lzw".equalsIgnoreCase(choixAlgo)) {
                    File toCompress = new File(fileIn);
                    FileReader fileReader = new FileReader(toCompress);
                    StrategieLZW.compress(fileReader, fileOut);
                }
                else if ("huff".equalsIgnoreCase(choixAlgo)) {
                    long startTime = System.currentTimeMillis();
                    StrategieHuff.compress(convertTxtToString(fileIn),fileOut);
                    long endTime = System.currentTimeMillis();
                    System.out.println("duree: " + (endTime - startTime) + "ms");
                } else {
                    System.out.println("Erreur dans le choix d'algorithme de compression, vérifiez l'orthographe.");
                }
            } else if ("d".equalsIgnoreCase(choixCompress)) {
                if ("lzw".equalsIgnoreCase(choixAlgo)) {
                    //FileInputStream inputStream = new FileInputStream(fileIn);
                    BitInputStream inputStream = new BitInputStream(fileIn);
                    StrategieLZW.decompress(inputStream, fileOut);
                } else if ("huff".equalsIgnoreCase(choixAlgo)) {
                    long startTime = System.currentTimeMillis();
                    BitInputStream inputStream = new BitInputStream(fileIn);
                    StrategieHuff.decompress(inputStream, fileIn, fileOut);
                    long endTime = System.currentTimeMillis();
                    System.out.println("duree: " + (endTime - startTime) + "ms");
                }
            }
            else {
                System.out.println("Erreur dans le choix d'algorithme de compression, vérifiez l'orthographe.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String convertTxtToString(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines (Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach( string -> stringBuilder.append(string).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

//    private static void writeToFile(String data, String path) {
//        data = data.replace("[", "");
//        data = data.replace("]", "");
//
//        try {
//            File compressedFile = new File(path);
//            if (!compressedFile.exists()){
//                compressedFile.createNewFile();
//            }
//
//            FileWriter fileWriter = new FileWriter(compressedFile.getAbsoluteFile());
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            bufferedWriter.write(data);
//            bufferedWriter.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private static List<Integer> convertStringToList(String data) {
//        List<String> stringList = new ArrayList<String>(Arrays.asList(data.split(",")));
//        List<Integer> intList = new ArrayList<>();
//        for (String string : stringList){
//            string = string.replace(" ","").replace("\n","");
//            intList.add(Integer.valueOf(string));
//        }
//        return intList;
//    }
//    public static ArrayList<Object> read(){
//        ArrayList<Object> woi;
//        try
//        {
//            FileInputStream fis = new FileInputStream("hashmap.txt");
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            woi = (ArrayList<Object>) ois.readObject();
//            ois.close();
//            fis.close();
//            return woi;
//        }catch(IOException ioe)
//        {
//            ioe.printStackTrace();
//            return null;
//        }catch(ClassNotFoundException c)
//        {
//            System.out.println("Class not found");
//            c.printStackTrace();
//            return null;
//        }
//    }
//
//    public static void write(ArrayList<Object> woi){
//        try
//        {
//            FileOutputStream fos =
//                    new FileOutputStream("hashmap.txt");
//
//            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));
//            oos.writeObject(woi);
//            oos.close();
//            fos.close();
//
//        }catch(IOException ioe)
//        {
//            ioe.printStackTrace();
//        }
//    }
}
