package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlaybackActivity extends AppCompatActivity {
    public static final String LOGS_FILENAME = "logs.txt";

    String prompts[];
    int current;
    TextView promptTextView;
    EditText typedEditText;
    TextView answerTextView;
    Vibrator vibrator;
    MorseCoder mc;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        promptTextView = (TextView) findViewById(R.id.playbackWordTextView);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mc = new MorseCoder();
        // TODO: implement loading of clipboard script
        // tapping the screen loads and plays the next prompt
        Context ctx = this.getApplicationContext();
        promptTextView.setOnTouchListener(new OnSwipeTouchListener(ctx) {
            //            public void onSwipeTop() {
//                Toast.makeText(ctx, "top", Toast.LENGTH_SHORT).show();
//            }
            public void onSwipeRight() {
                toLandingActivity();
            }

            public void onTap() {
                nextPrompt();
            }
//            public void onSwipeLeft() {
//                Toast.makeText(ctx, "left", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeBottom() {
//                Toast.makeText(ctx, "bottom", Toast.LENGTH_SHORT).show();
//            }
        });

        prefs = getSharedPreferences("org.dotdotdash.mio", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String script;
        if (prefs.getBoolean("UserScript", false)) { // by default, use the random script
            script = FileAccess.readFromFile(getApplicationContext(), FileAccess.USERSCRIPT_FILENAME);
        } else { // if random script, preview some words from CKOgden
            script = FileAccess.getStringAsset(getApplicationContext(), FileAccess.ABCS_FILENAME);
        }
        prompts = script.split("\\r?\\n");
        current = 0;
        // load in prompt
        vibrator.vibrate(new long[]{100, 400, 100, 400, 100, 400, 100, 400}, -1);
        // load in prompt
        new Handler().postDelayed(() -> nextPrompt(), 2000);
    }

    public void playPrompt() {

        long[] pitter = mc.playableSeq(promptTextView.getText().toString());
        vibrator.vibrate(pitter, -1);

    }

    public void nextPrompt() {
        if (current >= prompts.length) {
            current = 0;
        }
        String prompt = prompts[current].toUpperCase();
        promptTextView.setText(prompt);
        String mockEntry = "@playback: " + System.currentTimeMillis() + ": played prompt \"" + prompt + "\"\n";
        playPrompt();
        FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
        current++;
    }

    public void toLandingActivity() {
        Intent intent = new Intent(this, LandingActivity.class);
        startActivity(intent);
    }
}