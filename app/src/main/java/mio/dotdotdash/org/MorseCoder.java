package mio.dotdotdash.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MorseCoder {

    private long DOT;
    private long DASH;
    private long AND;
    private long SPACE_EXTRA;
    private long ZERO;
    private long LETSEP;

    private long[][] charVibe;

    private long[][] jingles;
    private HashMap<String, String> morseToText;

    public MorseCoder() {
        // international morseFor code
        DOT = 100;             // dot is one unit (here 100ms)
        DASH = 300;            // dash is 3 units
        AND = 100;             // space between parts of the same letter is one unit
        LETSEP = 300;          // space between letters is three units
        SPACE_EXTRA = 400;     // space character is seven units (four more than the space between characters)
        ZERO = 0;

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
                {ZERO, SPACE_EXTRA, ZERO},                         // Space, index 37
                {DOT, AND, DOT, AND, DOT, AND, DOT, AND, DOT, AND, DOT, AND, DOT, AND, DOT} // #, ERROR, index 38
        };

        jingles = new long[][]{ //playable as standalone, so start with time OFF - 300ms to separate from other outputs
                {300, 50, 50, 250, 100, 250},            // 0: WRONG - 50ms on, 50ms pause, then 250-100-250 (600 as when deleting, bisected by 100 pause)
                {300, 75, 75, 75, 75, 75, 75, 75, 75}          // 0: RIGHT - many (4) quick 75ms vibrations
        };

        morseToText = new HashMap<>();
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
        morseToText.put("........", "#");
    }

    public long[] playableSeq(String in) {
        Scanner sc = new Scanner(in);
        String next;
        ArrayList<Long> seq = new ArrayList<>();
        boolean err = false; // flag to check if exception was raised
        while (sc.hasNext()) {
            next = sc.next();
            if (next.equals("\\")) {
                // switch to "playback mode" - simply append everything directly
//                if (sc.hasNextLong())
//                    sc.nextLong(); // skip the first value - this represents the length of the pause before recording
                /* above: include this - we need the pause in**/
                while (sc.hasNextLong()) seq.add(sc.nextLong()); // add all remaining longs
                // the final pause is replaced, in all cases, with a 500ms pause
                seq.remove(seq.size() - 1);
                seq.add((long) 500);

            } else {
                // first, add a SPACE
                seq.add(SPACE_EXTRA);
                seq.add(ZERO);
                // then, add all letters from the current word
                long[] vibes = morseFor(next);
                for (long vibe : vibes) {
                    seq.add(vibe);
                }
            }
        }
        // construct output
        long[] out = new long[seq.size()];
        for (int i = 0; i < seq.size(); i++) {
            out[i] = seq.get(i);  //toArray yields array of Longs (wrapper), need array of longs (primitive)
        }
        return out;

    }

    public long[] morseFor(String in) {
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

    public long[] morseFromChar(char c) {
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
        } else { // other character - ERROR code ........ (8 dots) (automatically catches '#' char)
            return charVibe[38];
        }

    }

    public String fromMorse(String in) {
        StringBuilder out = new StringBuilder();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            String sym = in.substring(i, i + 1);
            if (sym.equals(" ")) {
                out.append(morseLookup(current.toString()));
                current = new StringBuilder();
            } else {
                current.append(sym);
            }
        }
        return out.toString();
    }

    public String morseLookup(String in) {
        if (morseToText.containsKey(in)) return morseToText.get(in);
        else
            return "#"; // error is 8 dots under ITU M.1677-1 https://www.itu.int/rec/R-REC-M.1677-1-200910-I/
    }

    /**
     * @param id - the id of the desired jingle:
     *           0 for WRONG,
     *           1 for RIGHT/CORRECT
     * @return A long[] starting with the pause length - can be fed directly to Vibrator.vibrate(long[] pattern, int repeat)
     */
    public long[] getJingle(int id) {
        return jingles[id];
    }

}
