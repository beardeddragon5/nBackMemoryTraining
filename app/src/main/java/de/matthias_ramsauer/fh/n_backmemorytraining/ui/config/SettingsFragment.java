package de.matthias_ramsauer.fh.n_backmemorytraining.ui.config;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;

import androidx.preference.EditTextPreference;
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

        setPreferenceInputType(nKey, InputType.TYPE_CLASS_NUMBER);
        setPreferenceInputType(timeLimitKey, InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
        setPreferenceInputType(expressionLimitKey, InputType.TYPE_CLASS_NUMBER);

        findPreference(nKey).setOnPreferenceChangeListener((preference, newValue) -> {
            n = (String) newValue;
            return true;
        });

        findPreference(endConditionKey).setOnPreferenceChangeListener((preference, newValue) -> {
            endCondition = (String) newValue;
            return true;
        });

        findPreference(timeLimitKey).setOnPreferenceChangeListener((preference, newValue) -> {
            timeLimit = (String) newValue;
            return true;
        });

        findPreference(expressionLimitKey).setOnPreferenceChangeListener((preference, newValue) -> {
            expressionLimit = (String) newValue;
            return true;
        });

        findPreference(nKey).setPersistent(false);
        findPreference(endConditionKey).setPersistent(false);
        findPreference(timeLimitKey).setPersistent(false);
        findPreference(expressionLimitKey).setPersistent(false);
    }

    public boolean save() {
        return NBackPreferences.setPreferences(getActivity(), n, endCondition, timeLimit, expressionLimit);
    }


    public void setPreferenceInputType(final CharSequence key, final int inputType) {
        final EditTextPreference pref = findPreference(key);
        pref.setOnBindEditTextListener(editText -> editText.setInputType(inputType));
    }

}
