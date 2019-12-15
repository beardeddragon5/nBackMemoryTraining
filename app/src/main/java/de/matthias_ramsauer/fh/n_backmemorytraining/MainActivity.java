package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;

import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    public void onClickConfig(@SuppressWarnings("unused") View view) {
        final Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
    }

    public void onClickStart(@SuppressWarnings("unused") View view) {
        final NBackPreferences.EndCondition condition = NBackPreferences.getEndCondition(this);
        switch (condition) {
            case time:
                startActivity(new Intent(this, TimeGameActivity.class));
                break;
            case expression:
                startActivity(new Intent(this, ExpressionGameActivity.class));
                break;
        }
    }

    public void onClickStats(@SuppressWarnings("unused") View view) {
        final Intent i = new Intent(this, StatsActivity.class);
        startActivity(i);
    }
}
