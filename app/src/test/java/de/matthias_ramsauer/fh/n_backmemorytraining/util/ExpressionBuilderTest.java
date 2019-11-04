package de.matthias_ramsauer.fh.n_backmemorytraining.util;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ExpressionBuilderTest {

    @Test
    public void expression_random() {
        final Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            final Expression actual = ExpressionBuilder.expression(rand::nextInt);
            assertTrue(actual.toString(), actual.result < 10);
            assertTrue(actual.toString(), actual.result > 0);
        }
    }
}
