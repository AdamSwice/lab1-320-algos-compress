package Strategie;

import Bit.BitInputStream;
import Bit.BitOutputStream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

public class StrategieOPT {
    public static void compress(FileReader fileReader, String fileOutput) throws Exception {
        HashMap<String, Integer> dictio = new HashMap<>();
        int i;

        for (i = 0; i < 256; i++){
            char t = (char) i;
            dictio.put(Character.toString(t), i);
        }
        BitOutputStream outputStream =  new BitOutputStream(fileOutput,false);
//        FileOutputStream outputStream = new FileOutputStream(new File(fileOutput));
        String prefix = "";
        int index;
        long startTime = System.currentTimeMillis();
        while ((index = fileReader.read()) != -1){
            String tempString = prefix + (char) index;
            if (dictio.containsKey(tempString)){
                prefix = tempString;
            } else {
                int code = dictio.get(prefix);
                String binaryCode = String.format("%9s", Integer.toBinaryString(code)).replaceAll(" ","0");
//                String binaryCode = Integer.toBinaryString(code);
                bitWriter(outputStream, binaryCode);
                if (dictio.size() == 511){
                    initializeDictioCompress(dictio, i);
                    i=256;
                }
                dictio.put(tempString, i);
                i++;
                prefix = "" + (char) index;
            }
        }

        if (dictio.containsKey(prefix)){
            int code = dictio.get(prefix);
            String binaryCode = String.format("%9s", Integer.toBinaryString(code)).replaceAll(" ","0");
//            String binaryCode = Integer.toBinaryString(code);
            bitWriter(outputStream, binaryCode);
        } else {
            dictio.put(prefix, i);
            int code = dictio.get(prefix);
//            String binaryCode = Integer.toBinaryString(code);
            String binaryCode = String.format("%9s", Integer.toBinaryString(code)).replaceAll(" ","0");
            bitWriter(outputStream, binaryCode);
        }
        fileReader.close();
//        outputStream.flush();
        outputStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    public static void decompress(BitInputStream inputStream, String fileOutput) throws Exception{
        HashMap<Integer, String> dictio = new HashMap<>();
        int i;
        String negativeBitChecker="";
        for (i=0;i<256; i++){
            char t = (char) i;
            dictio.put(i, Character.toString(t));
        }
        StringBuilder original = new StringBuilder("");
        int code = Integer.parseInt(readBit(inputStream),2);
        if (dictio.containsKey(code)){
            original.append(dictio.get(code));
        }
        int oldValue = code;
        long startTime = System.currentTimeMillis();
        while (!(negativeBitChecker = readBit(inputStream)).contains("-1") && !negativeBitChecker.isEmpty()){
            System.out.println(original.length());
            code = Integer.parseInt(negativeBitChecker,2);
            if (dictio.containsKey(code)){
                String tempString = dictio.get(code);
                original.append(tempString);
                String nullChecker = dictio.get(oldValue) == null? "" : dictio.get(oldValue);
                if (dictio.size() == 511){
                    initializeDictioDecompress(dictio, i);
                    i=256;
                }
                dictio.put(i, nullChecker + tempString.charAt(0));
                i++;
                oldValue = code;
            } else {
                String tempString = dictio.get(oldValue);
                if (i == 511){
                    initializeDictioDecompress(dictio, i);
                    i=256;
                }
                if ((tempString+tempString.charAt(0)).contains("null")){
                    System.out.println(code);
                }
                dictio.put(i, tempString + tempString.charAt(0));
                i++;
                original.append(tempString + tempString.charAt(0));
            }
        }
        inputStream.close();
        FileWriter fileWriter = new FileWriter(new File(fileOutput));
        fileWriter.write(original.toString());
        fileWriter.flush();
        fileWriter.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    private static void bitWriter(BitOutputStream writer, String bitString) throws Exception{
        bitString += "";
        char[] chars = bitString.toCharArray();
        int bit;
        for (int i = 0, n = chars.length; i < n; i++) {
            bit = Integer.parseInt(chars[i]+"");
            writer.writeBit(bit);
        }
    }

    private static void initializeDictioCompress(HashMap dictio, int i){
        dictio.clear();
        i=0;
        while (i<256){
            char t = (char) i;
            dictio.put(Character.toString(t), i);
            i++;
        }
    }
    private static void initializeDictioDecompress(HashMap dictio, int i){
        dictio.clear();
        i=0;
        while (i<256){
            char t = (char) i;
            dictio.put(i, Character.toString(t));
            i++;
        }
    }

    private static String readBit(BitInputStream inputStream) {
        String binaryCodeString = "";
        int bit;
        while (binaryCodeString.length() < 9 && (bit = inputStream.readBit() )!= -1) {
            binaryCodeString +=  Integer.toString(bit);
        }
        binaryCodeString += "";
        return new StringBuilder(binaryCodeString).toString();
    }

}
