package de.matthias_ramsauer.fh.n_backmemorytraining.ui.config;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class SettingsFragment extends PreferenceFragmentCompat {

    private String n;
    private String endCondition;
    private String timeLimit;
    private String expressionLimit;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        final Resources res = getResources();
        final String nKey = res.getString(R.string.pref_n_key);
        final String endConditionKey = res.getString(R.string.pref_end_condition_key);
        final String timeLimitKey = res.getString(R.string.pref_time_limit_key);
        final String expressionLimitKey = res.getString(R.string.pref_expression_limit_key);

        final Preference nPref = findPreference(nKey);
        final Preference endConditionPref = findPreference(endConditionKey);
        final Preference timeLimitPref = findPreference(timeLimitKey);
        final Preference expressionLimitPref = findPreference(expressionLimitKey);

        assert nPref != null;
        assert endConditionPref != null;
        assert timeLimitPref != null;
        assert expressionLimitPref != null;

        setPreferenceInputType((EditTextPreference) nPref, InputType.TYPE_CLASS_NUMBER);
        setPreferenceInputType((EditTextPreference) timeLimitPref, InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
        setPreferenceInputType((EditTextPreference) expressionLimitPref, InputType.TYPE_CLASS_NUMBER);

        nPref.setOnPreferenceChangeListener((preference, newValue) -> {
            n = (String) newValue;
            return true;
        });

        endConditionPref.setOnPreferenceChangeListener((preference, newValue) -> {
            endCondition = (String) newValue;
            return true;
        });

        timeLimitPref.setOnPreferenceChangeListener((preference, newValue) -> {
            timeLimit = (String) newValue;
            return true;
        });

        expressionLimitPref.setOnPreferenceChangeListener((preference, newValue) -> {
            expressionLimit = (String) newValue;
            return true;
        });

        nPref.setPersistent(false);
        endConditionPref.setPersistent(false);
        timeLimitPref.setPersistent(false);
        expressionLimitPref.setPersistent(false);
    }

    public boolean save() {
        final FragmentActivity activity = getActivity();
        assert activity != null;

        return NBackPreferences.setPreferences(activity, n, endCondition, timeLimit, expressionLimit);
    }


    private void setPreferenceInputType(EditTextPreference pref, final int inputType) {
        pref.setOnBindEditTextListener(editText -> editText.setInputType(inputType));
    }

}
