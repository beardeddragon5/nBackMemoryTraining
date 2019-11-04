package de.matthias_ramsauer.fh.n_backmemorytraining.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.Arrays;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public class NBackPreferences {

    public enum EndCondition {
        expression,
        time
    }

    public static boolean setPreferences(@NonNull Context context, String n, String endCondition, String timeLimit, String expressionLimit) {
        final Resources res = context.getResources();
        final String nKey = res.getString(R.string.pref_n_key);
        final String endConditionKey = res.getString(R.string.pref_end_condition_key);
        final String timeLimitKey = res.getString(R.string.pref_time_limit_key);
        final String expressionLimitKey = res.getString(R.string.pref_expression_limit_key);

        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (n != null) {
            editor.putString(nKey, n);
        }
        if (endCondition != null) {
            editor.putString(endConditionKey, endCondition);
        }
        if (timeLimit != null) {
            editor.putString(timeLimitKey, timeLimit);
        }
        if (expressionLimit != null) {
            editor.putString(expressionLimitKey, expressionLimit);
        }
        return editor.commit();
    }

    private static int getIntPreference(@NonNull Context context, int prefKeyID, String defValue) {
        return Integer.parseInt(getStringPreference(context, prefKeyID, defValue));
    }

    private static String getStringPreference(@NonNull Context context, int prefKeyID, String defValue) {
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

    public static long getTimeLimit(@NonNull Context context) {
        final String timeLimit = getStringPreference(context, R.string.pref_time_limit_key, "01:30");
        final String[] timeParts = timeLimit.split(":", 3);
        long result = 0;
        if (timeParts.length == 3) {
            result += Long.parseLong(timeParts[0]) * (1000 * 60 * 60);
            result += Long.parseLong(timeParts[1]) * (1000 * 60);
            result += Long.parseLong(timeParts[2]) * (1000);
        }
        if (timeParts.length == 2) {
            result += Long.parseLong(timeParts[0]) * (1000 * 60);
            result += Long.parseLong(timeParts[1]) * (1000);
        }
        return result;
    }

    public static int getExpressionLimit(@NonNull Context context) {
        return getIntPreference(context, R.string.pref_expression_limit_key, "10");
    }

}
