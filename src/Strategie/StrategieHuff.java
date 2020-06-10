package Strategie;


import Bit.BitInputStream;
import Bit.BitOutputStream;

import java.io.*;
import java.util.*;

public class StrategieHuff {
    private static Map<Character, String> encoding = new HashMap<>();
    private static Map<Character, Integer> freq = new HashMap<Character, Integer>();
    private static HuffmanNode root;
    private static int facteurErreur;

    static class HuffmanNode implements Comparable<HuffmanNode>, Serializable {
        int frequency;
        char data;
        HuffmanNode left = null;
        HuffmanNode right = null;

        //primordial to be able to use a priority queue
        public int compareTo(HuffmanNode node) {
            return frequency - node.frequency;
        }

        public boolean isLeaf() {
            if (this.left == null && this.right == null)
                return true;
            else
                return false;
        }
    }


    public static void compress(String data, String fileOutput) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < data.length(); i++) {
            if (!freq.containsKey(data.charAt(i))) {
                freq.put(data.charAt(i), 0);
            }
            freq.put(data.charAt(i), freq.get(data.charAt(i)) + 1);
        }

        root = buildTree(freq);
        encode(root, new StringBuilder());

        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            s.append(encoding.get(c));
        }

        try {
            //Calculer le facteur d'erreur du bitWriter
            facteurErreur = s.length() % 8;

            //Saving JavaObject usefull for decompression of the file
            ArrayList<Object> a = new ArrayList<>();
            OutputStream os = new FileOutputStream(new File(fileOutput));
            ObjectOutputStream o = new ObjectOutputStream(new BufferedOutputStream(os));
            a.add(facteurErreur);
            a.add(root);
            o.writeObject(a);
            o.close();

            //Saving the CompressedData
            BitOutputStream outputStream = new BitOutputStream(fileOutput, true);
            bitWriter(outputStream, s.toString());
            outputStream.close();
            os.close();

        } catch (Exception e) {
        }

    }

    public static void decompress(BitInputStream inputStream, String fileInput, String fileOutput) {
        StringBuilder uncompressedData = new StringBuilder();

        //Gets the huffman tree and the ErrorFactor
        readObject(fileInput);

        //Reads the compressedData
        StringBuilder compressedData = readBit(inputStream);

        //Decompression of the code by iterating throw the tree
        HuffmanNode temp = root;
        for (int i = 0; i < compressedData.length(); i++) {
            int j = Integer.parseInt(String.valueOf(compressedData.charAt(i)));

            if (j == 0) {
                temp = temp.left;
                if (temp.isLeaf()) {
                    uncompressedData.append(temp.data);
                    temp = root;
                }
            }
            if (j == 1) {
                temp = temp.right;
                if (temp.isLeaf()) {
                    uncompressedData.append(temp.data);
                    temp = root;
                }
            }
        }
        //The uncompressed data is written to the disk
        writeToFile(uncompressedData.toString(), fileOutput);
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

    private static HuffmanNode buildTree(Map<Character, Integer> freq) {

        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        for (Character charc : freq.keySet()) {

            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.data = charc;
            huffmanNode.frequency = freq.get(charc);
            priorityQueue.add(huffmanNode);

        }

        while (priorityQueue.size() > 1) {

            HuffmanNode e1 = priorityQueue.poll();
            HuffmanNode e2 = priorityQueue.poll();


            HuffmanNode combination = new HuffmanNode();
            combination.frequency = e1.frequency + e2.frequency;
            combination.left = e1;
            combination.right = e2;

            priorityQueue.add(combination);
        }
        return priorityQueue.peek();
    }

    private static void encode(HuffmanNode node, StringBuilder prefix) {

        if (node != null) {
            if (node.isLeaf()) {
                encoding.put(node.data, prefix.toString());

            } else {
                prefix.append('0');
                encode(node.left, prefix);
                prefix.deleteCharAt(prefix.length() - 1);

                prefix.append('1');
                encode(node.right, prefix);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

    }

    private static StringBuilder readBit(BitInputStream inputStream) {
        StringBuilder s = new StringBuilder();
        int bit;
        ArrayList<Object> a = new ArrayList<>();
        a.add(facteurErreur);
        a.add(root);
        int bitsToSkip = sizeof(a);
        double currentBits = 0;
        while ((bit = inputStream.readBit()) != -1) {
            if (!(currentBits < bitsToSkip)) {
                s.append(bit);
            } else
                currentBits++;
        }
        inputStream.close();
        s.delete(s.length() - facteurErreur, s.length());
        return s;
    }

    public static void readObject(String fileInput) {

        try {
            FileInputStream fis = new FileInputStream(fileInput);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ArrayList<Object> a = (ArrayList<Object>) ois.readObject();
            facteurErreur = (int) a.get(0);
            root = (HuffmanNode) a.get(1);

            ois.close();
            fis.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();

        }
    }

    //Pure copier coller de stackOverflow Jvais le changer un peu (Permet de calculer la taille d'un object dans un file).
    public static int sizeof(Object obj) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();

            return byteOutputStream.toByteArray().length * 8;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void writeToFile(String data, String path) {

        try {
            File compressedFile = new File(path);
            if (!compressedFile.exists()) {
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
}
