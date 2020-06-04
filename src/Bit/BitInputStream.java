package Bit;

/*
 * Classe donnée par le cours LOG320 dans le cadre du laboratoire 1, trouvable sur moodle: https://courses.cs.washington.edu/courses/cse143/12sp/homework/ass8/BitInputStream.java
 * */

import java.io.*;

public class BitInputStream {
    private FileInputStream input;
    public int digits;     // next set of digits (buffer)
    private int numDigits;  // how many digits from buffer have been used

    private static final int BYTE_SIZE = 8;  // digits per byte

    // pre : given file name is legal
    // post: creates a BitInputStream reading input from the file
    public BitInputStream(String file) {
        try {
            input = new FileInputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        nextByte();
    }

    // post: reads next bit from input (-1 if at end of file)
    public int readBit() {
        // if at eof, return -1
        if (digits == -1)
            return -1;
        int result = digits % 2;
        digits /= 2;
        numDigits++;
        if (numDigits == BYTE_SIZE)
            nextByte();
        return result;
    }

    // post: refreshes the internal buffer with the next BYTE_SIZE bits
    private void nextByte() {
        try {
            digits = input.read();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        numDigits = 0;
    }

    // post: input is closed
    public void close() {
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    // included to ensure that the stream is closed
    protected void finalize() {
        close();
    }

    public int getDigits() {
        return digits;
    }
}