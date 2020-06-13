package Strategie;

import Bit.BitInputStream;
import Bit.BitOutputStream;

import java.io.*;
import java.util.HashMap;

public class StrategieLZW {

    public static void compress(String fileOutput, File toCompress) throws Exception {
        HashMap<String, Integer> dictio = new HashMap<>();
        int i;

        for (i = 0; i < 256; i++) {
            char t = (char) i;
            dictio.put(Character.toString(t), i);
        }
        //Buffered
        BitOutputStream outputStream = new BitOutputStream(fileOutput, false);
        FileInputStream fileInputStream = new FileInputStream(toCompress);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        byte[] bytes = bufferedInputStream.readAllBytes();
        String prefix = "";
        int index = 0;
        long startTime = System.currentTimeMillis();
        while (index < bytes.length) {
            int byteToInt = Byte.toUnsignedInt(bytes[index++]);
            char c = (char) byteToInt;
            String combinaison = prefix + c;
            if (dictio.containsKey(combinaison)) {
                prefix = combinaison;
            } else {
                int code = dictio.get(prefix);
                String binaryCode = String.format("%16s", Integer.toBinaryString(code)).replaceAll(" ", "0");
                bitWriter(outputStream, binaryCode);
                if (dictio.size() == 65535) {
                    initializeDictioCompress(dictio, i);
                    i = 256;
                }
                dictio.put(combinaison, i);
                i++;
                prefix = "" + c;
            }
        }

        if (dictio.containsKey(prefix)) {
            int code = dictio.get(prefix);
            String binaryCode = String.format("%16s", Integer.toBinaryString(code)).replaceAll(" ", "0");
            bitWriter(outputStream, binaryCode);
        } else {
            dictio.put(prefix, i);
            int code = dictio.get(prefix);
            String binaryCode = String.format("%16s", Integer.toBinaryString(code)).replaceAll(" ", "0");
            bitWriter(outputStream, binaryCode);
        }
        outputStream.close();
        fileInputStream.close();
        bufferedInputStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    public static void decompress(BitInputStream inputStream, String fileOutput) throws Exception {
        HashMap<Integer, String> dictio = new HashMap<>();
        int i;
        String negativeBitChecker = "";
        for (i = 0; i < 256; i++) {
            char t = (char) i;
            dictio.put(i, Character.toString(t));
        }
        StringBuilder original = new StringBuilder("");
        int code = Integer.parseInt(readBit(inputStream), 2);
        if (dictio.containsKey(code)) {
            original.append(dictio.get(code));
        }
        int oldValue = code;
        long startTime = System.currentTimeMillis();
        while (!(negativeBitChecker = readBit(inputStream)).contains("-1") && !negativeBitChecker.isEmpty()) {
            code = Integer.parseInt(negativeBitChecker, 2);
            if (dictio.containsKey(code)) {
                String combinaison = dictio.get(code);
                original.append(combinaison);
                String nullChecker = dictio.get(oldValue) == null ? "" : dictio.get(oldValue);
                if (dictio.size() == 65535) {
                    initializeDictioDecompress(dictio, i);
                    i = 256;
                }
                dictio.put(i, nullChecker + combinaison.charAt(0));
                i++;
                oldValue = code;
            } else {
                String combinaison = dictio.get(oldValue);
                if (i == 65535) {
                    initializeDictioDecompress(dictio, i);
                    i = 256;
                }
                if ((combinaison + combinaison.charAt(0)).contains("null")) {
                    // System.out.println(code);
                }
                dictio.put(i, combinaison + combinaison.charAt(0));
                i++;
                original.append(combinaison + combinaison.charAt(0));
            }
        }
        inputStream.close();
        FileWriter fileWriter = new FileWriter(new File(fileOutput));
        fileWriter.write(original.toString());
        fileWriter.flush();
        fileWriter.close();
        /*FileOutputStream fileOutputStream = new FileOutputStream(fileToDecompress);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);*/
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    private static void bitWriter(BitOutputStream writer, String bitString) throws Exception {
        bitString += "";
        char[] chars = bitString.toCharArray();
        int bit;
        for (int i = 0, n = chars.length; i < n; i++) {
            bit = Integer.parseInt(chars[i] + "");
            writer.writeBit(bit);
        }
    }

    private static void initializeDictioCompress(HashMap dictio, int i) {
        dictio.clear();
        i = 0;
        while (i < 256) {
            char t = (char) i;
            dictio.put(Character.toString(t), i);
            i++;
        }
    }

    private static void initializeDictioDecompress(HashMap dictio, int i) {
        dictio.clear();
        i = 0;
        while (i < 256) {
            char t = (char) i;
            dictio.put(i, Character.toString(t));
            i++;
        }
    }

    private static String readBit(BitInputStream inputStream) {
        String binaryCodeString = "";
        int bit;
        while (binaryCodeString.length() < 16 && (bit = inputStream.readBit()) != -1) {
            binaryCodeString += Integer.toString(bit);
        }
        binaryCodeString += "";
        return new StringBuilder(binaryCodeString).toString();
    }

}
