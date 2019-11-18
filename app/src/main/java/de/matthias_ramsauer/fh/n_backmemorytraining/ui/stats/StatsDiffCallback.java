package de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import de.matthias_ramsauer.fh.n_backmemorytraining.db.Stats;

class StatsDiffCallback extends DiffUtil.ItemCallback<Stats> {

    @Override
    public boolean areItemsTheSame(@NonNull Stats oldItem, @NonNull Stats newItem) {
        return oldItem.id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Stats oldItem, @NonNull Stats newItem) {
        return this.areItemsTheSame(oldItem, newItem);
    }
}