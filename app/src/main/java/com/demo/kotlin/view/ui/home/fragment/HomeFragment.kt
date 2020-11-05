package com.demo.kotlin.view.ui.home.fragment

import android.view.View
import com.demo.kotlin.R
import com.demo.kotlin.base.BaseFragment
import com.demo.kotlin.databinding.FragmentHomeBinding
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment(val text: String) : BaseFragment(), View.OnClickListener {

    private lateinit var mBinding: FragmentHomeBinding
    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    override fun initBinding(): View? {
        mBinding = mViewDataBinding as FragmentHomeBinding
        mBinding.click = this
        fitSystemBar(true)
        return mBinding.root
    }


    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onClick(v: View?) {

    }

    override fun initViews() {
        mBinding.tvText.text = text
        when (text) {
            "AAA" -> mBinding.root.setBackgroundColor(mActivity?.resources!!.getColor(R.color.red_active))
            "BBB" -> mBinding.root.setBackgroundColor(mActivity?.resources!!.getColor(R.color.black))
            "CCC" -> mBinding.root.setBackgroundColor(mActivity?.resources!!.getColor(R.color.green_active))
            "DDD" -> mBinding.root.setBackgroundColor(mActivity?.resources!!.getColor(R.color.yellow_active))
            "EEE" -> mBinding.root.setBackgroundColor(mActivity?.resources!!.getColor(R.color.orange_active))
            else -> mBinding.root.setBackgroundColor(mActivity?.resources!!.getColor(R.color.purple_active))
        }
    }
}