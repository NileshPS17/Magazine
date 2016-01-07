package com.cloudfoyo.magazine.extras;

import android.animation.Animator;
import android.view.View;

/**
 * Created by nilesh on 7/1/16.
 */
public class Utility {


    public static final int NO_RESULT = 1,
                            HIDE_PROGRESS = 2,
                            SHOW_PROGRESS = 3;
    /**
     *
     * @param one View
     * @param two View
     *            Slowly convert VIew one into View two
     */
    public static void crossFadeViews(final View one,final View two)
    {
        two.setAlpha(0f);
        two.setVisibility(View.VISIBLE);

        two.animate().alpha(1f).setDuration(200).setListener(null);

        one.animate().alpha(0f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                    one.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                    two.setVisibility(View.INVISIBLE);
                    two.setAlpha(1f); // Opaque
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    public static void fadeOutView(final View v)
    {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.animate().setDuration(200).alpha(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                    v.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
