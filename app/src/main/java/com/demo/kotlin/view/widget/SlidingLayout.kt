package com.demo.kotlin.view.widget

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Scroller
import com.demo.kotlin.R

/**
侧滑退出
 */
class SlidingLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var mActivity: Activity? = null
    private var mScroller: Scroller? = null

    // 页面边缘的阴影图
    private var mLeftShadow: Drawable? = null

    // 页面边缘阴影的宽度
    private var mShadowWidth = 0
    private var mInterceptDownX = 0
    private var mLastInterceptX = 0
    private var mLastInterceptY = 0
    private var mTouchDownX = 0
    private var mLastTouchX = 0
    private var mLastTouchY = 0
    private var isConsumed = false

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    private fun initView(context: Context?) {
        mScroller = Scroller(context)
        mLeftShadow = resources.getDrawable(R.drawable.left_shadow)
        val density = resources.displayMetrics.density.toInt()
        mShadowWidth = SHADOW_WIDTH * density
    }

    /**
     * 绑定Activity
     */
    fun bindActivity(activity: Activity?) {
        mActivity = activity
        val decorView = mActivity!!.window.decorView as ViewGroup
        val child: View = decorView.getChildAt(0)
        decorView.removeView(child)
        addView(child)
        decorView.addView(this)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = false
                mInterceptDownX = x
                mLastInterceptX = x
                mLastInterceptY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastInterceptX
                val deltaY = y - mLastInterceptY
                // 手指处于屏幕边缘，且横向滑动距离大于纵向滑动距离时，拦截事件
                intercept =
                    if (mInterceptDownX < width / 10 && Math.abs(deltaX) > Math.abs(deltaY)) {
                        true
                    } else {
                        false
                    }
                mLastInterceptX = x
                mLastInterceptY = y
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
                run {
                    mLastInterceptY = 0
                    mLastInterceptX = mLastInterceptY
                    mInterceptDownX = mLastInterceptX
                }
            }
        }
        return intercept
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchDownX = x
                mLastTouchX = x
                mLastTouchY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastTouchX
                val deltaY = y - mLastTouchY
                if (!isConsumed && mTouchDownX < width / 10 && Math.abs(deltaX) > Math.abs(deltaY)) {
                    isConsumed = true
                }
                if (isConsumed) {
                    val rightMovedX = mLastTouchX - ev.x.toInt()
                    // 左侧即将滑出屏幕
                    if (scrollX + rightMovedX >= 0) {
                        scrollTo(0, 0)
                    } else {
                        scrollBy(rightMovedX, 0)
                    }
                }
                mLastTouchX = x
                mLastTouchY = y
            }
            MotionEvent.ACTION_UP -> {
                isConsumed = false
                run {
                    mLastTouchY = 0
                    mLastTouchX = mLastTouchY
                    mTouchDownX = mLastTouchX
                }
                // 根据手指释放时的位置决定回弹还是关闭
                if (-scrollX < width / 2) {
                    scrollBack()
                } else {
                    scrollClose()
                }
            }
        }
        return true
    }

    /**
     * 滑动返回
     */
    private fun scrollBack() {
        val startX = scrollX
        val dx = -scrollX
        mScroller!!.startScroll(startX, 0, dx, 0, 300)
        invalidate()
    }

    /**
     * 滑动关闭
     */
    private fun scrollClose() {
        val startX = scrollX
        val dx = -scrollX - width
        mScroller!!.startScroll(startX, 0, dx, 0, 300)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller!!.currX, 0)
            postInvalidate()
        } else if (-scrollX >= width) {
            mActivity!!.finish()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawShadow(canvas)
    }

    /**
     * 绘制边缘的阴影
     */
    private fun drawShadow(canvas: Canvas) {
        mLeftShadow!!.setBounds(0, 0, mShadowWidth, height)
        canvas.save()
        canvas.translate(-mShadowWidth.toFloat(), 0f)
        mLeftShadow!!.draw(canvas)
        canvas.restore()
    }

    companion object {
        // 页面边缘阴影的宽度默认值
        private const val SHADOW_WIDTH = 16
    }

    init {
        initView(context)
    }
}