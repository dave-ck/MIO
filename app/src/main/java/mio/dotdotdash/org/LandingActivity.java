package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {
    public static final String EXTRA_NORTHUMBRIA = "mio.dotdotdash.org.NORTHUMBRIA";
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        prefs = getSharedPreferences("org.dotdotdash.mio", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
// below: uncomment for debugging logs; leave commented for prod
//        if (prefs.getBoolean("firstrun", true)) {
//            // Do first run stuff here then set 'firstrun' as false
//            String firstEntry = "@Landing: " + System.currentTimeMillis() + ": logfile created\n";
//            FileAccess.writeToFile(getApplicationContext(), SettingsActivity.LOGS_FILENAME, firstEntry);
//            prefs.edit().putBoolean("firstrun", false).apply();
//        }

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        MorseCoder mc = new MorseCoder();
        vibrator.vibrate(mc.playableSeq("MIO"), -1);
    }


    @SuppressLint("SetTextI18n")
    public void toMorsePracticeActivity(View v) {
        Intent intent = new Intent(this, MorsePracticeActivity.class);
        ArrayList<String> script = new ArrayList<>();
        script.add("T");
        script.add("H");
        script.add("E");
        script.add("THE");
        script.add("C");
        script.add("A");
        script.add("T");
        script.add("CAT");
        script.add("E");
        script.add("A");
        script.add("T");
        script.add("S");
        script.add("EATS");
        script.add("THE CAT EATS");
        script.add("T");
        script.add("H");
        script.add("E");
        script.add("THE");
        script.add("C");
        script.add("A");
        script.add("K");
        script.add("E");
        script.add("CAKE");
        script.add("THE CAT EATS THE CAKE");
        intent.putExtra(EXTRA_NORTHUMBRIA, script);
        startActivity(intent);
    }

    public void toPracticeActivity(View v) {
        Intent intent = new Intent(this, PracticeActivity.class);
        startActivity(intent);
    }

    public void toABCPracticeActivity(View view) {
        Intent intent = new Intent(this, ABCPracticeActivity.class);
        startActivity(intent);
    }

    public void toLogsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void toPlaybackActivity(View v) {
        Intent intent = new Intent(this, PlaybackActivity.class);
        startActivity(intent);
    }
}