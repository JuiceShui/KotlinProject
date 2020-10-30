package com.demo.kotlin.view.ui.home

import android.view.View
import com.demo.kotlin.R
import com.demo.kotlin.base.BaseActivity
import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.base.mvp.IPresenter
import com.demo.kotlin.data.entity.WXHotNewsEntity
import com.demo.kotlin.databinding.ActivityHomeBinding
import com.demo.kotlin.utils.ToastUtil
import com.demo.kotlin.view.ui.home.contract.HomeContract
import com.demo.kotlin.view.ui.home.presenter.HomePresenter
import kotlinx.android.synthetic.main.activity_home.*
import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Named

class HomeActivity : BaseActivity(), View.OnClickListener, HomeContract.View {
    private lateinit var mBinding: ActivityHomeBinding

    @Inject
    lateinit var mPresenter: HomePresenter
    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun initBinding() {
        mBinding = mViewDataBinding as ActivityHomeBinding
        mBinding.click = this
    }

    override val layoutId: Int
        get() = R.layout.activity_home

    override fun getPresenter(): IPresenter<ILoadView> {
        return mPresenter as IPresenter<ILoadView>
    }

    override fun getData() {
        mPresenter.getData(1)
    }

    override fun onClick(v: View?) {

    }

    override fun canSlidingClose(): Boolean {
        return true
    }

    override fun onGetData(data: MutableList<WXHotNewsEntity>) {
        //ToastUtil.showLongToast(data.size)
        tv_home.text = "hhhhhhh"
        val sb: StringBuilder = StringBuilder()
        for (it in data) {
            sb.append(it.title)
                .append("\n")
        }
        tv_home.text = sb.toString()
    }
}