package de.matthias_ramsauer.fh.n_backmemorytraining;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

import de.matthias_ramsauer.fh.n_backmemorytraining.model.Expression;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.ExpressionBuilder;
import de.matthias_ramsauer.fh.n_backmemorytraining.viewmodel.GameViewModel;

public class TimeGameActivity extends GameActivity {

    private CountDownTimer countDownTimer;

    @Override
    protected void onResume() {
        super.onResume();
        final TextView stateIndicator = findViewById(R.id.current_state);

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
                final Intent i = new Intent(TimeGameActivity.this, ScoreActivity.class);
                i.putExtra(ScoreActivity.INTENT_EXTRA_CORRECT, viewModel.correct);
                i.putExtra(ScoreActivity.INTENT_EXTRA_EXPRESSIONS_COUNT, viewModel.answeredExpressionCount);
                startActivity(i);
            }
        };
        countDownTimer.onTick(viewModel.remainingTime);
        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    public void onNextExpression() {
        final Expression expression = ExpressionBuilder.expression(GameViewModel.random);
        viewModel.expressions.add(expression);
        // viewModel.selected = -1;
        recreate();
    }
}
