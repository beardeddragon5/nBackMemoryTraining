package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.model.TimeSpan;
import de.matthias_ramsauer.fh.n_backmemorytraining.tasks.UpdateGraphTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatsGraphFragment extends Fragment {

    private final static String KEY_TIME_SPAN = "graph_time_span";

    private TimeSpan timeSpan = TimeSpan.Week;
    private BarChart chart;

    public StatsGraphFragment() {
        super(R.layout.fragment_stats_graph);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TIME_SPAN, timeSpan.ordinal());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            this.timeSpan = TimeSpan.values()[savedInstanceState.getInt(KEY_TIME_SPAN)];
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View root = super.onCreateView(inflater, container, savedInstanceState);

        // final View root = inflater.inflate(R.layout.fragment_stats_graph, container, false);
        final int textColor = getResources().getColor(R.color.primaryTextColor);

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
        chart.getAxisRight().setDrawGridLines(false);
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
    public void onStart() {
        super.onStart();

        final MaterialButtonToggleGroup group = getView().findViewById(R.id.stats_time_span_group);
        group.check(this.timeSpan.id);

        group.addOnButtonCheckedListener((group1, checkedId, isChecked) -> {
            if (!isChecked && group1.getCheckedButtonId() == View.NO_ID) {
                group1.check(checkedId);
                return;
            }
            if (!isChecked) {
                return;
            }

            final TimeSpan span = TimeSpan.getByResId(checkedId);
            assert span != null;
            timeSpan = span;
            updateView();
        });
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
}