package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.db.Stats;
import de.matthias_ramsauer.fh.n_backmemorytraining.db.StatsDatabase;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.DatabaseExecutor;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatsGraphFragment extends Fragment {

    private final static String KEY_TIME_SPAN = "graph_time_span";

    enum TimeSpan {
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
    }

    private TimeSpan timeSpan = TimeSpan.Week;
    private BarChart chart;

    public static StatsGraphFragment newInstance() {
        return new StatsGraphFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.timeSpan = TimeSpan.values()[savedInstanceState.getInt(KEY_TIME_SPAN)];
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TIME_SPAN, timeSpan.ordinal());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_stats_graph, container, false);
        final MaterialButtonToggleGroup group = root.findViewById(R.id.stats_time_span_group);
        final int textColor = getResources().getColor(R.color.primaryTextColor);

        ((MaterialButton) root.findViewById(R.id.stats_graph_week)).addOnCheckedChangeListener(this::onTimeSpanChange);
        ((MaterialButton) root.findViewById(R.id.stats_graph_month)).addOnCheckedChangeListener(this::onTimeSpanChange);
        ((MaterialButton) root.findViewById(R.id.stats_graph_year)).addOnCheckedChangeListener(this::onTimeSpanChange);

        group.check(this.timeSpan.id);

        chart = root.findViewById(R.id.plot);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.getXAxis().setTextColor(textColor);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setGranularityEnabled(true);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return Integer.toString((int)value);
            }
        });
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setTextColor(textColor);
        chart.setNoDataTextColor(textColor);
        chart.setFitBars(true);
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.setNoDataText(getResources().getString(R.string.stats_graph_no_data));
        chart.getLegend().setEnabled(false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    public void updateView() {
        final Locale locale = getResources().getConfiguration().locale;

        DatabaseExecutor.getInstance().execute(() -> {
            final StatsDatabase db = StatsDatabase.getInstance(getActivity());
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
                return;
            }

            final List<String> labels = new ArrayList<>();
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
                } else if (currentScore.intValue() < stat.score) {
                    groupScoreByLabel.put(label, stat.score);
                }
            }

            for (int i = 0; i < labels.size(); i++) {
                final String label = labels.get(i);
                if (groupScoreByLabel.containsKey(label)) {
                    dataPoints.add(new BarEntry(i, groupScoreByLabel.get(label)));
                }
            }

            final BarDataSet dataSet = new BarDataSet(dataPoints, "");
            dataSet.setDrawValues(false);

            final BarData data = new BarData(dataSet);
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
                    if (timeSpan == TimeSpan.Month && index % 2 == 1) {
                        return "";
                    }
                    return labels.get(index);
                }
            });

            getActivity().runOnUiThread(() -> {
                chart.setData(data);

                chart.invalidate();
            });
        });
    }

    private void onTimeSpanChange(MaterialButton button, boolean isChecked) {
        final TimeSpan buttonTimeSpan = TimeSpan.values()[Integer.parseInt((String) button.getTag(), 10)];
        if (!isChecked && buttonTimeSpan == this.timeSpan) {
            button.setChecked(true);
        } else if (isChecked) {
            this.timeSpan = buttonTimeSpan;
            updateView();
        }
    }
}