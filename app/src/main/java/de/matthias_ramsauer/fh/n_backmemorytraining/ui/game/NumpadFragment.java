package de.matthias_ramsauer.fh.n_backmemorytraining.ui.game;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import de.matthias_ramsauer.fh.n_backmemorytraining.GameActivity;
import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public class NumpadFragment extends Fragment implements Animator.AnimatorListener {

    private GameViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View out = inflater.inflate(R.layout.game_fragment_numberpad, container, false);
        out.findViewById(R.id.numpad_one).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_two).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_three).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_four).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_five).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_six).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_seven).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_eight).setOnClickListener(this::onNumpadInput);
        out.findViewById(R.id.numpad_nine).setOnClickListener(this::onNumpadInput);
        return out;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(GameViewModel.class);

        if (this.mViewModel.selected != -1) {
            final View view = getView().findViewWithTag(String.valueOf(this.mViewModel.selected));
            this.mViewModel.selected = -1;
            onNumpadInput(view);
        }
    }

    public void onNumpadInput(View view) {
        if (this.mViewModel.selected != -1) {
            return;
        }
        final int pressed = Integer.parseInt((String) view.getTag());
        this.mViewModel.selected = pressed;

        assert this.mViewModel.getNBackExpression() != null;

        if (this.mViewModel.getNBackExpression().result == pressed) {
            startAnimation(view, R.color.neutral, R.color.correct, this);
            mViewModel.correct++;
        } else {
            final View correct = ((View) view.getParent()).findViewWithTag(String.valueOf(this.mViewModel.getNBackExpression().result));
            startAnimation(correct, R.color.neutral, R.color.correct_not_pressed, this);
            startAnimation(view, R.color.neutral, R.color.wrong, null);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        getView().findViewById(R.id.numpad_one).setEnabled(false);
        getView().findViewById(R.id.numpad_two).setEnabled(false);
        getView().findViewById(R.id.numpad_three).setEnabled(false);
        getView().findViewById(R.id.numpad_four).setEnabled(false);
        getView().findViewById(R.id.numpad_five).setEnabled(false);
        getView().findViewById(R.id.numpad_six).setEnabled(false);
        getView().findViewById(R.id.numpad_seven).setEnabled(false);
        getView().findViewById(R.id.numpad_eight).setEnabled(false);
        getView().findViewById(R.id.numpad_nine).setEnabled(false);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        final GameActivity activity = (GameActivity) getActivity();
        if (activity != null) {
            activity.onNextExpression();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        final GameActivity activity = (GameActivity) getActivity();
        if (activity != null) {
            activity.onNextExpression();
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    private void startAnimation(@NonNull View view, int fromColor, int toColor, @Nullable Animator.AnimatorListener cb) {
        final ValueAnimator animator = ObjectAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(fromColor), getResources().getColor(toColor));
        animator.setDuration(400L);
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            view.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
        });
        if (cb != null) {
            animator.addListener(cb);
        }
        animator.start();
    }
}
