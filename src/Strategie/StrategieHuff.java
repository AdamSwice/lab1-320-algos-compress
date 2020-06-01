package Strategie;

import java.io.*;
import java.util.*;

public class StrategieHuff {
    private static Map<Character, String> encoding = new HashMap<>();
    private static Map<Character, Integer> freq = new HashMap<Character, Integer>();
    static HuffmanNode root;
    static HuffmanNode DecompressionTree;

    static class HuffmanNode implements Comparable<HuffmanNode> {
        int frequency;
        char data;
        HuffmanNode left= null;
        HuffmanNode right= null;

        //primordial to be able to use a priority queue
        public int compareTo(HuffmanNode node) {
            return frequency - node.frequency;
        }
        public boolean isLeaf(){
            if(this.left == null  && this.right == null)
                return true;
            else
                return false;
        }
    }


    public static ArrayList<Object> compress(String data) {
        ArrayList<Object> compressedData =new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            if (!freq.containsKey(data.charAt(i))) {
                freq.put(data.charAt(i), 0);
            }
            freq.put(data.charAt(i), freq.get(data.charAt(i)) + 1);
        }

        root = buildTree(freq);
        Encode(root, new StringBuilder());
        System.out.println(encoding);
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            s.append(encoding.get(c));
        }
        compressedData.add(freq);
        compressedData.add(s.toString());
        return compressedData;
    }

    public static String decompress (ArrayList<Object> data){
        StringBuilder stringBuilder = new StringBuilder();

        root = buildTree((Map<Character, Integer>)data.get(0));
        Encode(root, new StringBuilder());
        HuffmanNode temp=root;

        String s = (String)data.get(1);


        for (int i = 0; i < s.length(); i++) {
            int j = Integer.parseInt(String.valueOf(s.charAt(i)));

            if (j == 0) {
                temp = temp.left;
                if (temp.isLeaf()) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
            if (j == 1) {
                temp = temp.right;
                if (temp.isLeaf()) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
        }


    return  stringBuilder.toString();
    }

    private static HuffmanNode buildTree (Map < Character, Integer > freq){

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
    private static void Encode(HuffmanNode node, StringBuilder prefix){

        if (node != null) {
            if (node.isLeaf()) {
                encoding.put(node.data, prefix.toString());

            } else {
                prefix.append('0');
                Encode(node.left, prefix);
                prefix.deleteCharAt(prefix.length() - 1);

                prefix.append('1');
                Encode(node.right, prefix);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

    }

}
