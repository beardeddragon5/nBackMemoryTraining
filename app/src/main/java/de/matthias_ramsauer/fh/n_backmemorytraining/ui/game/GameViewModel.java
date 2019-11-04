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
    public final List<Expression> expressions = new ArrayList<>(10);
    public int selected = -1;
    public long remainingTime;

    @Nullable
    public Expression getExpression() {
        if (expressions.isEmpty()) {
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInitialized() {
        return expressions.size() != 0;
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
                ", selected=" + selected +
                '}';
    }
}
