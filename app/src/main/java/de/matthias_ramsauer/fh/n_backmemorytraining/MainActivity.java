package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    public void onClickConfig(View view) {
        final Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
    }

    public void onClickStart(View view) {
        final Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}
