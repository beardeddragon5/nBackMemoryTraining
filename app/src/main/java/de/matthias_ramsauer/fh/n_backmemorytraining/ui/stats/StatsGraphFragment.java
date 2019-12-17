package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.tasks.UpdateGraphTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatsGraphFragment extends Fragment {

    private final static String KEY_TIME_SPAN = "graph_time_span";

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

    private void updateView() {
        final UpdateGraphTask task = new UpdateGraphTask(chart, timeSpan);
        task.execute(getActivity());
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