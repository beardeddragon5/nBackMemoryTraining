package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.db.Stats;
import de.matthias_ramsauer.fh.n_backmemorytraining.db.StatsDatabase;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.DatabaseExecutor;

public class StatsListFragment extends Fragment {

    private static final String STATE_SORT_BY_DATE = "sort-by-date";
    private boolean sortByDate = true;
    private StatsListRecyclerViewAdapter listAdapter;

    public StatsListFragment() {
    }

    public static StatsListFragment newInstance() {
        return new StatsListFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_SORT_BY_DATE, sortByDate);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            sortByDate = savedInstanceState.getBoolean(STATE_SORT_BY_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_statslist_list, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.list);
        final Spinner spinner = view.findViewById(R.id.stats_list_sort_by);

        listAdapter = new StatsListRecyclerViewAdapter(new StatsDiffCallback());

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.stats_sortby_values, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && !sortByDate) {
                    sortByDate = true;
                    updateList();
                } else if (position == 1 && sortByDate) {
                    sortByDate = false;
                    updateList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        recyclerView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        DatabaseExecutor.getInstance().execute(() -> {
            final StatsDatabase db = StatsDatabase.getInstance(getActivity());
            final DataSource.Factory factory = sortByDate ?
                    db.statsDao().getStatsSortedByDate() :
                    db.statsDao().getStatsSortedByScore();

            final LiveData<PagedList<Stats>> stats = new LivePagedListBuilder(factory, 20).build();

            getActivity().runOnUiThread(() -> {
                stats.observe(this, currentStats -> {
                    if (currentStats != null) {
                        listAdapter.submitList(currentStats);
                    }
                });
            });
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
