package com.demo.kotlin.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.demo.kotlin.base.BaseApplication

class ToastUtil {
    companion object{
        /**
         * 之前显示的内容
         */
        private var oldMsg = 0

        /**
         * Toast对象
         */
        private var toast: Toast? = null

        /**
         * 第一次时间
         */
        private var oneTime: Long = 0

        /**
         * 第二次时间
         */
        private var twoTime: Long = 0

        /*public static void showToast(@StringRes int resId) {
            showToast(YYUtils.getInstance().getContext(), Toast.LENGTH_SHORT, resId);
        }*/

        fun showToast(@StringRes resId: Int) {
            if (toast == null) {
                toast = Toast.makeText(Utils().getInstance()?.getContext(), resId, Toast.LENGTH_SHORT)
                toast!!.show()
                oneTime = System.currentTimeMillis()
            } else {
                twoTime = System.currentTimeMillis()
                if (resId == oldMsg) {
                    if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                        toast!!.show()
                    }
                } else {
                    oldMsg = resId
                    toast!!.setText(resId)
                    toast!!.show()
                }
            }
            oneTime = twoTime
        }

        fun showToast(text: String?) {
            showToast(Utils().getInstance()?.getContext(), Toast.LENGTH_SHORT, text)
        }

        fun showLongToast(@StringRes resId: Int) {
            Utils().getInstance()?.getContext()?.let { showToast(it, Toast.LENGTH_LONG, resId) }
        }

        fun showLongToast(text: String?) {
            showToast(Utils().getInstance()?.getContext(), Toast.LENGTH_LONG, text)
        }

        fun showToast(ctx: Context, @StringRes resId: Int) {
            showToast(ctx, Toast.LENGTH_SHORT, resId)
        }

        fun showToast(ctx: Context?, text: String?) {
            showToast(ctx, Toast.LENGTH_SHORT, text)
        }

        fun showLongToast(ctx: Context, @StringRes resId: Int) {
            showToast(ctx, Toast.LENGTH_LONG, resId)
        }

        fun showLongToast(ctx: Context?, text: String?) {
            showToast(ctx, Toast.LENGTH_LONG, text)
        }

        fun showToast(ctx: Context, duration: Int, resId: Int) {
            showToast(ctx, duration, ctx.getString(resId))
        }

        fun showToast(ctx: Context?, duration: Int, text: String?) {
            Toast.makeText(ctx, text, duration).show()
        }

        /**
         * 在UI线程运行弹出
         */
        fun showToastOnUiThread(ctx: Activity?, text: String?) {
            ctx?.runOnUiThread { showToast(ctx, text) }
        }
    }

}