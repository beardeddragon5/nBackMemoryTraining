package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import de.matthias_ramsauer.fh.n_backmemorytraining.ui.config.SettingsFragment;

public class ConfigActivity extends AppCompatActivity {

    private SettingsFragment settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setSupportActionBar(findViewById(R.id.toolbar));

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reloadSettings();
    }

    private void reloadSettings() {
        settings = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settings)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.configmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSave:
                if (settings.save()) {
                    Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.toast_save_failed, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.itemResetToDefault:
                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit().clear().apply();
                reloadSettings();
                Toast.makeText(this, R.string.toast_reset_to_default, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemRevertChanges:
                reloadSettings();
                Toast.makeText(this, R.string.toast_revert_changes, Toast.LENGTH_SHORT).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
