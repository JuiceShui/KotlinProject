package com.demo.kotlin.view.widget

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.demo.kotlin.R
import com.demo.kotlin.base.view.ILoadingIndicator

/**
 * Default loading.
 */
class DefaultLoadingDialog(private val mContext: Context) :
    Dialog(mContext, R.style.Dialog_LoadingDialog), ILoadingIndicator {
    private var mTvTips: TextView? = null
    private fun init() {
        val window = window
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        )
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.dialog_default_loading, null)
        mTvTips = view.findViewById<View>(R.id.default_loading_dialog_tips) as TextView
        //设置加载的view
        setContentView(view)
        //设置点击外面不消失
        setCanceledOnTouchOutside(false)
    }

    fun setMessage(message: String?) {
        if (TextUtils.isEmpty(message)) {
            mTvTips!!.visibility = View.GONE
        } else {
            mTvTips!!.text = message
            mTvTips!!.visibility = View.VISIBLE
        }
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun showLoading() {
        if (!isShowing) {
            show()
        }
    }

    override fun dismissLoading() {
        if (isShowing) {
            dismiss()
        }
    }

    init {
        init()
    }
}