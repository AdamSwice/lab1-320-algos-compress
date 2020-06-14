package Strategie;

import Bit.BitInputStream;
import Bit.BitOutputStream;

import java.io.*;
import java.nio.ByteBuffer;
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
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileOutput));
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
                bitWriter(byteToInt);
                String binaryCode = String.format("%16s", Integer.toBinaryString(code)).replaceAll(" ", "0");
                String binaryCodeOne = binaryCode.substring(0, (binaryCode).length()/2);
                String binaryCodeTwo = binaryCode.substring(binaryCode.length()/2);

//                bitWriter(bufferedOutputStream, binaryCodeOne, binaryCodeTwo);
                //bitWriter(outputStream, binaryCode);
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
            String binaryCodeOne = binaryCode.substring(0, (binaryCode).length()/2);
            String binaryCodeTwo = binaryCode.substring(binaryCode.length()/2);
            bitWriter(bufferedOutputStream, binaryCodeOne, binaryCodeTwo);
        } else {
            dictio.put(prefix, i);
            int code = dictio.get(prefix);
            String binaryCode = String.format("%16s", Integer.toBinaryString(code)).replaceAll(" ", "0");
            String binaryCodeOne = binaryCode.substring(0, (binaryCode).length()/2);
            String binaryCodeTwo = binaryCode.substring(binaryCode.length()/2);
            bitWriter(bufferedOutputStream, binaryCodeOne, binaryCodeTwo);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        fileInputStream.close();
        bufferedInputStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    public static void decompress(BitInputStream inputStream, String fileOutput) throws Exception {
        HashMap<Integer, String> dictio = new HashMap<>();
        int i;
        String negativeBitChecker = "";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutput));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        FileInputStream fileInputStream = new FileInputStream(fileOutput);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        byte[] bytes = bufferedInputStream.readAllBytes();
        for (i = 0; i < 256; i++) {
            char t = (char) i;
            dictio.put(i, Character.toString(t));
        }
        // StringBuilder original = new StringBuilder("");
        int code = Integer.parseInt(readBit(inputStream), 2);
        if (dictio.containsKey(code)) {
            // original.append(dictio.get(code));
            char[] chars = dictio.get(code).toCharArray();
            for (char aChar : chars) bufferedOutputStream.write(aChar);
        }
        int oldValue = code;
        long startTime = System.currentTimeMillis();
        while (i < bytes.length) {
            byte[] buffer = new byte[2];
            for (int j = 0; j < buffer.length; j++){
                buffer[j] = bytes[i++];
            }
            ByteBuffer wrapper = ByteBuffer.wrap(buffer);
            code = wrapper.getInt();
            //code = Integer.parseInt(negativeBitChecker, 2);
            if (dictio.containsKey(code)) {
                String combinaison = dictio.get(code);
                for (int j = 0; j < combinaison.length(); j++) {
                    bufferedOutputStream.write(combinaison.charAt(j));
                }
                //bufferedOutputStream.write(combinaison.getBytes());
                //original.append(combinaison);
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
               /* if ((combinaison + combinaison.charAt(0)).contains("null")) {
                    // System.out.println(code);
                }*/
                dictio.put(i, combinaison + combinaison.charAt(0));
                i++;
                String combinaisons = (combinaison + combinaison.charAt(0));
                for (int j = 0; j < combinaisons.length(); j++) {
                    bufferedOutputStream.write(combinaisons.charAt(j));
                }
                //bufferedOutputStream.write((combinaison + combinaison.charAt(0)).getBytes());

                // original.append(combinaison + combinaison.charAt(0));
            }
        }
        inputStream.close();
        // String originalToString = original.toString();
        // byte[] bytes = originalToString.getBytes();
        // bufferedOutputStream.write(bytes);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        long endTime = System.currentTimeMillis();
        System.out.println("duree: " + (endTime - startTime) + "ms");
    }

    private static byte[] bitWriter(int value){
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        return byteBuffer.putInt(value).array();

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
        return new StringBuilder(binaryCodeString).toString();
    }

}
