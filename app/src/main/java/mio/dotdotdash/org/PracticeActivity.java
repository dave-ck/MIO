package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class PracticeActivity extends AppCompatActivity {
    public static final String LOGS_FILENAME = "logs.txt";

    String prompts[];
    int current;
    TextView promptTextView;
    EditText typedEditText;
    TextView answerTextView;
    Vibrator vibrator;
    MorseCoder mc;
    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        //Intent intent = getIntent();
        promptTextView = (TextView) findViewById(R.id.promptTextView);
        typedEditText = (EditText) findViewById(R.id.typedEditText);
        answerTextView = (TextView) findViewById(R.id.answerTextView);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        r = new Random();
        mc = new MorseCoder();
        prompts = FileAccess.getStringAsset(getApplicationContext(), "CKOgden.txt").split("\\r?\\n");

        // show keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // load in prompt
        nextPrompt();


        promptTextView.setOnClickListener(v -> {
            String mockEntry = "" + System.currentTimeMillis() + ": played prompt";
            FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
            if (Build.VERSION.SDK_INT >= 26) {
                answerTextView.setText("\nAPI GEQ 26 DETECTED");
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                String txtOut = "API < 26 DETECTED";
                try {
                    long[] pitter = mc.playableSeq(promptTextView.getText().toString());
                    vibrator.vibrate(pitter, -1);
                } catch (Exception e) {
                    txtOut += "\nError parsing string to Morse";
                    txtOut += "\nWith input:\n";
                    txtOut += answerTextView.getText().toString();
                    txtOut += e.getMessage();
                    answerTextView.setText(txtOut);
                }
            }
        });

        typedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                answerTextView.setText(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                answerTextView.setText(s);
                String prompt = promptTextView.getText().toString();
                if (s.toString().equals(prompt)) {
                    // correct!
                    answerTextView.setText("Correct!");
                    String mockEntry = "@practice: " + System.currentTimeMillis() + ": finished prompt \"" + prompt + "\" successfully\n";
                    FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
                    answerTextView.setTextColor(0xFF00FF00);
                    vibrator.vibrate(mc.getJingle(1), -1);
                    current++;
                    nextPrompt();
                } else if (!prompt.startsWith(s.toString())) {
                    // wrong - nothing good can come of this. Only madness lies this way
                    answerTextView.setTextColor(0xFFFF0000);
                    vibrator.vibrate(mc.getJingle(0), -1);
                    String mockEntry = "@practice: " + System.currentTimeMillis() + ": failed prompt \"" + prompt + "\" with string \"" + s + "\"\n";
                    FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
                } else if (s.length() == 0) {
                    answerTextView.setTextColor(0xFF000000);
                }

            }
        });
    }

    public void nextPrompt() {
        // do bookkeeping for the prompt just completed
        // if there is another prompt

        int rand = r.nextInt(prompts.length);
        String prompt = prompts[rand].toUpperCase();
        String mockEntry = "@practice: " + System.currentTimeMillis() + ": began prompt \"" + prompt + "\"\n";
        FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
        promptTextView.setText(prompt);
        typedEditText.setText(null);

    }
}