package Strategie;

import Bit.BitInputStream;
import Bit.BitOutputStream;

import java.io.*;
import java.util.HashMap;

public class StrategieLZW {
    final static String binaryCodeLength = "%20s";
    public static final int BYTE_AMOUNT_INT_20 = 1048575;

    public static void compress(String fileOutput, File toCompress) throws Exception {
        HashMap<String, Integer> dictio = new HashMap<>();
        int i;

        for (i = 0; i < 256; i++) {
            char t = (char) i;
            dictio.put(Character.toString(t), i);
        }

        BitOutputStream outputStream = new BitOutputStream(fileOutput, false);
        FileInputStream fileInputStream = new FileInputStream(toCompress);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        byte[] bytes = bufferedInputStream.readAllBytes();

        String prefix = "";
        int index = 0;
        while (index < bytes.length) {

            int byteToInt = Byte.toUnsignedInt(bytes[index++]);
            char c = (char) byteToInt;

            String combinaison = prefix + c;
            if (dictio.containsKey(combinaison)) {
                prefix = combinaison;
            } else {
                int code = dictio.get(prefix);
                String binaryCode = String.format(binaryCodeLength, Integer.toBinaryString(code)).replaceAll(" ", "0");
                bitWriter(outputStream, binaryCode);

                if (dictio.size() == BYTE_AMOUNT_INT_20) {
                    initializeDictioCompress(dictio, i);
                    i = 256;
                }
                dictio.put(combinaison, i);

                i++;
                prefix = c + "";
            }

        }

        if (dictio.containsKey(prefix)) {
            int code = dictio.get(prefix);
            String binaryCode = String.format(binaryCodeLength, Integer.toBinaryString(code)).replaceAll(" ", "0");
            bitWriter(outputStream, binaryCode);

        } else {
            dictio.put(prefix, i);
            int code = dictio.get(prefix);
            String binaryCode = String.format(binaryCodeLength, Integer.toBinaryString(code)).replaceAll(" ", "0");
            bitWriter(outputStream, binaryCode);

        }

        outputStream.close();
        fileInputStream.close();
        bufferedInputStream.close();
    }

    public static void decompress(BitInputStream inputStream, String fileOutput) throws Exception {
        HashMap<Integer, String> dictio = new HashMap<>();
        int i;
        String negativeBitChecker = "";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutput));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        for (i = 0; i < 256; i++) {
            char t = (char) i;
            dictio.put(i, Character.toString(t));
        }

        int code = Integer.parseInt(readBit(inputStream), 2);

        if (dictio.containsKey(code)) {
            char[] chars = dictio.get(code).toCharArray();
            for (char aChar : chars) bufferedOutputStream.write(aChar);
        }
        int oldValue = code;
        while (!(negativeBitChecker = readBit(inputStream)).contains("-1") && !negativeBitChecker.isEmpty()) {
            code = Integer.parseInt(negativeBitChecker, 2);

            if (dictio.containsKey(code)) {
                String combinaison = dictio.get(code);
                for (int j = 0; j < combinaison.length(); j++) {
                    bufferedOutputStream.write(combinaison.charAt(j));
                }
                String nullChecker = dictio.get(oldValue) == null ? "" : dictio.get(oldValue);
                if (dictio.size() == BYTE_AMOUNT_INT_20) {
                    initializeDictioDecompress(dictio, i);
                    i = 256;
                }
                dictio.put(i, nullChecker + combinaison.charAt(0));
                i++;
                oldValue = code;
            } else {
                String combinaison = dictio.get(oldValue);
                if (i == BYTE_AMOUNT_INT_20) {
                    initializeDictioDecompress(dictio, i);
                    i = 256;
                }
                dictio.put(i, combinaison + combinaison.charAt(0));
                oldValue = code;
                i++;
                String combinaisons = (combinaison + combinaison.charAt(0));
                for (int j = 0; j < combinaisons.length(); j++) {
                    bufferedOutputStream.write(combinaisons.charAt(j));
                }
            }
        }

        inputStream.close();
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
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

    private static void initializeDictioCompress(HashMap<String, Integer> dictio, int i) {
        dictio.clear();
        i = 0;
        while (i < 256) {
            char t = (char) i;
            dictio.put(Character.toString(t), i);
            i++;
        }
    }

    private static void initializeDictioDecompress(HashMap<Integer, String> dictio, int i) {
        dictio.clear();
        i = 0;
        while (i < 256) {
            char t = (char) i;
            dictio.put(i, Character.toString(t));
            i++;
        }
    }

    private static String readBit(BitInputStream inputStream) {
        StringBuilder binaryCodeString = new StringBuilder();
        int bit;
        while (binaryCodeString.length() < 20 && (bit = inputStream.readBit()) != -1) {
            binaryCodeString.append(bit);
        }
        return binaryCodeString.toString();
    }

}
