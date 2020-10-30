package com.demo.kotlin.view.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import com.demo.kotlin.R
import com.demo.kotlin.utils.statusBarHeight

class AppBar(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var mTvTitle: AppCompatTextView? = null
    private var mTvLeftText: AppCompatTextView? = null
    private var mTvRightText: AppCompatTextView? = null
    private var mTvRightSecondaryText: AppCompatTextView? = null
    private var mIvLeftIcon: AppCompatImageView? = null
    private var mIvRightIcon: AppCompatImageView? = null
    private var mIvRightSecondaryIcon: AppCompatImageView? = null
    private var mLlLeftWrapper: LinearLayout? = null
    private var mLlRightWrapper: LinearLayout? = null
    private var mLlRightPrimaryWrapper: LinearLayout? = null
    private var mLlRightSecondaryWrapper: LinearLayout? = null
    private var mFlTitleWrapper: FrameLayout? = null
    private var mCustomView: View? = null
    private var mLeftClickListener: OnClickListener? = null
    private var mRightClickListener: OnClickListener? = null
    private var mRightSecondaryClickListener: OnClickListener? = null
    private var mTitleTextAppearance = 0
    private var mLeftTextAppearance = 0
    private var mRightTextAppearance = 0
    private var mRightSecondaryTextAppearance = 0
    private var mTitleTextColor = 0
    private var mLeftTextColor = 0
    private var mRightTextColor = 0
    private var mRightSecondaryTextColor = 0
    private var mTitleTextSize = 0
    private var mLeftTextSize = 0
    private var mRightTextSize = 0
    private var mRightSecondaryTextSize = 0
    private var mHeight = 0
    private var mStatusBarHeight = 0
    private var mTextHorizontalPadding = 0
    private var mTitleHorizontalMargin = 0

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.app_bar_style
    ) {
    }

    private fun initView() {}
    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AppBar, defStyleAttr, 0)
        mTextHorizontalPadding =
            a.getDimensionPixelOffset(R.styleable.AppBar_app_bar_text_padding_horizontal, -1)
        mHeight = a.getLayoutDimension(R.styleable.AppBar_android_layout_height, -1)
        minimumHeight = if (mHeight < 0) {
            throw UnsupportedOperationException("you must supply an exact dimen value.")
        } else {
            mHeight
        }
        val fitStatusBar = a.getBoolean(R.styleable.AppBar_ui_fit_status_bar, true)
        if (fitStatusBar) {
            // 设置状态栏预留高度
            mStatusBarHeight = (context as Activity).statusBarHeight()
            setPadding(
                paddingLeft, paddingTop + mStatusBarHeight,
                paddingRight, paddingBottom
            )
            post { // 修改Toolbar高度
                val params = layoutParams
                params.height = mHeight + mStatusBarHeight
                layoutParams = params
            }
        }
        // textAppearance
        mTitleTextAppearance = a.getResourceId(R.styleable.AppBar_app_bar_title_text_appearance, 0)
        mLeftTextAppearance = a.getResourceId(R.styleable.AppBar_app_bar_left_text_appearance, 0)
        mRightTextAppearance = a.getResourceId(R.styleable.AppBar_app_bar_right_text_appearance, 0)
        mRightSecondaryTextAppearance =
            a.getResourceId(R.styleable.AppBar_app_bar_right_secondary_text_appearance, 0)
        // textColor
        mTitleTextColor = a.getColor(R.styleable.AppBar_app_bar_title_text_color, 0)
        mLeftTextColor = a.getColor(R.styleable.AppBar_app_bar_left_text_color, 0)
        mRightTextColor = a.getColor(R.styleable.AppBar_app_bar_right_text_color, 0)
        mRightSecondaryTextColor =
            a.getColor(R.styleable.AppBar_app_bar_right_secondary_text_color, mRightTextColor)
        // textSize
        mTitleTextSize = a.getDimensionPixelSize(R.styleable.AppBar_app_bar_title_text_size, -1)
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.AppBar_app_bar_left_text_size, -1)
        mRightTextSize = a.getDimensionPixelSize(R.styleable.AppBar_app_bar_right_text_size, -1)
        mRightSecondaryTextSize = a.getDimensionPixelSize(
            R.styleable.AppBar_app_bar_right_secondary_text_size,
            mRightTextSize
        )
        val title = a.getString(R.styleable.AppBar_app_bar_title)
        setTitle(title)
        val leftText = a.getString(R.styleable.AppBar_app_bar_left_text)
        setLeftText(leftText)
        val rightText = a.getString(R.styleable.AppBar_app_bar_right_text)
        setRightText(rightText)
        val rightSecondaryText = a.getString(R.styleable.AppBar_app_bar_right_secondary_text)
        setRightSecondaryText(rightSecondaryText)
        val leftIcon = a.getDrawable(R.styleable.AppBar_app_bar_left_icon)
        setLeftIcon(leftIcon)
        val rightIcon = a.getDrawable(R.styleable.AppBar_app_bar_right_icon)
        setRightIcon(rightIcon)
        val rightSecondaryIcon = a.getDrawable(R.styleable.AppBar_app_bar_right_secondary_icon)
        setRightSecondaryIcon(rightSecondaryIcon)
        // custom view
        val layoutId = a.getResourceId(R.styleable.AppBar_app_bar_custom_view, 0)
        if (layoutId != 0) {
            setCustomView(layoutId)
        }
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mTvTitle != null) {
            var titleMarginLeft = 0
            var titleMarginRight = 0
            if (mLlLeftWrapper != null) {
                titleMarginLeft = mLlLeftWrapper!!.measuredWidth
            }
            if (mLlRightWrapper != null) {
                titleMarginRight = mLlRightWrapper!!.measuredWidth
            }
            val titleMargin = Math.max(titleMarginLeft, titleMarginRight)
            if (titleMargin != 0 && titleMargin != mTitleHorizontalMargin) {
                mTitleHorizontalMargin = titleMargin
                val titleMaxWidth = measuredWidth - mTitleHorizontalMargin * 2
                val params = mTvTitle!!.layoutParams as LayoutParams
                params.width = Math.min(titleMaxWidth, params.width)
                mTvTitle!!.layoutParams = params
            }
        }
    }

    //    public void setFloat(boolean enabled) {
    //        if (enabled) {
    //            if (mShadowDrawable == null) {
    //                mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
    //                        new int[]{Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
    //            }
    //            InsetDrawable insetDrawable = new InsetDrawable(mShadowDrawable, 0, getHeight(), 0, 0);
    //            int mShadowHeight = (int) (8 * getResources().getDisplayMetrics().density);
    //            insetDrawable.setBounds(getLeft(),
    //                    getTop(),
    //                    getRight(),
    //                    getBottom() + mShadowHeight);
    //            setBackgroundResource(R.drawable.bg_avatar_stroke);
    //        } else {
    //            setBackground(null);
    //        }
    //    }
    //    @Override
    //    protected void dispatchDraw(Canvas canvas) {
    //        super.dispatchDraw(canvas);
    //        Paint paint = new Paint();  //定义一个Paint
    //        Shader mShader = new LinearGradient(getWidth() / 2, getHeight() - 16, getWidth() / 2, getHeight(), new int[]{Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")}, null, Shader.TileMode.REPEAT);
    ////新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
    //
    //        paint.setShader(mShader);
    //        paint.setStrokeWidth(getWidth());
    //        canvas.drawLine(getWidth() / 2, getHeight(), getWidth() / 2, getHeight()+16, paint);
    //    }
    private fun ensureLeftWrapper() {
        if (mLlLeftWrapper == null) {
            mLlLeftWrapper = LinearLayout(context)
            val params = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            mLlLeftWrapper!!.layoutParams = params
            mLlLeftWrapper!!.gravity = Gravity.CENTER_VERTICAL
            addView(mLlLeftWrapper, params)
        }
    }

    private fun ensureRightWrapper() {
        if (mLlRightWrapper == null) {
            mLlRightWrapper = LinearLayout(context)
            val params = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,
                GravityCompat.END
            )
            mLlRightWrapper!!.layoutParams = params
            mLlRightWrapper!!.gravity = Gravity.CENTER_VERTICAL
            addView(mLlRightWrapper, params)
        }
    }

    private fun ensureRightPrimaryWrapper() {
        ensureRightWrapper()
        if (mLlRightPrimaryWrapper == null) {
            mLlRightPrimaryWrapper = LinearLayout(context)
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            mLlRightPrimaryWrapper!!.layoutParams = params
            mLlRightPrimaryWrapper!!.gravity = Gravity.CENTER_VERTICAL
            mLlRightWrapper!!.addView(mLlRightPrimaryWrapper, params)
        }
    }

    private fun ensureRightSecondaryWrapper() {
        ensureRightWrapper()
        if (mLlRightSecondaryWrapper == null) {
            mLlRightSecondaryWrapper = LinearLayout(context)
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            mLlRightSecondaryWrapper!!.layoutParams = params
            mLlRightSecondaryWrapper!!.gravity = Gravity.CENTER_VERTICAL
            mLlRightWrapper!!.addView(mLlRightSecondaryWrapper, 0, params)
        }
    }

    private fun ensureTitleWrapper() {
        if (mFlTitleWrapper == null) {
            mFlTitleWrapper = FrameLayout(context)
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            mFlTitleWrapper!!.layoutParams = params
            addView(mFlTitleWrapper, 0, params)
        }
    }

    /**
     * 创建Icon按钮
     * Icon宽度与AppBar高度相同，表现为正方形
     */
    private fun createImageButton(): AppCompatImageView {
        val imageButton = AppCompatImageView(
            context, null,
            R.attr.toolbarNavigationButtonStyle
        )
        val params = ViewGroup.LayoutParams(
            mHeight,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageButton.layoutParams = params
        return imageButton
    }

    /**
     * 创建文字按钮
     * 文字按钮最小宽度与AppBar高度相同
     */
    private fun createTextView(
        textAppearance: Int,
        textColor: Int,
        textSize: Int
    ): AppCompatTextView {
        val textView = AppCompatTextView(
            context, null,
            R.attr.toolbarNavigationButtonStyle
        )
        textView.setSingleLine()
        textView.gravity = Gravity.CENTER
        textView.minWidth = mHeight
        textView.minimumWidth = mHeight
        if (mTextHorizontalPadding > -1) {
            textView.setPadding(mTextHorizontalPadding, 0, mTextHorizontalPadding, 0)
        }
        if (textAppearance != 0) {
            textView.setTextAppearance(context, textAppearance)
        }
        if (textColor != 0) {
            textView.setTextColor(textColor)
        }
        if (textSize > -1) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.layoutParams = params
        return textView
    }

    /**
     * 将View添加到指定容器
     * 如果View已经在目标容器中，则不做任何操作
     *
     * @param view    要添加的View
     * @param wrapper 容器
     */
    private fun addViewToWrapper(view: View?, wrapper: ViewGroup?) {
        if (view!!.parent !== wrapper) {
            wrapper!!.removeAllViews()
            wrapper.addView(view, view!!.layoutParams)
        }
    }

    /**
     * 为左边View绑定点击事件
     */
    private fun setLeftClickListener(view: View?) {
        view!!.setOnClickListener { v ->
            if (mLeftClickListener != null) {
                mLeftClickListener!!.onClick(v)
            }
        }
    }

    /**
     * 为右边View绑定点击事件
     *
     * @param primary 是否是primary(右数第一个)按钮
     */
    private fun setRightClickListener(view: View?, primary: Boolean) {
        view!!.setOnClickListener { v ->
            if (primary) {
                if (mRightClickListener != null) {
                    mRightClickListener!!.onClick(v)
                }
            } else {
                if (mRightSecondaryClickListener != null) {
                    mRightSecondaryClickListener!!.onClick(v)
                }
            }
        }
    }

    fun setLeftIcon(@DrawableRes resId: Int) {
        setLeftIcon(AppCompatResources.getDrawable(context, resId))
    }

    /**
     * 设置左边按钮Icon
     * 设置Icon的同时会移除文字
     */
    fun setLeftIcon(icon: Drawable?) {
        if (icon != null) {
            ensureLeftWrapper()
            if (mIvLeftIcon == null) {
                mIvLeftIcon = createImageButton()
                setLeftClickListener(mIvLeftIcon)
            }
            addViewToWrapper(mIvLeftIcon, mLlLeftWrapper)
        } else if (mIvLeftIcon != null && mIvLeftIcon!!.parent === mLlLeftWrapper) {
            mLlLeftWrapper!!.removeView(mIvLeftIcon)
        }
        if (mIvLeftIcon != null) {
            mIvLeftIcon!!.setImageDrawable(icon)
        }
    }

    fun setLeftText(@StringRes resId: Int) {
        setLeftText(context.getText(resId))
    }

    /**
     * 设置左边按钮文字
     * 设置文字的同时会移除Icon
     */
    fun setLeftText(text: CharSequence?) {
        if (!TextUtils.isEmpty(text)) {
            ensureLeftWrapper()
            if (mTvLeftText == null) {
                mTvLeftText = createTextView(mLeftTextAppearance, mLeftTextColor, mLeftTextSize)
                setLeftClickListener(mTvLeftText)
            }
            addViewToWrapper(mTvLeftText, mLlLeftWrapper)
        } else if (mTvLeftText != null && mTvLeftText!!.parent === mLlLeftWrapper) {
            mLlLeftWrapper!!.removeView(mTvLeftText)
        }
        if (mTvLeftText != null) {
            mTvLeftText!!.text = text
        }
    }

    fun setTitle(@StringRes resId: Int) {
        setTitle(context.getText(resId))
    }

    /**
     * 设置标题，同时会移除自定义View
     */
    fun setTitle(title: CharSequence?) {
        if (!TextUtils.isEmpty(title)) {
            ensureTitleWrapper()
            if (mTvTitle == null) {
                mTvTitle = AppCompatTextView(context)
                mTvTitle?.let {
                    it.setSingleLine()
                    it.ellipsize = TextUtils.TruncateAt.MARQUEE
                    it.isFocusableInTouchMode = true
                    if (mTitleTextAppearance != 0) {
                        it.setTextAppearance(context, mTitleTextAppearance)
                    }
                    if (mTitleTextColor != 0) {
                        it.setTextColor(mTitleTextColor)
                    }
                    if (mTitleTextSize > -1) {
                        it.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize.toFloat())
                    }
                    val params = LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                    )
                    it.layoutParams = params
                }

            }
            addViewToWrapper(mTvTitle, mFlTitleWrapper)
        } else if (mTvTitle != null && mTvTitle!!.parent === mFlTitleWrapper) {
            mFlTitleWrapper!!.removeView(mTvTitle)
        }
        if (mTvTitle != null) {
            mTvTitle!!.text = title
        }
    }

    fun setRightText(@StringRes resId: Int) {
        setRightText(context.getText(resId))
    }

    /**
     * 设置右数第一个按钮文字
     * 设置文字的同时会移除Icon
     */
    fun setRightText(text: CharSequence?) {
        if (!TextUtils.isEmpty(text)) {
            ensureRightPrimaryWrapper()
            if (mTvRightText == null) {
                mTvRightText = createTextView(mRightTextAppearance, mRightTextColor, mRightTextSize)
                setRightClickListener(mTvRightText, true)
            }
            addViewToWrapper(mTvRightText, mLlRightPrimaryWrapper)
        } else if (mTvRightText != null && mTvRightText!!.parent === mLlRightPrimaryWrapper) {
            mLlRightPrimaryWrapper!!.removeView(mTvRightText)
        }
        if (mTvRightText != null) {
            mTvRightText!!.text = text
        }
    }

    fun setRightSecondaryText(@StringRes resId: Int) {
        setRightSecondaryText(context.getText(resId))
    }

    /**
     * 设置右数第二个按钮文字
     * 设置文字的同时会移除Icon
     */
    fun setRightSecondaryText(text: CharSequence?) {
        if (!TextUtils.isEmpty(text)) {
            ensureRightSecondaryWrapper()
            if (mTvRightSecondaryText == null) {
                mTvRightSecondaryText = createTextView(
                    mRightSecondaryTextAppearance,
                    mRightSecondaryTextColor, mRightSecondaryTextSize
                )
                setRightClickListener(mTvRightSecondaryText, false)
            }
            addViewToWrapper(mTvRightSecondaryText, mLlRightSecondaryWrapper)
        } else if (mTvRightSecondaryText != null && mTvRightSecondaryText!!.parent === mLlRightSecondaryWrapper) {
            mLlRightSecondaryWrapper!!.removeView(mTvRightSecondaryText)
        }
        if (mTvRightSecondaryText != null) {
            mTvRightSecondaryText!!.text = text
        }
    }

    fun setRightIcon(@DrawableRes resId: Int) {
        setRightIcon(AppCompatResources.getDrawable(context, resId))
    }

    /**
     * 设置右数第一个按钮Icon
     * 设置Icon的同时会移除文字
     */
    fun setRightIcon(icon: Drawable?) {
        if (icon != null) {
            ensureRightPrimaryWrapper()
            if (mIvRightIcon == null) {
                mIvRightIcon = createImageButton()
                setRightClickListener(mIvRightIcon, true)
            }
            addViewToWrapper(mIvRightIcon, mLlRightPrimaryWrapper)
        } else if (mIvRightIcon != null && mIvRightIcon!!.parent === mLlRightPrimaryWrapper) {
            mLlRightPrimaryWrapper!!.removeView(mIvRightIcon)
        }
        if (mIvRightIcon != null) {
            mIvRightIcon!!.setImageDrawable(icon)
        }
    }

    fun setRightSecondaryIcon(@DrawableRes resId: Int) {
        setRightSecondaryIcon(AppCompatResources.getDrawable(context, resId))
    }

    /**
     * 设置右数第二个按钮Icon
     * 设置Icon的同时会移除文字
     */
    fun setRightSecondaryIcon(icon: Drawable?) {
        if (icon != null) {
            ensureRightSecondaryWrapper()
            if (mIvRightSecondaryIcon == null) {
                mIvRightSecondaryIcon = createImageButton()
                setRightClickListener(mIvRightSecondaryIcon, false)
            }
            addViewToWrapper(mIvRightSecondaryIcon, mLlRightSecondaryWrapper)
        } else if (mIvRightSecondaryIcon != null && mIvRightSecondaryIcon!!.parent === mLlRightPrimaryWrapper) {
            mLlRightSecondaryWrapper!!.removeView(mIvRightSecondaryIcon)
        }
        if (mIvRightSecondaryIcon != null) {
            mIvRightSecondaryIcon!!.setImageDrawable(icon)
        }
    }

    fun setCustomView(@LayoutRes resId: Int): View {
        val view = inflate(context, resId, null)
        setCustomView(view)
        return view
    }

    /**
     * 设置自定义View
     * 设置自定义View的同时会移除title
     *
     * @param view 自定义View
     */
    fun setCustomView(view: View?) {
        mCustomView = view
        var params = mCustomView?.layoutParams as LayoutParams
        if (params == null) {
            params = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER
            )
            mCustomView!!.layoutParams = params
        }
        addViewToWrapper(mCustomView, mFlTitleWrapper)
    }

    fun setOnLeftClickListener(listener: OnClickListener?) {
        mLeftClickListener = listener
    }

    fun setOnRightClickListener(listener: OnClickListener?) {
        mRightClickListener = listener
    }

    fun setOnRightSecondaryClickListener(listener: OnClickListener?) {
        mRightSecondaryClickListener = listener
    }

    init {
        initView()
        init(attrs, defStyleAttr)
    }
}
