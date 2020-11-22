package mio.dotdotdash.org;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputConnection;


public class MorseInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    String symSequence;
    int blanksSeen;
    MorseCoder mc;

    @Override
    public View onCreateInputView() {
        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.morse_keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.morse_pad);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        symSequence = "";
        blanksSeen = 0;
        mc = new MorseCoder();
        return keyboardView;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            // if sequence of blanks just ended, we must add whitespace
            if (primaryCode != 62 && blanksSeen > 0) {
                if (blanksSeen==2){
                    // also add a space character
                    inputConnection.commitText(" ", 1);
                }
                else if (blanksSeen==3){
                    // also add a newline character (or custom action)
                    // todo: extend to actually trigger sending
                    inputConnection.commitText("\n", 1);
                }
                symSequence = "";
                blanksSeen = 0;
            }
            switch (primaryCode) {
                case (56): // dot
                    symSequence += ".";
                    break;
                case (69): // dash
                    symSequence += "-";
                    break;
                case (62): // space
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
        inputConnection.deleteSurroundingText(1, 0);
        symSequence = "";
    }

    @Override
    public void swipeRight() {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.commitText("RIGHT", 1);
    }

    @Override
    public void swipeDown() {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.commitText("DOWN", 1);
    }

    @Override
    public void swipeUp() {
        InputConnection inputConnection = getCurrentInputConnection();
        inputConnection.commitText("UP", 1);
    }
}