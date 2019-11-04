package de.matthias_ramsauer.fh.n_backmemorytraining.ui.game;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.matthias_ramsauer.fh.n_backmemorytraining.GameActivity;
import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public class NextFragment extends Fragment {

    private GameViewModel mViewModel;

    public NextFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View out = inflater.inflate(R.layout.game_fragment_next, container, false);
        out.findViewById(R.id.game_next).setOnClickListener(this::onNextClick);
        return out;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(GameViewModel.class);
    }

    public void onNextClick(View view) {
        final GameActivity activity = (GameActivity) getActivity();
        activity.onNextExpression();
    }
}
