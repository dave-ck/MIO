package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MorsePracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_practice);

        Intent intent = getIntent();
        ArrayList<String> northumbria = (ArrayList<String>) intent.getSerializableExtra(MainActivity.EXTRA_NORTHUMBRIA);

        // Capture the layout's TextView and set the string as its text
        TextView promptTextView = (TextView) findViewById(R.id.promptTextView);
        EditText typedEditText = (EditText) findViewById(R.id.typedEditText);
        TextView answerTextView = (TextView) findViewById(R.id.answerTextView);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        MorseCoder mc = new MorseCoder();
        String prompt = northumbria.get(0);
        promptTextView.setText(prompt);

        promptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (s.toString().equals(prompt)) {
                    // correct!
                    answerTextView.setText("Correct!");
                    answerTextView.setTextColor(0xFF00FF00);
                    // do some bookkeeping - ensure logs are kept somewhere "safe" in case of accidental app quit
                    // queue next question, if it exists

                } else if (!prompt.startsWith(s.toString())) {
                    // wrong - nothing good can come of this. Only madness lies this way
                    answerTextView.setTextColor(0xFFFF0000);
                } else if (s.length() == 0) {
                    answerTextView.setTextColor(0xFF000000);
                }

            }
        });


    }
}