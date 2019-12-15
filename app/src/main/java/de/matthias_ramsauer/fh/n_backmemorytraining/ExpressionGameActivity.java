package de.matthias_ramsauer.fh.n_backmemorytraining;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;
import java.util.Random;

import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.GameFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.GameViewModel;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.NextFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.NumpadFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.Expression;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.ExpressionBuilder;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.IntSupplier;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

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
            return;
        }

        final Expression expression = ExpressionBuilder.expression(random);
        viewModel.expressions.add(expression);
        recreate();
    }
}
