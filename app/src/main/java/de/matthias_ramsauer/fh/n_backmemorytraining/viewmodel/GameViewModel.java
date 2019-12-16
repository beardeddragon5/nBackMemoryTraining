package de.matthias_ramsauer.fh.n_backmemorytraining.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.matthias_ramsauer.fh.n_backmemorytraining.model.Expression;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.ExpressionBuilder;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.IntSupplier;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class GameViewModel extends ViewModel implements Serializable {

    public static final IntSupplier random = new Random()::nextInt;

    // Parameters loaded from preferences
    public int n = -1;
    public NBackPreferences.EndCondition endCondition;
    public int expressionLimit = -1;
    public long timeLimit = -1;

    // Game State
    public boolean initialized = false;
    public int correct;
    public int answeredExpressionCount = 0;
    public final List<Expression> expressions = new ArrayList<>(10);
    public long remainingTime;

    public void initialize(@NonNull Context context) {
        final Expression expression = ExpressionBuilder.expression(random);
        this.expressions.add(expression);
        this.n = NBackPreferences.getN(context);
        this.expressionLimit = NBackPreferences.getExpressionLimit(context);
        this.timeLimit = NBackPreferences.getTimeLimit(context);
        this.endCondition = NBackPreferences.getEndCondition(context);
        this.remainingTime = this.timeLimit;
        this.initialized = true;
    }

    @Nullable
    public Expression getExpression() {
        if (expressions.isEmpty()) {
            return null;
        }

        if (endCondition == NBackPreferences.EndCondition.expression && expressions.size() > expressionLimit) {
            return null;
        }

        return expressions.get(expressions.size() - 1);
    }

    @Nullable
    public Expression getNBackExpression() {
        if (expressions.size() <= n) {
            return null;
        }
        return expressions.get(expressions.size() - n - 1);
    }

    public boolean isGameDone() {
        switch(endCondition) {
            case expression:
                return expressionLimit == expressions.size() - n;
            case time:
                return remainingTime <= 0;
        }
        throw new IllegalStateException("endconditon unknown");
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInitialized() {
        return initialized;
    }

    public int answer(int selectedResult) {
        assert this.getNBackExpression() != null;
        final Expression expression = getNBackExpression();

        answeredExpressionCount++;
        if (expression.result == selectedResult) {
            correct++;
        }
        return expression.result;
    }

    public boolean showNumpad() {
        return this.n < this.expressions.size();
    }

    @Override
    @NonNull
    public String toString() {
        return "GameViewModel{" +
                "n=" + n +
                ", endCondition=" + endCondition +
                ", expressionLimit=" + expressionLimit +
                ", timeLimit='" + timeLimit + '\'' +
                ", correct=" + correct +
                ", expressions=" + expressions +
//                ", selected=" + selected +
                '}';
    }
}
