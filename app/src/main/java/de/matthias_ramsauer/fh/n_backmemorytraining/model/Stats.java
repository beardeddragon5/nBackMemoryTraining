package de.matthias_ramsauer.fh.n_backmemorytraining.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@SuppressWarnings("WeakerAccess")
@Entity
public class Stats {

    @PrimaryKey
    public long id;

    public int n;

    @ColumnInfo(name = "expression_count")
    public int expressionCount;

    @ColumnInfo(index = true)
    public int score;

    @ColumnInfo(index = true)
    public Date date;

    @NonNull
    @Override
    public String toString() {
        return "Stats{" +
                "id=" + id +
                ", n=" + n +
                ", expressionCount=" + expressionCount +
                ", score=" + score +
                ", date=" + date +
                '}';
    }
}