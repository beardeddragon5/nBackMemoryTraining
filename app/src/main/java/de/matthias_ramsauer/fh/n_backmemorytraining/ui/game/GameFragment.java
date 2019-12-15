package de.matthias_ramsauer.fh.n_backmemorytraining.ui.game;

import androidx.fragment.app.FragmentActivity;
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
        final FragmentActivity activity = getActivity();

        assert activity != null;

        mViewModel = ViewModelProviders.of(activity).get(GameViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        assert mViewModel != null;
        if (!mViewModel.isInitialized()) {
            return;
        }

        final View view = getView();
        assert view != null;

        final TextView expressionView = view.findViewById(R.id.game_expression);
        final Expression expression = mViewModel.getExpression();

        expressionView.setText(expression != null ? expression.toString() : "");
    }
}
