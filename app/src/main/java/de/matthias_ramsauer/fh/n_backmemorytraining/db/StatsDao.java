package de.matthias_ramsauer.fh.n_backmemorytraining.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StatsDao {

    @Query("SELECT * FROM stats")
    List<Stats> getAll();

    @Query("INSERT INTO stats (n, expression_count, score) VALUES(:n, :expressionCount, :score)")
    void addStats(int n, int expressionCount, int score);

    @Query("SELECT MAX(score) FROM stats WHERE date > date('now', 'start of day')")
    int getTodaysBestScore();

    @Query("SELECT MAX(score) FROM stats")
    int getBestScore();

    @Delete
    void delete(Stats stats);
}
