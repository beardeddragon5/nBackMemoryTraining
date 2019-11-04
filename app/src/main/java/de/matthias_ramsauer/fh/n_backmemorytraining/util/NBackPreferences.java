package de.matthias_ramsauer.fh.n_backmemorytraining.util;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public class NBackPreferences {

    public enum EndCondition {
        expression,
        time
    };

    public static boolean setPreferences(@NonNull Context context, String n, String endCondition, String timeLimit, String expressionLimit) {
        final Resources res = context.getResources();
        final String nKey = res.getString(R.string.pref_n_key);
        final String endConditionKey = res.getString(R.string.pref_end_condition_key);
        final String timeLimitKey = res.getString(R.string.pref_time_limit_key);
        final String expressionLimitKey = res.getString(R.string.pref_expression_limit_key);

        return PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(nKey, n)
                .putString(endConditionKey, endCondition)
                .putString(timeLimitKey, timeLimit)
                .putString(expressionLimitKey, expressionLimit)
                .commit();
    }

    public static int getIntPreference(@NonNull Context context, int prefKeyID, String defValue) {
        return Integer.parseInt(getStringPreference(context, prefKeyID, defValue));
    }

    public static String getStringPreference(@NonNull Context context, int prefKeyID, String defValue) {
        final Resources res = context.getResources();
        final String key = res.getString(prefKeyID);
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defValue);
    }

    public static int getN(@NonNull Context context) {
        return getIntPreference(context, R.string.pref_n_key, "1");
    }

    public static EndCondition getEndCondition(@NonNull Context context) {
        return EndCondition.valueOf(getStringPreference(context, R.string.pref_end_condition_key, "expression"));
    }

    public static String getTimeLimit(@NonNull Context context) {
        return getStringPreference(context, R.string.pref_time_limit_key, "01:30");
    }

    public static int getExpressionLimit(@NonNull Context context) {
        return getIntPreference(context, R.string.pref_expression_limit_key, "10");
    }

}
