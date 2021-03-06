package de.matthias_ramsauer.fh.n_backmemorytraining.ui.config;

import android.os.Bundle;
import android.text.InputType;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
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

        final EditTextPreference nPref = findPreference(NBackPreferences.Settings.N.key);
        final ListPreference endConditionPref = findPreference(NBackPreferences.Settings.EndCondition.key);
        final EditTextPreference timeLimitPref = findPreference(NBackPreferences.Settings.TimeLimit.key);
        final EditTextPreference expressionLimitPref = findPreference(NBackPreferences.Settings.ExpressionLimit.key);

        nPref.setPersistent(false);
        endConditionPref.setPersistent(false);
        timeLimitPref.setPersistent(false);
        expressionLimitPref.setPersistent(false);

        if (viewModel.hasChanged()) {
            if (viewModel.getValues().containsKey(NBackPreferences.Settings.N)) {
                nPref.setText(viewModel.getValues().get(NBackPreferences.Settings.N));
            }
            if (viewModel.getValues().containsKey(NBackPreferences.Settings.EndCondition)) {
                endConditionPref.setValue(viewModel.getValues().get(NBackPreferences.Settings.EndCondition));
            }
            if (viewModel.getValues().containsKey(NBackPreferences.Settings.TimeLimit)) {
                timeLimitPref.setText(viewModel.getValues().get(NBackPreferences.Settings.TimeLimit));
            }
            if (viewModel.getValues().containsKey(NBackPreferences.Settings.ExpressionLimit)) {
                expressionLimitPref.setText(viewModel.getValues().get(NBackPreferences.Settings.ExpressionLimit));
            }
        }

        assert nPref != null;
        assert endConditionPref != null;
        assert timeLimitPref != null;
        assert expressionLimitPref != null;

        nPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_n_summary)));
        endConditionPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_end_condition_summary)));
        timeLimitPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_time_limit_summary)));
        expressionLimitPref.setSummaryProvider(new FormatSummaryProvider(getResources().getString(R.string.pref_expression_limit_summary)));

        setPreferenceInputType(nPref, InputType.TYPE_CLASS_NUMBER);
        setPreferenceInputType(timeLimitPref, InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
        setPreferenceInputType(expressionLimitPref, InputType.TYPE_CLASS_NUMBER);

        nPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.N, "\\d{1,10}"));
        endConditionPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.EndCondition, "(expression)|(time)"));
        timeLimitPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.TimeLimit, "(\\d?\\d:)?(\\d?\\d:\\d\\d)"));
        expressionLimitPref.setOnPreferenceChangeListener(viewModel.onChange(NBackPreferences.Settings.ExpressionLimit, "\\d{1,10}"));
    }


    private void setPreferenceInputType(EditTextPreference pref, final int inputType) {
        pref.setOnBindEditTextListener(editText -> {
            editText.setInputType(inputType);
            editText.selectAll();
        });
    }

}
