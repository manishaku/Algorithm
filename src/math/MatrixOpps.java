package math;

public class MatrixOpps {

    private static byte[][] matrix = { { 0x03, 0x0b }, { 0x01, 0x0d }, { 0x01,
        0x09 }, { 0x02, 0x0e } };

    private byte a = 0, b = 0, c = 0, d = 0;

    //Mix columns either in decrypt or encrypt mode
    public void mixCol(boolean decrypt) {
        int type = 0;
        if (decrypt) {
            type = 1;
        }
        a = matrix[0][type];
        b = matrix[1][type];
        c = matrix[2][type];
        d = matrix[3][type];
    }

    //Integer multiplication
    public int multi(int a, int b) {
        int sum = 0;
        while (a != 0) { 
            if ((a & 1) != 0) { 
                sum = sum ^ b; 
            }
            if ((b & 0x80) == 0) {
                b = b << 1;
            }
            else {
                b = (b << 1) ^ 0x11b;
            }
            a = a >>> 1; 
        }
        return sum;
    }

    //Mix columns process
    public int[][] mix(int[][] input) {
        int[] temp = new int[4];
        for (int i = 0; i < 4; i++) {
            temp[0] = multi(d, input[0][i]) ^ multi(a, input[1][i])
                ^ multi(b, input[2][i]) ^ multi(c, input[3][i]);
            temp[1] = multi(c, input[0][i]) ^ multi(d, input[1][i])
                ^ multi(a, input[2][i]) ^ multi(b, input[3][i]);
            temp[2] = multi(b, input[0][i]) ^ multi(c, input[1][i])
                ^ multi(d, input[2][i]) ^ multi(a, input[3][i]);
            temp[3] = multi(a, input[0][i]) ^ multi(b, input[1][i])
                ^ multi(c, input[2][i]) ^ multi(d, input[3][i]);
            for (int j = 0; j < 4; j++) {
                input[j][i] = (byte)(temp[j]);
            }  
        }
        return input;
    }

    //Shift rows 
    //Second row is shifted one byte to the left, third row two bytes, 
    //fourth row three bytes
    public void shiftRows(int[][] state) {
        int temp1, temp2, temp3, i;

        // 1st row
        temp1 = state[1][0];
        for (i = 0; i < 4 - 1; i++) {
            state[1][i] = state[1][(i + 1) % 4];
        }
        state[1][4 - 1] = temp1;

        // 2nd row, moves 1-byte
        temp1 = state[2][0];
        temp2 = state[2][1];
        for (i = 0; i < 4 - 2; i++) {
            state[2][i] = state[2][(i + 2) % 4];
        }
        state[2][4 - 2] = temp1;
        state[2][4 - 1] = temp2;

        // 3rd row, moves 2-bytes
        temp1 = state[3][0];
        temp2 = state[3][1];
        temp3 = state[3][2];
        for (i = 0; i < 4 - 3; i++) {
            state[3][i] = state[3][(i + 3) % 4];
        }
        state[3][4 - 3] = temp1;
        state[3][4 - 2] = temp2;
        state[3][4 - 1] = temp3;

    }
    
    //Inverse of shift rows for decryption process
    public int[][] invShiftRows(int[][] state) {
        int temp1, temp2, temp3, i;

        // 1st row
        temp1 = state[1][4 - 1];
        for (i = 4 - 1; i > 0; i--) {
            state[1][i] = state[1][(i - 1) % 4];
        }
        state[1][0] = temp1;
        // 2nd row
        temp1 = state[2][4 - 1];
        temp2 = state[2][4 - 2];
        for (i = 4 - 1; i > 1; i--) {
            state[2][i] = state[2][(i - 2) % 4];
        }
        state[2][1] = temp1;
        state[2][0] = temp2;
        // 3rd row
        temp1 = state[3][4 - 3];
        temp2 = state[3][4 - 2];
        temp3 = state[3][4 - 1];
        for (i = 4 - 1; i > 2; i--) {
            state[3][i] = state[3][(i - 3) % 4];
        }
        state[3][0] = temp1;
        state[3][1] = temp2;
        state[3][2] = temp3;

        return state;
    }

    //the round key process that 
    // XOR’s the 128-bit block with the 128-bit scheduled key
    // the key scheduler produces a different key for each round
    public int[][] addRoundKey(int[][] s, int round, int[] arr) {
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                s[r][c] = s[r][c] ^ ((arr[round * 4 + c] << (r * 8)) >>> 24);
            }
        }
        return s;
    }
}
