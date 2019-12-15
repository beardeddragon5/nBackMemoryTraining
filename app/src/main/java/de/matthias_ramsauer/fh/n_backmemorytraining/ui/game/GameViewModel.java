package de.matthias_ramsauer.fh.n_backmemorytraining.ui.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.matthias_ramsauer.fh.n_backmemorytraining.util.Expression;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class GameViewModel extends ViewModel implements Serializable {

    // Parameters loaded from preferences
    public int n = -1;
    public NBackPreferences.EndCondition endCondition;
    public int expressionLimit = -1;
    public long timeLimit = -1;

    // Game State
    public int correct;
    public int answeredExpressionCount = 0;
    public final List<Expression> expressions = new ArrayList<>(10);
    public long remainingTime;

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
        return expressions.size() != 0;
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
