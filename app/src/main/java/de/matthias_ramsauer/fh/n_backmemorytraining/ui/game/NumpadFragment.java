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

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import de.matthias_ramsauer.fh.n_backmemorytraining.GameActivity;
import de.matthias_ramsauer.fh.n_backmemorytraining.R;

public class NumpadFragment extends Fragment implements Animator.AnimatorListener {

    private GameViewModel mViewModel;

    @IdRes
    private final int[] numpadIds = new int[]{
            R.id.numpad_one,
            R.id.numpad_two,
            R.id.numpad_three,
            R.id.numpad_four,
            R.id.numpad_five,
            R.id.numpad_six,
            R.id.numpad_seven,
            R.id.numpad_eight,
            R.id.numpad_nine
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View out = inflater.inflate(R.layout.game_fragment_numberpad, container, false);
        for (int id : numpadIds) {
            out.findViewById(id).setOnClickListener(this::onNumpadInput);
        }
        return out;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity activity = getActivity();
        assert activity != null;

        mViewModel = ViewModelProviders.of(activity).get(GameViewModel.class);
    }

    private void onNumpadInput(View view) {
        final int pressed = Integer.parseInt((String) view.getTag());
        final int correct = this.mViewModel.answer(pressed);

        if (pressed == correct) {
            startAnimation(view, R.color.correct, this);
        } else {
            final View correctBtn = ((View) view.getParent()).findViewWithTag(String.valueOf(correct));
            startAnimation(correctBtn, R.color.correct_not_pressed, this);
            startAnimation(view, R.color.wrong, null);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        final View view = getView();
        assert view != null;
        for (int id : numpadIds) {
            view.findViewById(id).setEnabled(false);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        final GameActivity activity = (GameActivity) getActivity();
        if (activity == null) return;
        activity.onNextExpression();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        final GameActivity activity = (GameActivity) getActivity();
        if (activity == null) return;
        activity.onNextExpression();
    }

    @Override
    public void onAnimationRepeat(Animator animation) { }

    private void startAnimation(@NonNull View view, int toColor, @Nullable Animator.AnimatorListener cb) {
        final ValueAnimator animator = ObjectAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(R.color.neutral), getResources().getColor(toColor));
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
