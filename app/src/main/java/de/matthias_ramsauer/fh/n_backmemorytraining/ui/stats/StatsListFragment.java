package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.model.StatsSort;
import de.matthias_ramsauer.fh.n_backmemorytraining.tasks.UpdateListTask;

public class StatsListFragment extends Fragment {

    private static final String STATE_SORT_BY_DATE = "sort-by-date";
    private StatsSort sort = StatsSort.Score;
    private StatsListRecyclerViewAdapter listAdapter;

    public StatsListFragment() {
        super(R.layout.fragment_statslist_list);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_SORT_BY_DATE, sort.ordinal());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            sort = StatsSort.values()[savedInstanceState.getInt(STATE_SORT_BY_DATE)];
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Activity activity = getActivity();

        assert activity != null;

        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.list);
        final Spinner spinner = view.findViewById(R.id.stats_list_sort_by);

        listAdapter = new StatsListRecyclerViewAdapter(new StatsDiffCallback());

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(activity,
                R.array.stats_sortby_values, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        recyclerView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Spinner spinner = getView().findViewById(R.id.stats_list_sort_by);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = StatsSort.values()[position]; // equals SORT_DATE and SORT_SCORE
                updateList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinner.setSelection(sort.ordinal());
    }

    private void updateList() {
        final UpdateListTask task = new UpdateListTask(sort, stats -> stats.observe(this, currentStats -> {
            if (currentStats != null) {
                listAdapter.submitList(currentStats);
            }
        }));
        task.execute(getActivity());
    }
}
