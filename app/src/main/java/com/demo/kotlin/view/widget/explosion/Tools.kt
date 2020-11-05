package com.demo.kotlin.view.widget.explosion

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import kotlin.math.roundToInt

object Tools {
    private val DENSITY: Float = Resources.getSystem().displayMetrics.density
    private val sCanvas: Canvas = Canvas()
    fun dp2Px(dp: Int): Int {
        return (dp * DENSITY).roundToInt()
    }

    fun createBitmapFromView(view: View): Bitmap? {
        if (view is ImageView) {
            val drawable: Drawable = (view as ImageView).getDrawable()
            if (drawable != null && drawable is BitmapDrawable) {
                return drawable.bitmap
            }
        }
        view.clearFocus()
        val bitmap = createBitmapSafely(
            view.width,
            view.height, Bitmap.Config.ARGB_8888, 1
        )
        if (bitmap != null) {
            synchronized(sCanvas) {
                val canvas: Canvas = sCanvas
                canvas.setBitmap(bitmap)
                view.draw(canvas)
                canvas.setBitmap(null)
            }
        }
        return bitmap
    }

    fun createBitmapSafely(
        width: Int,
        height: Int,
        config: Bitmap.Config?,
        retryCount: Int
    ): Bitmap? {
        return try {
            Bitmap.createBitmap(width, height, config!!)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            if (retryCount > 0) {
                System.gc()
                return createBitmapSafely(width, height, config, retryCount - 1)
            }
            null
        }
    }
}