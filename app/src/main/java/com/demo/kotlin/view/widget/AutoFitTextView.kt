package com.demo.kotlin.view.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView

class AutoFitTextView(context: Context?, attrs: AttributeSet?) :
    AppCompatTextView(context!!, attrs) {
    // Attributes
    private var testPaint: Paint? = null
    private var minTextSize = 0f
    private var maxTextSize = 0f
    private fun init() {
        testPaint = Paint()
        testPaint!!.set(this.paint)
        // max size defaults to the intially specified text size unless it is
        // too small
        maxTextSize = this.textSize
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE
        }
        minTextSize = DEFAULT_MIN_TEXT_SIZE
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private fun refitText(text: String, textWidth: Int) {
        if (textWidth > 0) {
            val availableWidth = textWidth - this.paddingLeft -
                    this.paddingRight
            var trySize = maxTextSize
            testPaint!!.textSize = trySize
            while (trySize > minTextSize &&
                testPaint!!.measureText(text) > availableWidth
            ) {
                trySize -= 1f
                if (trySize <= minTextSize) {
                    trySize = minTextSize
                    break
                }
                testPaint!!.textSize = trySize
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize)
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        refitText(text.toString(), this.width)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw) {
            refitText(this.text.toString(), w)
        }
    }

    companion object {
        private const val DEFAULT_MIN_TEXT_SIZE = 2f
        private const val DEFAULT_MAX_TEXT_SIZE = 20f
    }

    init {
        init()
    }
}
