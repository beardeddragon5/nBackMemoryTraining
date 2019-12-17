package de.matthias_ramsauer.fh.n_backmemorytraining;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

import de.matthias_ramsauer.fh.n_backmemorytraining.model.Expression;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.ExpressionBuilder;
import de.matthias_ramsauer.fh.n_backmemorytraining.viewmodel.GameViewModel;

public class ExpressionGameActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView stateIndicator = findViewById(R.id.current_state);
        stateIndicator.setText(String.format(Locale.GERMANY, "%d / %d", viewModel.expressions.size(), viewModel.expressionLimit + viewModel.n));
    }

    public void onNextExpression() {
        if (viewModel.isGameDone()) {
            final Intent i = new Intent(this, ScoreActivity.class);
            i.putExtra(ScoreActivity.INTENT_EXTRA_CORRECT, viewModel.correct);
            i.putExtra(ScoreActivity.INTENT_EXTRA_EXPRESSIONS_COUNT, viewModel.answeredExpressionCount);
            startActivity(i);
            this.finishAndRemoveTask();
            return;
        }

        final Expression expression = ExpressionBuilder.expression(GameViewModel.random);
        viewModel.expressions.add(expression);
        recreate();
    }
}
