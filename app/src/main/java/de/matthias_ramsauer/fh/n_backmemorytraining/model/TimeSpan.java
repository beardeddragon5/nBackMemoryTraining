package de.matthias_ramsauer.fh.n_backmemorytraining.model;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public enum TimeSpan {
    Week(R.id.stats_graph_week, "EEE"),
    Month(R.id.stats_graph_month, "d"),
    Year(R.id.stats_graph_year, "LLL");

    @IdRes
    public final int id;
    public final String dateFormatString;

    TimeSpan(int id, String dateFormatString) {
        this.id = id;
        this.dateFormatString = dateFormatString;
    }

    public String format(Locale local, Date date) {
        return new SimpleDateFormat(this.dateFormatString, local).format(date);
    }

    public static @Nullable TimeSpan getByResId(@IdRes int id) {
        for (TimeSpan span : TimeSpan.values()) {
            if (span.id == id) {
                return span;
            }
        }
        return null;
    }
}
