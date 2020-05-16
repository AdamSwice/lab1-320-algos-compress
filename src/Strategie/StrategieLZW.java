package Strategie;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StrategieLZW {
    public static List<Integer> compress(String data) {
        int dictioSize = 256;
        String tempString = "";
        List<Integer> compressedResult = new ArrayList<Integer>();
        HashMap<String, Integer> dictionnary = new HashMap<String, Integer>();

        for (int i = 0; i < 256; i++) {
            dictionnary.put("" + (char) i, i);
        }

        for (char character : data.toCharArray()) {
            String concatenatedString = tempString + character;
            if (dictionnary.containsKey(concatenatedString)) {
                tempString = concatenatedString;
            } else {
                compressedResult.add(dictionnary.get(tempString));
                dictionnary.put(concatenatedString, dictioSize++);
                tempString = "" + character;
            }
        }

        if (!"".equalsIgnoreCase(tempString)){
            compressedResult.add(dictionnary.get(tempString));
        }
        return compressedResult;
    }

    public static String decompress(List<Integer> data) {
        int dictioSize = 256;
        String tempString = "" + (char) (int) data.remove(0);
        StringBuffer decompressedResult = new StringBuffer(tempString);

        HashMap<Integer, String> dictionnary = new HashMap<Integer, String>();

        for (int i = 0; i < 256; i++) {
            dictionnary.put(i, "" + (char) i);
        }

        for (int value : data) {
            String start = "";
            if (dictionnary.containsKey(value)){
                start = dictionnary.get(value);
            }
            else if (value == dictioSize) {
                start = tempString + tempString.charAt(0);
            } //TODO: throw exception si mal compressed maybe

            decompressedResult.append(start);

            dictionnary.put(dictioSize++, tempString + start.charAt(0));

            tempString = start;
        }
        return decompressedResult.toString();
    }
}
