package de.matthias_ramsauer.fh.n_backmemorytraining.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public class NBackPreferences {

    public enum Settings {
        N("n"),
        EndCondition("end_condition"),
        TimeLimit("time_limit"),
        ExpressionLimit("expression_limit");

        public final String key;

        Settings(String key) {
            this.key = key;
        }
    }

    public enum EndCondition {
        expression,
        time
    }

    public static boolean setPreferences(@NonNull Context context, Map<Settings, String> values) {
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        for (Settings setting : Settings.values()) {
            if (values.containsKey(setting)) {
                editor.putString(setting.key, values.get(setting));
            }
        }

        return editor.commit();
    }

    private static int getIntPreference(@NonNull Context context, String key, String defValue) {
        return Integer.parseInt(getStringPreference(context, key, defValue), 10);
    }

    private static String getStringPreference(@NonNull Context context, String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defValue);
    }

    public static int getN(@NonNull Context context) {
        return getIntPreference(context, Settings.N.key, "1");
    }

    public static EndCondition getEndCondition(@NonNull Context context) {
        return EndCondition.valueOf(getStringPreference(context, Settings.EndCondition.key, "expression"));
    }

    public static long getTimeLimit(@NonNull Context context) {
        final String timeLimit = getStringPreference(context, Settings.TimeLimit.key, "01:30");
        final String[] timeParts = timeLimit.split(":", 3);
        long result = 0;
        if (timeParts.length == 3) {
            result += Long.parseLong(timeParts[0], 10) * (1000 * 60 * 60);
            result += Long.parseLong(timeParts[1], 10) * (1000 * 60);
            result += Long.parseLong(timeParts[2], 10) * (1000);
        }
        if (timeParts.length == 2) {
            result += Long.parseLong(timeParts[0], 10) * (1000 * 60);
            result += Long.parseLong(timeParts[1], 10) * (1000);
        }
        return result;
    }

    public static int getExpressionLimit(@NonNull Context context) {
        return getIntPreference(context, Settings.ExpressionLimit.key, "10");
    }

}
