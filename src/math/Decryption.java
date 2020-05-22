package math;

import java.util.Arrays;

public class Decryption {
    
    //Main decryption mechanism of AES
    private byte[] decrypt(byte[] text, int round, int[][][] state, int[] key) {
        int actual = 0;
        byte[] out = new byte[text.length];

        //Converts byte array to int array
        for (int i = 0; i < 4; i++) { // columns
            for (int j = 0; j < 4; j++) { // rows
                state[0][j][i] = text[i * 4 + j] & 0xff;
            }
        }

        for (int i = 0; i < state[0].length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                state[1][i][j] = state[0][i][j];
            }
        }
        actual = round;
        MatrixOpps opps = new MatrixOpps();
        KeyExpansion exp = new KeyExpansion();
        
        //Initial key round 
        opps.addRoundKey(state[1], actual, key);

        //Repeats the main process of the AES based on the 
        //number of rounded needed for a particular key
        for (actual = round - 1; actual > 0; actual--) {
            opps.invShiftRows(state[1]); //Inverse of shift rows is executed to decrypt
            exp.invSubBytes(state[1]); //Inverse of sBox is executed to decrypt
            opps.addRoundKey(state[1], actual, key); //Add round key
            opps.mixCol(true);
            opps.mix(state[1]); //Inverse of mix columns is executed to decrypt
        }
        opps.invShiftRows(state[1]); //Inverse of shift rows is executed to decrypt
        exp.invSubBytes(state[1]); //Inverse of sBox is executed to decrypt
        opps.addRoundKey(state[1], actual, key); //Add round key
        
        //Converts the int array to a byte array
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                out[i * 4 + j] = (byte) (state[1][j][i] & 0xff);
            }
        }
        return out;

    }
    
    //Method calls the decrypt function and prints the result in hex string format
    public void decryptECB(byte[] text, int round, int[][][] state, int[] key) {
        for (int i = 0; i < text.length; i+=16) {
                byte[] result = decrypt(Arrays.copyOfRange(text, i, i + 16), round, state, key);
                StringBuilder sb = new StringBuilder();
                for (byte b : result) {
                    sb.append(String.format("%02x", b));
                }
                System.out.println(sb.toString());
        }
    }
    
    //Methods calls the decrypt function and prints the result in hex string format
    //Uses the initial vector to determine and track the previous block in CBC mode 
    public void decryptCBC(byte[] text, byte[] iv, int round, int[][][] state, int[] key) {
        //byte[] block = null;
        byte[] cipher = Arrays.copyOfRange(text, 0, 16);
        byte[] block = decrypt(cipher, round, state, key);
        StringBuilder sb = new StringBuilder();
        for (byte b : block) {
            sb.append(String.format("%02x", b));
        }
        System.out.println(sb.toString());
        for (int i = 16; i < text.length; i+=16) {
            byte[] part = Arrays.copyOfRange(text, i, i + 16);
            byte[] tmp = decrypt(part, round, state, key);
//            if (block == null) 
//                block = iv;
            tmp = xor(cipher, tmp);
            //block = part;
            cipher = part;
            StringBuilder sb1 = new StringBuilder();
            for (byte b : tmp) {
                sb1.append(String.format("%02x", b));
             }
             System.out.println(sb1.toString());
        }
    }
    
    //An Xor is used in the CBC mode
    public byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[Math.min(a.length, b.length)];
        for (int j = 0; j < result.length; j++) {
            int xor = a[j] ^ b[j];
            result[j] = (byte) (0xff & xor);
        }
        return result;
    }
}
