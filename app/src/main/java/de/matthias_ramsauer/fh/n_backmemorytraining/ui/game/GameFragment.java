package de.matthias_ramsauer.fh.n_backmemorytraining.ui.game;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.matthias_ramsauer.fh.n_backmemorytraining.R;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.Expression;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.NBackPreferences;

public class GameFragment extends Fragment {

    private GameViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(GameViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        final TextView expressionView = ((TextView)getView().findViewById(R.id.game_expression));

        if (mViewModel.endCondition == NBackPreferences.EndCondition.expression && mViewModel.expressions.size() > mViewModel.expressionLimit) {
            expressionView.setText("");
            return;
        }

        final Expression expression = mViewModel.getExpression();
        if (expression != null) {
            expressionView.setText(expression.toString());
        }
    }
}
