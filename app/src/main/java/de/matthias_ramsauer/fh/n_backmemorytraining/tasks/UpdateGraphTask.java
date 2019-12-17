package de.matthias_ramsauer.fh.n_backmemorytraining.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.matthias_ramsauer.fh.n_backmemorytraining.db.StatsDatabase;
import de.matthias_ramsauer.fh.n_backmemorytraining.model.Stats;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats.StatsGraphFragment;

public class UpdateGraphTask extends AsyncTask<Context, Void, BarData> {

    private final List<String> labels = new ArrayList<>();
    private final StatsGraphFragment.TimeSpan timeSpan;
    private final BarChart chart;

    public UpdateGraphTask(BarChart chart, StatsGraphFragment.TimeSpan timeSpan) {
        this.chart = chart;
        this.timeSpan = timeSpan;
    }

    @Override
    protected BarData doInBackground(Context... contexts) {
        final Locale locale = contexts[0].getResources().getConfiguration().locale;
        final StatsDatabase db = StatsDatabase.getInstance(contexts[0]);
        final List<Stats> values;
        final Calendar cal = Calendar.getInstance(locale);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        switch (timeSpan) {
            case Week:
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                break;
            case Month:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case Year:
                cal.set(Calendar.DAY_OF_YEAR, 1);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        values = db.statsDao().getStatsSince(cal.getTime());
        if (values.isEmpty()) {
            return null;
        }


        switch (timeSpan) {
            case Week:
                for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_WEEK); i++) {
                    cal.set(Calendar.DAY_OF_WEEK, i);
                    labels.add(timeSpan.format(locale, cal.getTime()));
                }
                break;
            case Month:
                for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    cal.set(Calendar.DAY_OF_MONTH, i);
                    labels.add(timeSpan.format(locale, cal.getTime()));
                }
                break;
            case Year:
                for (int i = 0; i <= cal.getActualMaximum(Calendar.MONTH); i++) {
                    cal.set(Calendar.MONTH, i);
                    labels.add(timeSpan.format(locale, cal.getTime()));
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }


        final List<BarEntry> dataPoints = new ArrayList<>(values.size());
        final Map<String, Integer> groupScoreByLabel = new HashMap<>();

        for (final Stats stat : values) {
            final String label = timeSpan.format(locale, stat.date);
            final Integer currentScore = groupScoreByLabel.get(label);
            if (currentScore == null) {
                groupScoreByLabel.put(label, stat.score);
            } else if (currentScore < stat.score) {
                groupScoreByLabel.put(label, stat.score);
            }
        }

        for (int i = 0; i < labels.size(); i++) {
            final String label = labels.get(i);
            if (label != null && groupScoreByLabel.containsKey(label)) {
                //noinspection ConstantConditions
                dataPoints.add(new BarEntry(i, groupScoreByLabel.get(label)));
            }
        }

        final BarDataSet dataSet = new BarDataSet(dataPoints, "");
        dataSet.setDrawValues(false);

        final BarData data = new BarData(dataSet);

        return data;
    }

    @Override
    protected void onPostExecute(BarData data) {
        chart.getXAxis().setLabelCount(labels.size());
        chart.getXAxis().setAxisMinimum(0 - data.getBarWidth() / 2);
        chart.getXAxis().setAxisMaximum(labels.size() - 1 + data.getBarWidth() / 2);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                final int index = (int) value;
                if (index < 0 || index >= labels.size()) {
                    return "";
                }
                if (timeSpan == StatsGraphFragment.TimeSpan.Month && index % 2 == 1) {
                    return "";
                }
                return labels.get(index);
            }
        });

        chart.setData(data);
        chart.invalidate();
    }
}
