package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        TextView debugTextView = (TextView) findViewById(R.id.debugTextView);

        debugTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", "Text to copy - 1228");
                clipboard.setPrimaryClip(clip);
                debugTextView.setText("Bruh.");
                String logs = FileAccess.readFromFile(getApplicationContext(), "yeetus.txt");
                if (logs != null) debugTextView.setText(logs);
            }
        });

        FileAccess.writeToFile(getApplicationContext(), "yeetus.txt", "helloworld - 12389");


    }


}