package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        Week(R.id.stats_graph_week),
        Month(R.id.stats_graph_month),
        Year(R.id.stats_graph_year);

        @IdRes
        public final int id;

        TimeSpan(int id) {
            this.id = id;
        }
    }

    private TimeSpan timeSpan = TimeSpan.Week;
    private LineChart chart;

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
        final Locale locale = getResources().getConfiguration().locale;
        final int textColor = getResources().getColor(R.color.primaryTextColor);

        ((MaterialButton) root.findViewById(R.id.stats_graph_week)).addOnCheckedChangeListener(this::onTimeSpanChange);
        ((MaterialButton) root.findViewById(R.id.stats_graph_month)).addOnCheckedChangeListener(this::onTimeSpanChange);
        ((MaterialButton) root.findViewById(R.id.stats_graph_year)).addOnCheckedChangeListener(this::onTimeSpanChange);

        group.check(this.timeSpan.id);

        chart = root.findViewById(R.id.plot);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                final long time = (long) value;
                final Date date = new Date(time);

                switch (timeSpan) {
                    case Week:
                        return new SimpleDateFormat("EE", locale).format(date);
                    case Month:
                        return new SimpleDateFormat("W", locale).format(date);
                    case Year:
                        return new SimpleDateFormat("L", locale).format(date);
                    default:
                        throw new UnsupportedOperationException();
                }
            }
        });
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return Integer.toString((int)value);
            }
        });

        chart.getXAxis().setTextColor(textColor);
        chart.getAxisLeft().setTextColor(textColor);
        chart.setNoDataTextColor(textColor);

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
        DatabaseExecutor.getInstance().execute(() -> {
            final StatsDatabase db = StatsDatabase.getInstance(getActivity());
            final List<Stats> values;

            switch (timeSpan) {
                case Week:
                    values = db.statsDao().getWeeksState();
                    chart.getXAxis().setLabelCount(7);
                    break;
                case Month:
                    values = db.statsDao().getMonthsState();
                    chart.getXAxis().setLabelCount(5);
                    break;
                case Year:
                    values = db.statsDao().getYearsState();
                    chart.getXAxis().setLabelCount(12);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            if (values.isEmpty()) {
                return;
            }

            final List<Entry> dataPoints = new ArrayList<>(values.size());
            for (final Stats stat : values) {
                dataPoints.add(new Entry(stat.date.getTime(), stat.score));
            }

            final LineDataSet dataSet = new LineDataSet(dataPoints, "");
            dataSet.setLineWidth(2f);
            dataSet.setDrawValues(false);
            dataSet.setValueTextColor(getResources().getColor(R.color.primaryTextColor));

            final LineData data = new LineData(dataSet);
            data.setValueTextSize(10f);

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