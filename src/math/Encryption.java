package math;

import java.util.Arrays;

public class Encryption {
    
    // Main encryption mechanism
    private byte[] encrypt(byte[] text, int Nr, int[][][] state, int[] key) {
        int actual = 0;
        byte[] out = new byte[text.length];

        //Changes the byte array to an int array
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
        actual = 0;
        MatrixOpps opps = new MatrixOpps();
        KeyExpansion exp = new KeyExpansion();
        
        //Initial key round 
        opps.addRoundKey(state[1], actual, key);
        //exp.subBytes(state[1]);

        //Repeats the main process of the AES based on the 
        //number of rounded needed for a particular key
        for (actual = 1; actual < Nr; actual++) {
            exp.subBytes(state[1]); //SBox
            opps.shiftRows(state[1]); //Shifts Rows
            opps.mixCol(false);
            opps.mix(state[1]); //Mixes Columns
            opps.addRoundKey(state[1], actual, key); //Add Round Key
        }
        exp.subBytes(state[1]); //Sbox
        opps.shiftRows(state[1]); //Shifts Rows
        opps.addRoundKey(state[1], Nr, key); //Add Round Key
        
        //Converts and int array to a byte array
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                out[i * 4 + j] = (byte) (state[1][j][i] & 0xff);
            }
        }
        return out;
    }
    
    //Method calls the encrypt function and prints the result in hex string format
    public void encryptECB(byte[] text, int round, int[][][] state, int[] key)  {
        for (int i = 0; i < text.length; i+=16) {
                byte[] result = encrypt(Arrays.copyOfRange(text, i, i + 16), round, state, key);
                StringBuilder sb = new StringBuilder();
                for (byte b : result) {
                    sb.append(String.format("%02x", b));
                }
                System.out.println(sb.toString());
        }
    }
    
    //Methods calls the encrypt function and prints the result in hex string format
    //Uses the initial vector to determine and track the previous block in CBC mode 
    public void encryptCBC(byte[] text, byte[] iv, int round, int[][][] state, int[] key) {
        byte[] block = null;
        block = encrypt(Arrays.copyOfRange(text, 0, 16), round, state, key);
        StringBuilder sb = new StringBuilder();
        for (byte b : block) {
            sb.append(String.format("%02x", b));
        }
        System.out.println(sb.toString());
        for (int i = 16; i < text.length; i+=16) {
            byte[] part = Arrays.copyOfRange(text, i, i + 16);
            if (block == null) 
                block = iv;
            part = xor(block, part);
            block = encrypt(part, round, state, key);
            
            StringBuilder sb1 = new StringBuilder();
            for (byte b : block) {
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
