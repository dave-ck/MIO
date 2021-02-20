package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class PlaybackActivity extends AppCompatActivity {
    public static final String LOGS_FILENAME = "logs.txt";

    String prompts[];
    int current;
    TextView promptTextView;
    EditText typedEditText;
    TextView answerTextView;
    Vibrator vibrator;
    MorseCoder mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        promptTextView = (TextView) findViewById(R.id.playbackWordTextView);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mc = new MorseCoder();
        // TODO: implement loading of clipboard script
        prompts = FileAccess.getStringAsset(getApplicationContext(), "ABCs.txt").split("\\r?\\n");
        current = 0;
        // load and play first prompt
        promptTextView.setText("PLAYBACK");
        playPrompt();
        // tapping the screen loads and plays the next prompt
        promptTextView.setOnClickListener(v -> {
            nextPrompt();
        });
    }

    public void playPrompt(){
        try {
            long[] pitter = mc.playableSeq(promptTextView.getText().toString());
            vibrator.vibrate(pitter, -1);
            String mockEntry = "" + System.currentTimeMillis() + ": played prompt";
            FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
        } catch (Exception e) {
            String txtOut = "Error parsing string to Morse";
            txtOut += "\nWith input:\n";
            txtOut += answerTextView.getText().toString();
            txtOut += e.getMessage();
            FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, txtOut);
        }
    }

    public void nextPrompt() {
        if (current >= prompts.length) {
            current = 0;
        }
        String prompt = prompts[current].toUpperCase();
        promptTextView.setText(prompt);
        playPrompt();
        String mockEntry = "@playback: " + System.currentTimeMillis() + ": began prompt \"" + prompt + "\"\n";
        FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
        current ++;
    }
}