package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

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
        outputTextView.append("SOS \\ 100 200 2000 2000 \\");
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
}