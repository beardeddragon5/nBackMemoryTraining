package de.matthias_ramsauer.fh.n_backmemorytraining.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

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
}
