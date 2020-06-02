package Strategie;
import java.io.*;
import java.util.HashMap;

public class StrategieLZW {

    public static void compress(FileReader fileReader, String fileOutput) throws Exception {
        HashMap<String, Integer> dictio = new HashMap<>();
        int i;
        for (i = 0; i < 256; i++){
            char t = (char) i;
            dictio.put(Character.toString(t), i);
        }

        FileOutputStream outputStream = new FileOutputStream(new File(fileOutput));
        String prefix = "";
        int index;

        long startTime = System.currentTimeMillis();
        while ((index = fileReader.read()) != -1){
            String tempString = prefix + (char) index;

            if (dictio.containsKey(tempString)){
                prefix = tempString;
            } else {
                int code = dictio.get(prefix);
                outputStream.write(code);
                dictio.put(tempString, i);
                i++;
                prefix = "" + (char) index;
            }
        }

        if (dictio.containsKey(prefix)){
            int code = dictio.get(prefix);
            outputStream.write(code);
        } else {
            dictio.put(prefix, i);
            int code = dictio.get(prefix);
            outputStream.write(code);
        }
        fileReader.close();
        outputStream.flush();
        outputStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    public static void decompress(FileInputStream inputStream, String fileOutput) throws Exception{
        HashMap<Integer, String> dictio = new HashMap<>();
        int i;

        for (i=0;i<256; i++){
            char t = (char) i;
            dictio.put(i, Character.toString(t));
        }

        String original = "";

        int code = inputStream.read();
        if (dictio.containsKey(code)){
            original += dictio.get(code);
        }
        int oldValue = code;

        long startTime = System.currentTimeMillis();
        while ((code = inputStream.read()) != -1){
            if (dictio.containsKey(code)){
                String tempString = dictio.get(code);
                original = original + tempString;
                dictio.put(i, dictio.get(oldValue) + tempString.charAt(0));
                i++;
                oldValue = code;
            } else {
                String tempString = dictio.get(oldValue);
                dictio.put(i, tempString + tempString.charAt(0));
                i++;
                original = original + tempString + tempString.charAt(0);
            }
        }
        inputStream.close();
        FileWriter fileWriter = new FileWriter(new File(fileOutput));
        fileWriter.write(original);
        fileWriter.flush();
        fileWriter.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

}
