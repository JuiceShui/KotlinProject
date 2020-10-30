package com.demo.kotlin.view.widget.adapter.decoration

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import java.util.*

class FloatingItemDecoration : ItemDecoration {
    private var mDivider: Drawable?
    private var mDividerHeight: Int
    private var mDividerWidth: Int
    private val mKeys: MutableMap<Int, String> = HashMap()
    private var mTitleHeight = 0
    private var mTextPaint: Paint? = null
    private var mBackgroundPaint: Paint? = null
    private var mTextHeight = 0f
    private var mTextBaselineOffset = 0f
    private var mContext: Context? = null

    // 滚动列表的时候是否一直显示悬浮头部
    private var mShowFloatingHeaderOnScrolling = true

    constructor(context: Context) {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        mDividerHeight = mDivider!!.intrinsicHeight
        mDividerWidth = mDivider!!.intrinsicWidth
        init(context)
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId 分割线图片
     */
    constructor(context: Context, @DrawableRes drawableId: Int) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider!!.intrinsicHeight
        mDividerWidth = mDivider!!.intrinsicWidth
        init(context)
    }

    /**
     * 自定义分割线
     * 也可以使用[Canvas.drawRect]或者[Canvas.drawText]等等
     * 结合[Paint]去绘制各式各样的分割线
     *
     * @param context
     * @param color         整型颜色值，非资源id
     * @param dividerWidth  单位为dp
     * @param dividerHeight 单位为dp
     */
    constructor(
        context: Context,
        @ColorInt color: Int,
        @Dimension dividerWidth: Float,
        @Dimension dividerHeight: Float
    ) {
        mDivider = ColorDrawable(color)
        mDividerWidth =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dividerWidth,
                context.resources.displayMetrics
            )
                .toInt()
        mDividerHeight =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dividerHeight,
                context.resources.displayMetrics
            )
                .toInt()
        init(context)
    }

    private fun init(mContext: Context) {
        this.mContext = mContext
        mTextPaint = Paint()
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            16f,
            mContext.resources.displayMetrics
        )
        mTextPaint!!.color = Color.WHITE
        val fm = mTextPaint!!.fontMetrics
        mTextHeight = fm.bottom - fm.top //计算文字高度
        mTextBaselineOffset = fm.bottom
        mBackgroundPaint = Paint()
        mBackgroundPaint!!.isAntiAlias = true
        mBackgroundPaint!!.color = Color.MAGENTA
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        drawVertical(c, parent)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (!mShowFloatingHeaderOnScrolling) {
            return
        }
        val firstVisiblePos =
            (parent.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (firstVisiblePos == RecyclerView.NO_POSITION) {
            return
        }
        val title = getTitle(firstVisiblePos)
        if (TextUtils.isEmpty(title)) {
            return
        }
        var flag = false
        if (getTitle(firstVisiblePos + 1) != null && title != getTitle(firstVisiblePos + 1)) {
            //说明是当前组最后一个元素，但不一定碰撞了
//            Log.e(TAG, "onDrawOver: "+"==============" +firstVisiblePos);
            val child = parent.findViewHolderForAdapterPosition(firstVisiblePos)!!.itemView
            if (child.top + child.measuredHeight < mTitleHeight) {
                //进一步检测碰撞
//                Log.e(TAG, "onDrawOver: "+child.getTop()+"$"+firstVisiblePos );
                c.save() //保存画布当前的状态
                flag = true
                c.translate(0f, child.top + child.measuredHeight - mTitleHeight.toFloat()) //负的代表向上
            }
        }
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val top = parent.paddingTop
        val bottom = top + mTitleHeight
        c.drawRect(
            left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(),
            mBackgroundPaint!!
        )
        val x = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            mContext!!.resources.displayMetrics
        )
        val y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset //计算文字baseLine
        c.drawText(title!!, x, y, mTextPaint!!)
        if (flag) {
            //还原画布为初始状态
            c.restore()
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildViewHolder(view).adapterPosition
        if (mKeys.containsKey(pos)) { //留出头部偏移
            outRect[0, mTitleHeight, 0] = 0
        } else {
            outRect[0, mDividerHeight, 0] = 0
        }
    }

    /**
     * *如果该位置没有，则往前循环去查找标题，找到说明该位置属于该分组
     *
     * @param position
     * @return
     */
    private fun getTitle(position: Int): String? {
        var position = position
        while (position >= 0) {
            if (mKeys.containsKey(position)) {
                return mKeys[position]
            }
            position--
        }
        return null
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        var top = 0
        var bottom = 0
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            if (!mKeys.containsKey(params.viewLayoutPosition)) {
                //画普通分割线
                top = child.top - params.topMargin - mDividerHeight
                bottom = top + mDividerHeight
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(c)
            } else {
                //画头部
                top = child.top - params.topMargin - mTitleHeight
                bottom = top + mTitleHeight
                c.drawRect(
                    left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(),
                    mBackgroundPaint!!
                )
                //                float x=child.getPaddingLeft()+params.leftMargin;
                val x = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f,
                    mContext!!.resources.displayMetrics
                )
                val y =
                    bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset //计算文字baseLine
                //                Log.e(TAG, "drawVertical: "+bottom );
                c.drawText(mKeys[params.viewLayoutPosition]!!, x, y, mTextPaint!!)
            }
        }
    }

    fun setmShowFloatingHeaderOnScrolling(mShowFloatingHeaderOnScrolling: Boolean) {
        this.mShowFloatingHeaderOnScrolling = mShowFloatingHeaderOnScrolling
    }

    fun setmKeys(mKeys: Map<Int, String>?) {
        this.mKeys.clear()
        this.mKeys.putAll(mKeys!!)
    }

    fun setmTitleHeight(titleHeight: Int) {
        mTitleHeight = titleHeight
    }

    companion object {
        private const val TAG = "FloatingItemDecoration"
        private val ATTRS = intArrayOf(R.attr.listDivider)
    }
}
