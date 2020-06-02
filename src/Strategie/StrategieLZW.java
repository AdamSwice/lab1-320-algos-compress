package Strategie;
import Bit.BitInputStream;
import Bit.BitOutputStream;

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

        BitOutputStream outputStream =  new BitOutputStream(fileOutput);
        //FileOutputStream outputStream = new FileOutputStream(new File(fileOutput));
        String prefix = "";
        int index;

        long startTime = System.currentTimeMillis();
        while ((index = fileReader.read()) != -1){
            String tempString = prefix + (char) index;
            if (dictio.containsKey(tempString)){
                prefix = tempString;
            } else {
                int code = dictio.get(prefix);
                String binaryCode = Integer.toBinaryString(code);
                binaryCode = new StringBuilder(binaryCode).reverse().toString();
                bitWriter(outputStream, binaryCode);
                if (dictio.size() == 511){
                    initializeDictio(dictio, i);
                    i=256;
                }
                dictio.put(tempString, i);
                i++;
                prefix = "" + (char) index;
            }
        }

        if (dictio.containsKey(prefix)){
            int code = dictio.get(prefix);
            String binaryCode = Integer.toBinaryString(code);
            binaryCode = new StringBuilder(binaryCode).reverse().toString();
            bitWriter(outputStream, binaryCode);
        } else {
            dictio.put(prefix, i);
            int code = dictio.get(prefix);
            String binaryCode = Integer.toBinaryString(code);
            binaryCode = new StringBuilder(binaryCode).reverse().toString();
            bitWriter(outputStream, binaryCode);
        }
        fileReader.close();
        //outputStream.flush();
        outputStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    public static void decompress(BitInputStream inputStream, String fileOutput) throws Exception{
        HashMap<Integer, String> dictio = new HashMap<>();
        int i;

        for (i=0;i<256; i++){
            char t = (char) i;
            dictio.put(i, Character.toString(t));
        }

        String original = "";

        int code = inputStream.readBit();
        if (dictio.containsKey(code)){
            original += dictio.get(code);
        }
        int oldValue = code;

        long startTime = System.currentTimeMillis();
        while ((code = inputStream.readBit()) != -1){
            if (dictio.containsKey(code)){
                String tempString = dictio.get(code);
                original = original + tempString;
                if (dictio.size() == 511){
                    initializeDictio(dictio, i);
                    i=256;
                }
                dictio.put(i, dictio.get(oldValue) + tempString.charAt(0));
                i++;
                oldValue = code;
            } else {
                String tempString = dictio.get(oldValue);
                if (dictio.size() == 511){
                    initializeDictio(dictio, i);
                    i=256;
                }
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

    private static void bitWriter(BitOutputStream writer, String bitString){
        if (bitString.length() < 9){
            for (int i=bitString.length();i<9;i++) {
                bitString += "0";
            }
        }
        for (char ch : bitString.toCharArray()) {
            writer.writeBit(Integer.parseInt(""+ch));
        }
    }

    private static void initializeDictio(HashMap dictio, int i){
        dictio.clear();
        i=0;
        while (i<256){
            char t = (char) i;
            dictio.put(Character.toString(t), i);
            i++;
        }
    }

    private static void readBit(int code, BitInputStream inputStream) {
        String binaryCodeString;
        while (binaryCodeString.length() < 9) {
            
        }
    }

}
