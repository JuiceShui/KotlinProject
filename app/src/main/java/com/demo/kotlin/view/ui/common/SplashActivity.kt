package com.demo.kotlin.view.ui.common

import android.view.View
import com.demo.kotlin.R
import com.demo.kotlin.base.BaseActivity
import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.base.mvp.IPresenter
import com.demo.kotlin.data.entity.TXTips
import com.demo.kotlin.databinding.ActivitySplashBinding
import com.demo.kotlin.utils.DateUtil
import com.demo.kotlin.utils.GlideUtils
import com.demo.kotlin.view.ui.common.contract.SplashContract
import com.demo.kotlin.view.ui.common.presenter.SplashPresenter
import com.demo.kotlin.view.ui.home.MainActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract.View, View.OnClickListener {
    private lateinit var mBinding: ActivitySplashBinding
    private lateinit var mDisposable: Disposable

    @Inject
    lateinit var mPresenter: SplashPresenter
    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun initBinding() {
        mBinding = mViewDataBinding as ActivitySplashBinding
        mBinding.click = this
    }

    override val layoutId: Int
        get() = R.layout.activity_splash

    override fun getPresenter(): IPresenter<ILoadView>? {
        return mPresenter as IPresenter<ILoadView>
    }

    override fun initViews() {
        mPresenter.getTips(DateUtil.formatyMd(System.currentTimeMillis()))
        mDisposable = Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tv_count.text = getString(R.string.splash_skip, 5 - it)
                if (it == 5L) {
                    MainActivity.launch(this@SplashActivity)
                }
            }, {
                it.printStackTrace()
            })
        addSubscribe(mDisposable)
    }

    override fun onGetTips(tips: TXTips?) {
        tips?.let {
            GlideUtils.loadImage(it.imgurl, iv_tips, R.mipmap.ic_splash_default)
        }
        if (tips == null) {
            GlideUtils.loadLocalImage(R.mipmap.ic_splash_default, iv_tips)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_count -> {
                MainActivity.launch(this)
                finish()
            }
        }
    }
}