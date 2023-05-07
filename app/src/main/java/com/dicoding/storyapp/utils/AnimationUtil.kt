package com.dicoding.storyapp.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

class AnimationUtil {
    companion object {
        private const val START_DELAY = 200L
        private const val ANIMATION_TIME = 800L

        fun playAnimation(vararg views: View) {
            val animation = mutableListOf<Animator>()
            views.forEach { view ->
                animation.add(ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(ANIMATION_TIME))
            }
            AnimatorSet().apply {
                playSequentially(animation)
                startDelay = START_DELAY
                start()
            }
        }
    }
}