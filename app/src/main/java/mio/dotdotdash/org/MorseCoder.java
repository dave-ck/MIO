package mio.dotdotdash.org;

import java.util.HashMap;

public class MorseCoder {

    private long DOT;
    private long DASH;
    private long AND;
    private long SPACE_EXTRA;
    private long ZERO;
    private long LETSEP;

    private long[][] charVibe;
    private HashMap<String, String> morseToText;

    public MorseCoder() {
        // international morseFor code
        DOT = (long) 100.0;             // dot is one unit (here 100ms)
        DASH = (long) 300.0;            // dash is 3 units
        AND = (long) 100.0;             // space between parts of the same letter is one unit
        LETSEP = (long) 300.0;          // space between letters is three units
        SPACE_EXTRA = (long) 400.0;     // space character is seven units (four more than the space between characters)
        ZERO = (long) 0.0;

        charVibe = new long[][]{ // time on, time off, time on, ..., time off, time on; always start & end with time ON
                {DOT, AND, DASH},            // A, AND,  index 0
                {DASH, AND, DOT, AND, DOT, AND, DOT},  // B
                {DASH, AND, DOT, AND, DASH, AND, DOT}, // C
                {DASH, AND, DOT, AND, DOT},       // ...
                {DOT},
                {DOT, AND, DOT, AND, DASH, AND, DOT},
                {DASH, AND, DASH, AND, DOT},
                {DOT, AND, DOT, AND, DOT, AND, DOT},
                {DOT, AND, DOT},
                {DOT, AND, DASH, AND, DASH, AND, DASH},
                {DASH, AND, DOT, AND, DASH},
                {DOT, AND, DASH, AND, DOT, AND, DOT},
                {DASH, AND, DASH},
                {DASH, AND, DOT},
                {DASH, AND, DASH, AND, DASH},
                {DOT, AND, DASH, AND, DASH, AND, DOT},
                {DASH, AND, DASH, AND, DOT, AND, DASH},
                {DOT, AND, DASH, AND, DOT},
                {DOT, AND, DOT, AND, DOT},
                {DASH},
                {DOT, AND, DOT, AND, DASH},
                {DOT, AND, DOT, AND, DOT, AND, DASH},
                {DOT, AND, DASH, AND, DASH},
                {DASH, AND, DOT, AND, DOT, AND, DASH},
                {DASH, AND, DOT, AND, DASH, AND, DASH},
                {DASH, AND, DASH, AND, DOT, AND, DOT},         // Z, AND,  index 25
                {DOT, AND, DASH, AND, DASH, AND, DASH, AND, DASH},      // 1, AND,  index 26
                {DOT, AND, DOT, AND, DASH, AND, DASH, AND, DASH},       // 2
                {DOT, AND, DOT, AND, DOT, AND, DASH, AND, DASH},        // ...
                {DOT, AND, DOT, AND, DOT, AND, DOT, AND, DASH},
                {DOT, AND, DOT, AND, DOT, AND, DOT, AND, DOT},
                {DASH, AND, DOT, AND, DOT, AND, DOT, AND, DOT},
                {DASH, AND, DASH, AND, DOT, AND, DOT, AND, DOT},
                {DASH, AND, DASH, AND, DASH, AND, DOT, AND, DOT},
                {DASH, AND, DASH, AND, DASH, AND, DASH, AND, DOT},      // 9
                {DASH, AND, DASH, AND, DASH, AND, DASH, AND, DASH},     // 0, AND,  index 35
                {DOT, AND, DOT, AND, DASH, AND, DOT, AND, DOT},     // accented e: é, AND,  ê, AND,  è, AND,  index 36
                {ZERO, SPACE_EXTRA, ZERO}                              // Space, index 37
        };

        morseToText = new HashMap<String, String>();
        morseToText.put("", "");
        morseToText.put(".-", "A");
        morseToText.put("-...", "B");
        morseToText.put("-.-.", "C");
        morseToText.put("-..", "D");
        morseToText.put(".", "E");
        morseToText.put("..-.", "F");
        morseToText.put("--.", "G");
        morseToText.put("....", "H");
        morseToText.put("..", "I");
        morseToText.put(".---", "J");
        morseToText.put("-.-", "K");
        morseToText.put(".-..", "L");
        morseToText.put("--", "M");
        morseToText.put("-.", "N");
        morseToText.put("---", "O");
        morseToText.put(".--.", "P");
        morseToText.put("--.-", "Q");
        morseToText.put(".-.", "R");
        morseToText.put("...", "S");
        morseToText.put("-", "T");
        morseToText.put("..-", "U");
        morseToText.put("...-", "V");
        morseToText.put(".--", "W");
        morseToText.put("-..-", "X");
        morseToText.put("-.--", "Y");
        morseToText.put("--..", "Z");
        morseToText.put(".----", "1");
        morseToText.put("..---", "2");
        morseToText.put("...--", "3");
        morseToText.put("....-", "4");
        morseToText.put(".....", "5");
        morseToText.put("-....", "6");
        morseToText.put("--...", "7");
        morseToText.put("---..", "8");
        morseToText.put("----.", "9");
        morseToText.put("-----", "0");

    }

    public long[] morseFor(String in) throws Exception {
        // compute necessary length for entire code:
        in = in.replace("\n", "  ");
        int outLen = 0;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            outLen += 1 + morseFromChar(c).length; // need to add a LETSEP, and the contents of the array up to that point
        }
        // allocate, then fill the array
        long[] out = new long[outLen];
        int p = 0; // pointer to next location for writing to
        int letEnd; // pointer to end of block corresponding to current letter
        int offset; // of the current letter
        long[] addendum;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            out[p] = LETSEP;
            p += 1;
            addendum = morseFromChar(c);
            letEnd = p + addendum.length;
            offset = p;
            while (p < letEnd) {
                out[p] = addendum[p - offset];
                p += 1;
            }
        }
        return out;
    }

    public long[] morseFromChar(char c) throws Exception {
        if (c <= 123 & c >= 97) { // lowercase letter
            return charVibe[(c - 97)];
        } else if (c <= 90 & c >= 65) { // upper letter
            return charVibe[(c - 65)];
        } else if (c <= 57 & c >= 48) { // number character
            return charVibe[(c - 22)]; // -48 + 26
        } else if (c == 32) { // space character
            return charVibe[37];
        } else if (c <= 234 & c >= 232) { // accented e
            return charVibe[36];
        } else { // other character - treat as punctuation
            throw new Exception("Character Morse representation nonexistent or not implemented: character " + c);
        }

    }

    public String fromMorse(String in) {
        StringBuilder out = new StringBuilder();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            String sym = in.substring(i, i + 1);
            if (sym.equals(" ")) {
                out.append(morseToText.get(current.toString()));
                current = new StringBuilder();
            } else {
                current.append(sym);
            }
        }
        return out.toString();
    }
}
