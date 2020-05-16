package Strategie;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StrategieLZW implements StrategieAlgo {
    @Override
    public List<Integer> compress(String data) {
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

    @Override
    public File decompress(File file) {
        return null;
    }
}
