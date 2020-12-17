package com.cropview.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Create by JFZ
 * date: 2020-06-09 10:03
 **/
public class AnimalHelp {

    /**
     * 文件夹列表的弹出动画
     *
     * @param view
     * @param visible
     */
    public static void folderAnims(final View view, final View lv, final View jiantou, final int visible) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            lv.post(new Runnable() {
                @Override
                public void run() {
                    float[] vals = new float[2];
                    ObjectAnimator alpha = null;
                    ObjectAnimator rotation = null;
                    if (visible == View.VISIBLE) {
                        view.setVisibility(visible);
                        vals[0] = -view.getHeight();
                        vals[1] = 0;
                        alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
                        rotation = ObjectAnimator.ofFloat(jiantou, "rotation", 0, 180);
                    } else {
                        vals[0] = 0;
                        vals[1] = -view.getHeight();
                        alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
                        rotation = ObjectAnimator.ofFloat(jiantou, "rotation", 180, 360);
                    }
                    ObjectAnimator objAnim = ObjectAnimator.ofFloat(lv, "translationY", vals);
                    objAnim.setDuration(300L);
                    objAnim.setInterpolator(new LinearInterpolator());

                    alpha.setDuration(300L);
                    alpha.setInterpolator(new LinearInterpolator());

                    rotation.setDuration(250L);
                    rotation.setInterpolator(new LinearInterpolator());

                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(alpha, objAnim, rotation);
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(visible);
                        }
                    });
                    set.start();
                }
            });
        } else {
            view.setVisibility(visible);
            if (visible == View.VISIBLE) {
                jiantou.setRotation(180);
            } else {
                jiantou.setRotation(0);
            }
        }
    }

    public static void layerAnim(final View view, final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ObjectAnimator alpha;
            if (show) {
                view.setVisibility(View.VISIBLE);
                alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
            } else {
                alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0);
            }
            alpha.setDuration(250L);
            alpha.setInterpolator(new LinearInterpolator());
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            alpha.start();
        } else {
            view.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
