package mio.dotdotdash.org;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;


public class MorseInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    String symSequence;
    int blanksSeen;
    MorseCoder mc;
    boolean morseMode; // do Morse input; when false, do touch recording.
    boolean seqStarted; //flags whether or not we have started to record touches - false until 1st touch
    ArrayList<Long> recording;
    long last, now;
    Vibrator vibrator;

    @Override
    public View onCreateInputView() {
        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.morse_keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.morse_pad);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        symSequence = "";
        blanksSeen = 0;
        mc = new MorseCoder();
        morseMode = true;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        return keyboardView;
    }

    @Override
    public void onPress(int i) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (!morseMode) {
            now = System.currentTimeMillis();
            if (seqStarted) {
                long diff = now - last;
                inputConnection.commitText("" + diff + " ", 1);
            } else {
                seqStarted = true;
                inputConnection.commitText("0 ", 1);
            }
            last = now;

        }
    }

    @Override
    public void onRelease(int i) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (!morseMode) {
            now = System.currentTimeMillis();
            long diff = now - last;
            inputConnection.commitText("" + diff + " ", 1);
            last = now;
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (morseMode) {
            // if sequence of blanks just ended, we must add whitespace
            if (primaryCode != 62 && blanksSeen > 0) {
                if (blanksSeen == 2) {
                    // also add a space character
                    inputConnection.commitText(" ", 1);
                } else if (blanksSeen == 3) {
                    // also add a newline character (or custom action)
                    // todo: extend to actually trigger sending
                    inputConnection.commitText("\n", 1);
                }
                symSequence = "";
                blanksSeen = 0;
            }
            switch (primaryCode) {
                case (56): // dot
                    vibrator.vibrate((long) 100);
                    symSequence += ".";
                    break;
                case (69): // dash
                    vibrator.vibrate((long) 300);
                    symSequence += "-";
                    break;
                case (62): // space
                    vibrator.vibrate((long) 30);
                    blanksSeen += 1;
                    String next = mc.morseLookup(symSequence);
                    inputConnection.commitText(next, 1);
                    symSequence = "";
                    break;
            }
        }
    }

    @Override
    public void onText(CharSequence charSequence) {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.commitText(" text:' " + charSequence.toString() + " '", 1);
    }

    @Override
    public void swipeLeft() {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.deleteSurroundingText(Character.MAX_CODE_POINT, 0);
        symSequence = "";
        vibrator.vibrate((long) 600);
        morseMode = true;

    }

    @Override
    public void swipeRight() {
//        InputConnection inputConnection = getCurrentInputConnection();
//        inputConnection.commitText("RIGHT", 1);
    }

    @Override
    public void swipeDown() {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.commitText(" \\ ", 1);
        vibrator.vibrate((long) 2000);
        // toggle mode
        if (morseMode) {
            morseMode = false;
            seqStarted = false;
            recording = new ArrayList<>();
            last = System.currentTimeMillis();
        } else {
            morseMode = true;
            symSequence = "";
        }
    }

    @Override
    public void swipeUp() {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.commitText("UP", 1);
    }
}