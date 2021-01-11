package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_NORTHUMBRIA = "mio.dotdotdash.org.NORTHUMBRIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button appendStrBtn = (Button) findViewById(R.id.appendStrBtn);
        Button resetBtn = (Button) findViewById(R.id.resetBtn);
        Button playBtn = (Button) findViewById(R.id.playBtn);
        TextView outputTextView = (TextView) findViewById(R.id.outputTextView);
        TextView debugTextView = (TextView) findViewById(R.id.debugTextView);
        EditText appendStrEditText = (EditText) findViewById(R.id.appendStrEditText);
        final int[] blanksSeen = {0}; // why the need for a 1-element arr? pointer-type logic?
        final String[] symSequence = {""};
        MorseCoder mc = new MorseCoder();
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        outputTextView.setText("");
        outputTextView.append("AT");
        appendStrEditText.setText("");
        appendStrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get String from appendStr field
                if (!outputTextView.getText().toString().isEmpty()) {
                    outputTextView.append("\n");
                }
                outputTextView.append(appendStrEditText.getText());
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputTextView.setText("");
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 26) {
                    debugTextView.setText("\nAPI GEQ 26 DETECTED");
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    String txtOut = "API < 26 DETECTED";
                    try {
                        long[] pitter = mc.playableSeq(outputTextView.getText().toString());
                        vibrator.vibrate(pitter, -1);
                    } catch (Exception e) {
                        txtOut += "\nError parsing string to Morse";
                        txtOut += "\nWith input:\n";
                        txtOut += outputTextView.getText().toString();
                        txtOut += e.getMessage();
                    }
                    debugTextView.setText(txtOut);

                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void toMorsePracticeActivity(View v){
        TextView debugTextView = findViewById(R.id.debugTextView);
        Intent intent = new Intent(this, MorsePracticeActivity.class);
        EditText editText = (EditText) findViewById(R.id.appendStrEditText);
        String message = editText.getText().toString();
        ArrayList<String> script = new ArrayList<>();
        script.add("T");
        script.add("H");
        script.add("A");
        script.add("I");
        script.add("E");
        script.add("C");
        script.add("HI");
        script.add("EAT");
        script.add("ACE");
        script.add("HAT");
        script.add("CAT");
        script.add("THAT");
        script.add("HEAT");
        script.add("CHAT");
        intent.putExtra(EXTRA_NORTHUMBRIA, script);
        startActivity(intent);
    }

    public void toLogsActivity(View view) {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }
}