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

        createFrequencyTable(data);

        root = buildTree(freq);
        createEncodingMap(root, new StringBuilder());

        StringBuilder compressedData=encode(data);


        try {
            //Calculer le facteur d'erreur du bitWriter
            facteurErreur = compressedData.length() % 8;

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
            bitWriter(outputStream, compressedData.toString());
            outputStream.close();
            os.close();

        } catch (Exception e) {
        }

    }
    public static StringBuilder encode(String data){
        StringBuilder compressedData = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            compressedData.append(encoding.get(c));
        }
        return compressedData;
    }
    public static void createFrequencyTable(String data){
        for (int i = 0; i < data.length(); i++) {
            if (!freq.containsKey(data.charAt(i))) {
                freq.put(data.charAt(i), 0);
            }
            freq.put(data.charAt(i), freq.get(data.charAt(i)) + 1);
        }
    }

    public static void decompress(BitInputStream inputStream, String fileInput, String fileOutput) {
        ArrayList<Byte> uncompressedData = new ArrayList<>();

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
                    byte b= (byte)temp.data;
                    uncompressedData.add(b);
                    temp = root;
                }
            }
            if (j == 1) {
                temp = temp.right;
                if (temp.isLeaf()) {
                    byte b= (byte)temp.data;
                    uncompressedData.add(b);
                    temp = root;
                }
            }
        }

        //The uncompressed data is written to the disk
        writeToFile(uncompressedData, fileOutput);
    }

    private static void bitWriter(BitOutputStream writer, String bitString) {
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

            HuffmanNode node1 = priorityQueue.poll();
            HuffmanNode node2 = priorityQueue.poll();


            HuffmanNode combination = new HuffmanNode();
            combination.frequency = node1.frequency + node2.frequency;
            combination.left = node1;
            combination.right = node2;

            priorityQueue.add(combination);
        }
        return priorityQueue.peek();
    }

    private static void createEncodingMap(HuffmanNode node, StringBuilder prefix) {

        if (node != null) {
            if (node.isLeaf()) {
                encoding.put(node.data, prefix.toString());

            } else {
                prefix.append('0');
                createEncodingMap(node.left, prefix);
                prefix.deleteCharAt(prefix.length() - 1);

                prefix.append('1');
                createEncodingMap(node.right, prefix);
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

    //L'idee proviens de stackOverFlow
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

    private static void writeToFile(ArrayList<Byte> data, String path) {

        try {
            byte[] a =new byte[data.size()];
            int i=0;
            for(byte b : data){
                a[i]=b;
                i++;
            }
            FileOutputStream fos = new FileOutputStream(path);
            BufferedOutputStream buff = new BufferedOutputStream(fos);
            buff.write(a);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
