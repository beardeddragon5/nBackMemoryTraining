package de.matthias_ramsauer.fh.n_backmemorytraining.ui.config;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

public class FormatSummaryProvider<T extends Preference> implements Preference.SummaryProvider<T> {

    private final String format;

    public FormatSummaryProvider(final String format) {
        this.format = format;
    }

    @Override
    public CharSequence provideSummary(T preference) {
        final String value;

        if (preference instanceof EditTextPreference) {
            value = ((EditTextPreference) preference).getText();
        } else if (preference instanceof ListPreference) {
            value = ((ListPreference) preference).getEntry().toString();
        } else {
            throw new IllegalArgumentException("preference class " + preference.getClass() + " not supported");
        }

        return this.format.replace("{}", value);
    }
}
