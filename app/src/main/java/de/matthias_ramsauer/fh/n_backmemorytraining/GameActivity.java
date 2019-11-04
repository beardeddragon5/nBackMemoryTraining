package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import java.util.Random;

import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.GameFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.GameViewModel;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.NextFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.game.NumpadFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.Expression;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.ExpressionBuilder;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.IntSupplier;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class GameActivity extends AppCompatActivity {

    public static IntSupplier random = new Random()::nextInt;

    private GameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        if (savedInstanceState == null) {
            viewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        } else {
            viewModel = (GameViewModel) savedInstanceState.getSerializable("view_model");
            if (viewModel == null) {
                viewModel = ViewModelProviders.of(this).get(GameViewModel.class);
            }
        }

        if (!viewModel.isInitalized()) {
            final Expression expression = ExpressionBuilder.expression(random);
            viewModel.expressions.add(expression);

            viewModel.n = NBackPreferences.getN(this);
            viewModel.expressionLimit = NBackPreferences.getExpressionLimit(this);
            viewModel.timeLimit = NBackPreferences.getTimeLimit(this);
            viewModel.endCondition = NBackPreferences.getEndCondition(this);
        }

        final TextView stateIndicator = findViewById(R.id.current_state);
        final TextView nIndicator = findViewById(R.id.game_n);
        stateIndicator.setText(String.format("%d / %d", viewModel.expressions.size(), viewModel.expressionLimit + viewModel.n));
        nIndicator.setText(String.format("n = %d", viewModel.n));

        getSupportFragmentManager().beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.game_screen, new GameFragment())
                .commitNow();

        final Fragment input;
        if (viewModel.n >= viewModel.expressions.size()) {
            input = new NextFragment();
        } else {
            input = new NumpadFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.game_input, input)
                .commitNow();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("view_model", viewModel);
    }

    public void onNextExpression() {
        switch (viewModel.endCondition) {
            case expression:
                if (viewModel.expressionLimit == viewModel.expressions.size() - viewModel.n) {
                    final Intent i = new Intent(this, ScoreActivity.class);
                    i.putExtra(ScoreActivity.INTENT_EXTRA_CORRECT, viewModel.correct);
                    i.putExtra(ScoreActivity.INTENT_EXTRA_EXPRESSIONS_COUNT, viewModel.expressionLimit);
                    startActivity(i);
                    return;
                }
                break;
            case time:
                throw new UnsupportedOperationException("endcondition not implemented");
            default:
                throw new UnsupportedOperationException("endcondition not implemented");
        }

        final Expression expression = ExpressionBuilder.expression(random);
        viewModel.expressions.add(expression);
        viewModel.selected = -1;
        recreate();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.game_back_dialog_title)
                .setCancelable(true)
                .setPositiveButton(R.string.game_back_dialog_yes, (dialog, which) -> super.onBackPressed())
                .setNegativeButton(R.string.game_back_dialog_no, (dialog, which) -> {})
                .create()
                .show();
    }
}
