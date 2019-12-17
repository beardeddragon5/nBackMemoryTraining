package de.matthias_ramsauer.fh.n_backmemorytraining.ui.config;

import android.os.Bundle;
import android.text.InputType;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;
import de.matthias_ramsauer.fh.n_backmemorytraining.viewmodel.SettingsViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        final FragmentActivity activity = getActivity();

        assert activity != null;

        final SettingsViewModel viewModel = ViewModelProviders.of(activity).get(SettingsViewModel.class);
        final Preference nPref = findPreference(NBackPreferences.Settings.N.key);
        final Preference endConditionPref = findPreference(NBackPreferences.Settings.EndCondition.key);
        final Preference timeLimitPref = findPreference(NBackPreferences.Settings.TimeLimit.key);
        final Preference expressionLimitPref = findPreference(NBackPreferences.Settings.ExpressionLimit.key);

        assert nPref != null;
        assert endConditionPref != null;
        assert timeLimitPref != null;
        assert expressionLimitPref != null;

        nPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_n_summary)));
        endConditionPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_end_condition_summary)));
        timeLimitPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_time_limit_summary)));
        expressionLimitPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_expression_limit_summary)));

        setPreferenceInputType((EditTextPreference) nPref, InputType.TYPE_CLASS_NUMBER);
        setPreferenceInputType((EditTextPreference) timeLimitPref, InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
        setPreferenceInputType((EditTextPreference) expressionLimitPref, InputType.TYPE_CLASS_NUMBER);

        nPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.N, "\\d{1,10}"));
        endConditionPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.EndCondition, "(expression)|(time)"));
        timeLimitPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.TimeLimit, "(\\d?\\d:)?(\\d?\\d:\\d\\d)"));
        expressionLimitPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.ExpressionLimit, "\\d{1,10}"));

        nPref.setPersistent(false);
        endConditionPref.setPersistent(false);
        timeLimitPref.setPersistent(false);
        expressionLimitPref.setPersistent(false);
    }


    private void setPreferenceInputType(EditTextPreference pref, final int inputType) {
        pref.setOnBindEditTextListener(editText -> {
            editText.setInputType(inputType);
            editText.selectAll();
        });
    }

}
