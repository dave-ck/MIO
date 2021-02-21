package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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
    SharedPreferences prefs;
    Random r;
    boolean rand_order;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        //Intent intent = getIntent();
        promptTextView = (TextView) findViewById(R.id.playbackWordTextView);
        typedEditText = (EditText) findViewById(R.id.typedEditText);
        answerTextView = (TextView) findViewById(R.id.answerTextView);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        r = new Random();
        mc = new MorseCoder();
        prompts = FileAccess.getStringAsset(getApplicationContext(), "CKOgden.txt").split("\\r?\\n");
        prefs = getSharedPreferences("org.dotdotdash.mio", MODE_PRIVATE);
        rand_order = true;
        // show keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        promptTextView.setOnClickListener(v -> {
            playPrompt();
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
                    vibrator.vibrate(600);
                    String logEntry = "" + System.currentTimeMillis() + ": failed prompt \"" + prompt + "\" with string \"" + s + "\"\n";
                    FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, logEntry);
                    typedEditText.setText("");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String script;
        if (prefs.getBoolean("UserScript", false)) { // by default, use the random script
            script = FileAccess.readFromFile(getApplicationContext(), FileAccess.USERSCRIPT_FILENAME);
        } else { // if random script, preview some words from CKOgden
            script = FileAccess.getStringAsset(getApplicationContext(), FileAccess.PRACTICE_WORDLIST_FILENAME);
        }
        prompts = script.split("\\r?\\n");
        index = -1;
        // load in prompt
        nextPrompt();
    }

    public void playPrompt(){
        String prompt = promptTextView.getText().toString();
        String logEntry = "@practice: " + System.currentTimeMillis() + ": played prompt \"" + prompt + "\"\n";
        FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, logEntry);
        try {
            long[] pitter = mc.playableSeq(promptTextView.getText().toString());
            vibrator.vibrate(pitter, -1);
        } catch (Exception e) {
            String txtOut = "@practice" + System.currentTimeMillis() +": Error parsing string to Morse";
            txtOut += "\nWith input:\n";
            txtOut += answerTextView.getText().toString();
            txtOut += "\n";
            txtOut += e.getMessage();
            FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, txtOut);
        }
    }

    public void nextPrompt() {
        if (rand_order){
            index = r.nextInt(prompts.length);
        }
        else {
            index ++;
            index = index % prompts.length;
        }
        String prompt = prompts[index].toUpperCase();
        String mockEntry = "@practice: " + System.currentTimeMillis() + ": began prompt \"" + prompt + "\"\n";
        FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
        promptTextView.setText(prompt);
        typedEditText.setText(null);
        playPrompt();
    }
}