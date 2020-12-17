package com.cropview.anim;

public interface SimpleValueAnimatorListener {
    void onAnimationStarted();
    void onAnimationUpdated(float scale);
    void onAnimationFinished();
}
