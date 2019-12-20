package de.matthias_ramsauer.fh.n_backmemorytraining.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.preference.Preference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class SettingsViewModel extends ViewModel implements Serializable {

    private final Map<NBackPreferences.Settings, String> values = new HashMap<>();

    public boolean save(@NonNull Context context) {
        final boolean saved = NBackPreferences.setPreferences(context, values);
        if (saved) {
            values.clear();
        }
        return saved;
    }

    public boolean hasChanged() {
        return !values.isEmpty();
    }

    public Map<NBackPreferences.Settings, String> getValues() {
        return values;
    }

    public Preference.OnPreferenceChangeListener onChange(NBackPreferences.Settings setting, String matchRegex) {
        return (preference, newValue) -> {
            if (!((String)newValue).matches(matchRegex)) {
                return false;
            }
            values.put(setting, (String) newValue);
            return true;
        };
    }
}
