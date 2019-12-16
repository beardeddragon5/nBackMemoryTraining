package de.matthias_ramsauer.fh.n_backmemorytraining.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import de.matthias_ramsauer.fh.n_backmemorytraining.model.Stats;


@Dao
public interface StatsDao {

    @Query("SELECT * FROM stats ORDER BY date DESC")
    DataSource.Factory<Integer, Stats> getStatsSortedByDate();

    @Query("SELECT * FROM stats ORDER BY score DESC")
    DataSource.Factory<Integer, Stats> getStatsSortedByScore();

    @Query("SELECT * FROM stats WHERE stats.date >= :date")
    List<Stats> getStatsSince(Date date);

    @Query("INSERT INTO stats (n, expression_count, score, date) VALUES(:n, :expressionCount, :score, :date)")
    void addStats(int n, int expressionCount, int score, Date date);

    @Query("SELECT MAX(score) FROM stats WHERE stats.date > :date")
    int getBestScoreSince(Date date);

    @Query("SELECT MAX(score) FROM stats")
    int getBestScore();

    @Delete
    void delete(Stats stats);
}
