package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.*;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {
    public static final String LOGS_FILENAME = "logs.txt";
    Button logsCopyButton;
    TextView scriptTitleTextView;
    TextView scriptPreviewTextView;
    Button scriptPasteButton;
    Button scriptResetButton;
    SharedPreferences prefs;
    boolean logsCopied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        scriptResetButton = (Button) findViewById(R.id.scriptResetButton);
        logsCopyButton = (Button) findViewById(R.id.logCopyButton);
        scriptTitleTextView = (TextView) findViewById(R.id.scriptTitleTextView);
        scriptPreviewTextView = (TextView) findViewById(R.id.scriptPreviewTextView);
        scriptPasteButton = (Button) findViewById(R.id.scriptPasteButton);
        prefs = getSharedPreferences("org.dotdotdash.mio", MODE_PRIVATE);
        logsCopied = false;

        Context t = this;
        logsCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logsCopied) {
                    Intent intent = new Intent(t, LandingActivity.class);
                    startActivity(intent);
                }
                Context context = getApplicationContext();
                String logs = FileAccess.readFromFile(context, LOGS_FILENAME);
                if (logs != null) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", logs);
                    clipboard.setPrimaryClip(clip);
                    logsCopyButton.setText("Logs Copied to Clipboard! Return to main?");
                    logsCopyButton.setBackgroundColor(0xFF00FF00);
                    logsCopied = true;
                } else { // TODO: Verify that this correctly informs the user when an error has occurred - crash cleanly.
                    logsCopyButton.setText("Error accessing logs");
                    logsCopyButton.setBackgroundColor(0xFFFF0000);
                }
            }
        });

        scriptPasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String script = clipboard.getPrimaryClip().getItemAt(0).coerceToText(getApplicationContext()).toString();
                scriptPasteButton.setText("Script loaded!");
                scriptPasteButton.setBackgroundColor(0xFF00FF00);
                FileAccess.writeToFile(context, FileAccess.USERSCRIPT_FILENAME, script); //todo: confirm with user
                prefs.edit().putBoolean("UserScript", true).apply();
                updatePreview();
            }
        });

        scriptResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean("UserScript", false).apply();
                scriptPasteButton.setText("Load Script from Clipboard");
                scriptPasteButton.setBackgroundColor(0xFF6200EE);
                updatePreview();
            }
        });
        String mockEntry = "@settings: " + System.currentTimeMillis() + ": accessed logs & settings\n";
        FileAccess.appendToFile(getApplicationContext(), LOGS_FILENAME, mockEntry);
        updatePreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        MorseCoder mc = new MorseCoder();
        vibrator.vibrate(mc.playableSeq("SET"), -1);
    }

    public void updatePreview() {
        if (prefs.getBoolean("UserScript", false)) { // by default, use the random script
            String script = FileAccess.readFromFile(getApplicationContext(), FileAccess.USERSCRIPT_FILENAME);
            scriptTitleTextView.setText("Currently using custom user script:");
            scriptPreviewTextView.setText(script);
        } else { // if random script, preview some words from CKOgden
            String script = FileAccess.getStringAsset(getApplicationContext(), FileAccess.PRACTICE_WORDLIST_FILENAME);
            scriptTitleTextView.setText("Currently using default wordlist (random order):");
            scriptPreviewTextView.setText(script);
        }
    }


}