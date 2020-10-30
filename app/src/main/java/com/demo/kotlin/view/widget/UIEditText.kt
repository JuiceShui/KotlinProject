package com.demo.kotlin.view.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import com.demo.kotlin.R


/**
 * 支持一键清除文字，可自定义图标
 */
class UIEditText(context: Context?, attrs: AttributeSet) :
    AppCompatEditText(context!!, attrs) {
    private var mClearEnabled = false
    private var mClearIcon: Drawable? = null
    private fun init(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.UIEditText)
        mClearEnabled = a.getBoolean(R.styleable.UIEditText_ui_clear_enabled, true)
        setClearIcon(a.getDrawable(R.styleable.UIEditText_ui_clear_icon))
        a.recycle()
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (mClearEnabled) {
            if (hasFocus() && text.length > 0) {
                showClearIcon()
            } else {
                hideClearIcon()
            }
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (mClearEnabled) {
            if (focused && text!!.length > 0) {
                showClearIcon()
            } else {
                hideClearIcon()
            }
        }
    }

    private fun showClearIcon() {
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1], mClearIcon, compoundDrawables[3]
        )
    }

    private fun hideClearIcon() {
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], null, compoundDrawables[3])
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mClearEnabled) {
            val x = event.x.toInt()
            if (mClearIcon != null && text!!.length > 0 && x > width - paddingRight - mClearIcon!!.intrinsicWidth) {
                setText("")
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setClearEnabled(enabled: Boolean) {
        mClearEnabled = enabled
        if (mClearEnabled) {
            // 如果icon为null，将使用默认icon
            setClearIcon(mClearIcon)
        }
    }

    fun setClearIcon(@DrawableRes resId: Int) {
        setClearIcon(AppCompatResources.getDrawable(context, resId))
    }

    /**
     * 设置清空按钮icon
     *
     * @param icon 为null时，无法触发点击清空事件
     */
    fun setClearIcon(icon: Drawable?) {
        mClearIcon = icon
        if (mClearEnabled && mClearIcon == null) {
            mClearIcon = AppCompatResources.getDrawable(
                context,
                R.drawable.uiedittext_default_clear_icon
            )
        }
        if (mClearIcon != null) {
            mClearIcon!!.setBounds(
                0, 0, mClearIcon!!.intrinsicWidth,
                mClearIcon!!.intrinsicHeight
            )
        }
    }

    init {
        init(attrs)
    }
}