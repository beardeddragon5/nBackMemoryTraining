package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.widget.TextView;

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

public class GameActivity extends AppCompatActivity {

    private static final IntSupplier random = new Random()::nextInt;

    private GameViewModel viewModel;
    private CountDownTimer countDownTimer;

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

        if (!viewModel.isInitialized()) {
            final Expression expression = ExpressionBuilder.expression(random);
            viewModel.expressions.add(expression);

            viewModel.n = NBackPreferences.getN(this);
            viewModel.expressionLimit = NBackPreferences.getExpressionLimit(this);
            viewModel.timeLimit = NBackPreferences.getTimeLimit(this);
            viewModel.endCondition = NBackPreferences.getEndCondition(this);
            viewModel.remainingTime = viewModel.timeLimit;
        }


        final TextView nIndicator = findViewById(R.id.game_n);

        if (viewModel.endCondition == NBackPreferences.EndCondition.expression) {
            final TextView stateIndicator = findViewById(R.id.current_state);
            stateIndicator.setText(String.format(Locale.GERMANY, "%d / %d", viewModel.expressions.size(), viewModel.expressionLimit + viewModel.n));

        }

        nIndicator.setText(String.format(Locale.GERMANY, "n = %d", viewModel.n));

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
    protected void onResume() {
        super.onResume();
        final TextView stateIndicator = findViewById(R.id.current_state);

        if (viewModel.endCondition == NBackPreferences.EndCondition.time) {
            countDownTimer = new CountDownTimer(viewModel.remainingTime, 10) {

                private long lastSeconds = -1;

                @Override
                public void onTick(long millisUntilFinished) {
                    viewModel.remainingTime = millisUntilFinished;

                    long seconds = millisUntilFinished / 1000;
                    if (lastSeconds == seconds) {
                        return;
                    }
                    lastSeconds = seconds;

                    long minutes = seconds / 60;
                    long hours = minutes / 60;

                    seconds %= 60;
                    minutes %= 60;

                    if (hours != 0L) {
                        stateIndicator.setText(String.format(Locale.GERMANY, "%02d:%02d:%02d", hours, minutes, seconds));
                    } else {
                        stateIndicator.setText(String.format(Locale.GERMANY, "%02d:%02d", minutes, seconds));
                    }
                }

                @Override
                public void onFinish() {
                    final Intent i = new Intent(GameActivity.this, ScoreActivity.class);
                    i.putExtra(ScoreActivity.INTENT_EXTRA_CORRECT, viewModel.correct);
                    i.putExtra(ScoreActivity.INTENT_EXTRA_EXPRESSIONS_COUNT, viewModel.answeredExpressionCount);
                    startActivity(i);
                }
            };
            countDownTimer.onTick(viewModel.remainingTime);
            countDownTimer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("view_model", viewModel);
    }

    public void onNextExpression() {
        if (viewModel.endCondition == NBackPreferences.EndCondition.expression &&
                viewModel.expressionLimit == viewModel.expressions.size() - viewModel.n) {
            final Intent i = new Intent(this, ScoreActivity.class);
            i.putExtra(ScoreActivity.INTENT_EXTRA_CORRECT, viewModel.correct);
            i.putExtra(ScoreActivity.INTENT_EXTRA_EXPRESSIONS_COUNT, viewModel.answeredExpressionCount);
            startActivity(i);
            return;
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
