package de.matthias_ramsauer.fh.n_backmemorytraining.util;

import de.matthias_ramsauer.fh.n_backmemorytraining.model.Expression;

public class ExpressionBuilder {

    private static final char[] symbols = {'+', '-'};

    public static Expression expression(IntSupplier random) {
        final IntSupplier absRandom = () -> Math.abs(random.get()) % 9 + 1;
        final char symbol = symbols[absRandom.get() % symbols.length];
        switch (symbol) {
            case '+': {
                final int first = absRandom.get();
                final int second = (absRandom.get() % (10 - first));
                return new Expression(first, symbol, second, first + second);
            }
            case '-': {
                final int first = absRandom.get();
                final int second = absRandom.get() % first;
                return new Expression(first, symbol, second, first - second);
            }
            default:
                throw new UnsupportedOperationException("symbol not implemented");
        }
    }
}
