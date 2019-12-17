package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.model.Stats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class StatsListRecyclerViewAdapter extends PagedListAdapter<Stats, StatsListRecyclerViewAdapter.ViewHolder> {

    private DateFormat dateFormatter;

    StatsListRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<Stats> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (dateFormatter == null) {
            dateFormatter = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT,
                    parent.getResources().getConfiguration().locale);
        }
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_statslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Stats stats = getItem(position);
        if (stats == null) {
            holder.clear();
        } else {
            holder.bind(stats);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Stats item;
        final TextView date;
        final TextView n;
        final TextView score;

        ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.list_item_date);
            n = view.findViewById(R.id.list_item_n);
            score = view.findViewById(R.id.list_item_score);
        }

        void bind(Stats item) {
            this.item = item;
            this.date.setText(dateFormatter.format(this.item.date));
            this.n.setText(String.valueOf(this.item.n));
            this.score.setText(String.valueOf(this.item.score));
        }

        void clear() {
            this.item = null;
            this.date.setText(null);
            this.n.setText(null);
            this.score.setText(null);
        }
    }
}
