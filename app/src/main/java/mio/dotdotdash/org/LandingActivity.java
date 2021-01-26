package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {
    public static final String EXTRA_NORTHUMBRIA = "mio.dotdotdash.org.NORTHUMBRIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }



    @SuppressLint("SetTextI18n")
    public void toMorsePracticeActivity(View v){
        Intent intent = new Intent(this, MorsePracticeActivity.class);
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

    @SuppressLint("SetTextI18n")
    public void toPracticeActivity(View v){
        Intent intent = new Intent(this, PracticeActivity.class);
        startActivity(intent);
    }



    public void toLogsActivity(View view) {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }
}