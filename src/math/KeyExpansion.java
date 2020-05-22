package math;

public class KeyExpansion {

    //The key expansion core includes a rotate left, sbox and a round constant
    public int[] keyExpansion(int rounds, int type, byte[] key, int[] expandedKey) {
        int temp, i = 0;
        while (i < type) {
            expandedKey[i] = toWord(key[4 * i], key[4 * i + 1], key[4 * i + 2],
                          key[4 * i + 3]);
            i++;
        }
        i = type;
        while (i < 4 * (rounds + 1) + type - 4) {
            temp = expandedKey[i - 1];
            if (i % type == 0) {
                temp = substitution(rotateLeft(temp)) ^ SBox.rCon[i / type]; //rotate left, sbox and a round constant
            } else if (type > 6 && (i % type == 4)) {
                temp = substitution(temp);
            } 
            expandedKey[i] = expandedKey[i - type] ^ temp;
            i++;
        }
        return expandedKey;
    }
    
    //Rotates left each byte one position to the left
    private int rotateLeft(int word) {
        return (word << 8) ^ ((word >> 24) & 0x000000ff);
    }
    
    //Converts bytes to int
    private int toWord(byte b1, byte b2, byte b3, byte b4) {
        int word = 0;
        word ^= ((int) b1) << 24;

        word ^= (((int) b2) & 0x000000ff) << 16;

        word ^= (((int) b3) & 0x000000ff) << 8;

        word ^= (((int) b4) & 0x000000ff);
        return word;
    }

    //Substituted one int
    public int substitution(int word) {
        int subWord = 0;
        for (int i = 24; i >= 0; i -= 8) {
            int in = word << i >>> 24;
            subWord |= SBox.box[in] << (24 - i);
        }
        return subWord;
    }
    
    //Inverse substitution of one int
    public int invSub(int word) {
        int subWord = 0;
        for (int i = 24; i >= 0; i -= 8) {
            int in = word << i >>> 24;
            subWord |= SBox.inverseBox[in] << (24 - i);
        }
        return subWord;
    }
    
    //Inverse Sbox
    public int[][] invSubBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = invSub(state[i][j]) & 0xFF;
            }
        }
        return state;
    }
    
    //Sbox
    // replaces each 8-bit byte of the 128-bit block with a different 8 bits
    public int[][] subBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = substitution(state[i][j]) & 0xFF;
            }
        }
        return state;
    }
    
}
