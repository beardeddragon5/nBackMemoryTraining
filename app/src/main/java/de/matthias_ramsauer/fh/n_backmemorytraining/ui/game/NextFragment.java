package de.matthias_ramsauer.fh.n_backmemorytraining.ui.game;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.matthias_ramsauer.fh.n_backmemorytraining.GameActivity;
import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public class NextFragment extends Fragment {


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

    private void onNextClick(@SuppressWarnings("unused") View view) {
        final GameActivity activity = (GameActivity) getActivity();
        assert activity != null;
        activity.onNextExpression();
    }
}
