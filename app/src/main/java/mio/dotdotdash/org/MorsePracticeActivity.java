package mio.dotdotdash.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MorsePracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_practice);

        Intent intent = getIntent();
        ArrayList<String> northumbria = (ArrayList<String>) intent.getSerializableExtra(MainActivity.EXTRA_NORTHUMBRIA);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.promptTextView);
        for (int i = 0; i < northumbria.size(); i++){
            textView.setText(northumbria.get(i));
        }
    }
}