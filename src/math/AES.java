package math;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*
 * Class Arguments Type: <-e/-d> <plaintext/ciphertext filename> <key filename>
 * TestCase 1: -e aes-plaintext11.txt aes-key11.txt
 * TestCase 1: -d aes-ciphertext10-cbc.txt aes-key10.txt
 * Follow similar format for all the tests
 * Will print out the ECB and CBC version for reach test case automatically
 */
public class AES {

    public static void main(String[] args) throws IOException {
        String type = args[0]; //first parameter encryption or decryption
        Scanner sc = new Scanner(new File(args[1]));
        Scanner scK = new Scanner(new File(args[2]));
        byte[] inputText = hexStringToArray(sc.next()); //second parameter either plaintext or ciphertext
        byte[] key = hexStringToArray(scK.next()); //third parameter is key
        int[] keyArr = new int[key.length];
        for (int i = 0; i < key.length; i++) {
            keyArr[i] = key[i];
        }
        sc.close();
        scK.close();
        int keySchedule = key.length;
        
        byte[] initV = hexStringToArray("000102030405060708090a0b0c0d0e0f"); //Initial vector for the CBC mode is created
        
        int rounds = 0;
        int keyNumber = 0;
        
        //Based on the number of bits in the key the number of rounds 
        //are determined and number of bytes
        switch (keySchedule) {
            // 128 bits
            case 16:
                rounds = 10;
                keyNumber = 4;
                break;
            // 192 bits
            case 24:
                rounds = 12;
                keyNumber = 6;
                break;
            // 256 bits 
            case 32:
                rounds = 14;
                keyNumber = 8;
                break;
        }

        int[][][] state = new int[2][4][4];
        int[] expandedKey = new int[(4 * (rounds + 1)) + keyNumber - 4]; //length of the expanded key
        
        KeyExpansion obj = new KeyExpansion();
        obj.keyExpansion(rounds, keyNumber, key, expandedKey); //Calls the key expansion
        
        Encryption encr = new Encryption();
        Decryption decr = new Decryption();
        //Checks whether encryption or decryption is called
        if(type.equals("-e")) {
            System.out.print("Plaintext file: ");
            System.out.println(args[1]);
            System.out.print("Key file: ");
            System.out.println(args[2]);
            
            //Encryption is done in ECB mode
            System.out.println("ECB mode:");
            encr.encryptECB(inputText, rounds, state, expandedKey);
            //Encryption is done in CBC mode
            System.out.println("CBC mode:");
            encr.encryptCBC(inputText, initV, rounds, state, expandedKey);
        }
        else if (type.equals("-d")) {
            System.out.print("Ciphertext file: ");
            System.out.println(args[1]);
            System.out.print("Key file: ");
            System.out.println(args[2]);
            //Decryption is done in ECB mode
            System.out.println("ECB mode:");
            decr.decryptECB(inputText, rounds, state, expandedKey);
            //Decryption is done in CBC mode
            System.out.println("CBC mode:");
            decr.decryptCBC(inputText, initV, rounds, state, expandedKey);
        }
    }
    
    //Changes the input hex string to a byte array used in AES
    public static byte[] hexStringToArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
