package com.demo.kotlin.view.widget.explosion

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.animation.addListener
import java.util.*
import kotlin.collections.ArrayList


class ExplosionField : View {
    private val mExplosions: MutableList<ExplosionAnimator> = ArrayList()
    private val mExpandInset = IntArray(2)
    private var mListener: onAnimEndListener? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        Arrays.fill(mExpandInset, Tools.dp2Px(32))
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (explosion in mExplosions) {
            explosion.draw(canvas)
        }
    }

    fun expandExplosionBound(dx: Int, dy: Int) {
        mExpandInset[0] = dx
        mExpandInset[1] = dy
    }

    fun explode(bitmap: Bitmap?, bound: Rect?, startDelay: Long, duration: Long) {
        val explosion = ExplosionAnimator(this, bitmap!!, bound)
        explosion.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mExplosions.remove(animation)
            }
        })
        explosion.startDelay = startDelay
        explosion.duration = duration
        mExplosions.add(explosion)
        explosion.start()
    }

    fun explode(view: View) {
        view.isClickable = false
        val r = Rect()
        val x = view.x
        val y = view.y
        view.getGlobalVisibleRect(r)
        val location = IntArray(2)
        getLocationOnScreen(location)
        r.offset(-location[0], -location[1])
        r.inset(-mExpandInset[0], -mExpandInset[1])
        val startDelay = 100L
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150)
        animator.addUpdateListener(object : AnimatorUpdateListener {
            var random: Random = Random()
            override fun onAnimationUpdate(animation: ValueAnimator) {
                view.translationX = (random.nextFloat() - 0.5f) * view.width * 0.05f
                view.translationY = (random.nextFloat() - 0.5f) * view.height * 0.05f
            }
        })
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mListener?.onAnimEnd()
                view.x = x
                view.y = y
                view.postDelayed({
                    view.animate().setDuration(500).setStartDelay(startDelay * 5).scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .start()
                }, 500)
                view.postDelayed({
                    view.isClickable = true
                }, 1000)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        animator.start()
        view.animate().setDuration(150).setStartDelay(startDelay).scaleX(0f).scaleY(0f)
            .alpha(0f)
            .start()
        explode(
            Tools.createBitmapFromView(view),
            r,
            startDelay.toLong(),
            ExplosionAnimator.DEFAULT_DURATION
        )
    }

    fun clear() {
        mExplosions.clear()
        invalidate()
    }

    interface onAnimEndListener {
        fun onAnimEnd()
    }

    fun setOnAnimEndListener(listener: onAnimEndListener) {
        this.mListener = listener
    }

    companion object {
        fun attach2Window(activity: Activity): ExplosionField {
            val rootView = activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
            val explosionField = ExplosionField(activity)
            rootView.addView(
                explosionField, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            return explosionField
        }
    }
}