package de.matthias_ramsauer.fh.n_backmemorytraining.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public class Expression implements Serializable {

    public final int first;
    public final int second;
    public final char symbol;
    public final int result;

    public Expression(int first, char symbol, int second, int result) {
        this.first = first;
        this.symbol = symbol;
        this.second = second;
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return first == that.first &&
                second == that.second &&
                symbol == that.symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, symbol);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.GERMANY, "%d %c %d", first, symbol, second);
    }
}
