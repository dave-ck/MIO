package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogsActivity extends AppCompatActivity {
    public static final String LOGS_FILENAME = "logs.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        TextView debugTextView = (TextView) findViewById(R.id.debugTextView);

        debugTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                String logs = FileAccess.readFromFile(context, LOGS_FILENAME);
                if (logs != null) {
                    debugTextView.setText(logs);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", logs);
                    clipboard.setPrimaryClip(clip);
//                    debugTextView.setBackgroundColor(0xFF00FF00);
                } else { // TODO: Verify that this correctly informs the user when an error has occurred - crash cleanly.
                    debugTextView.setText("Error accessing logs");
//                    debugTextView.setBackgroundColor(0xFFFF0000);
                }


            }
        });

        String mockEntry = "" + System.currentTimeMillis() + ": accessed log\n";
        FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);

        // todo: add functionality to reset log (big red button, require confirmation)


    }


}