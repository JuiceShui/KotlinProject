package com.demo.kotlin.view.ui.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.demo.kotlin.R
import com.demo.kotlin.base.BaseActivity
import com.demo.kotlin.databinding.ActivityMainBinding
import com.demo.kotlin.view.ui.home.adapter.MainFragmentPagerAdapter
import com.demo.kotlin.view.ui.home.fragment.HomeFragment
import com.demo.kotlin.view.ui.home.fragment.SoapFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mFragments: MutableList<Fragment>
    private lateinit var mAdapter: MainFragmentPagerAdapter
    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun initBinding() {
        mBinding = mViewDataBinding as ActivityMainBinding
        mBinding.click = this
    }

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onClick(v: View?) {

    }

    override fun initViews() {
        mFragments = ArrayList<Fragment>()
        mFragments.add(HomeFragment("AAA"))
        mFragments.add(HomeFragment("BBB"))
        mFragments.add(HomeFragment("CCC"))
        mFragments.add(HomeFragment("DDD"))
        mFragments.add(SoapFragment())
        mAdapter = MainFragmentPagerAdapter(mFragments, supportFragmentManager)
        bottom_navigation_view_linear.setBadgeValue(0, "40")
        bottom_navigation_view_linear.setBadgeValue(1, null)
        bottom_navigation_view_linear.setBadgeValue(2, "7")
        bottom_navigation_view_linear.setBadgeValue(3, "2")
        bottom_navigation_view_linear.setBadgeValue(4, "")
        view_pager.adapter = mAdapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                bottom_navigation_view_linear.setCurrentActiveItem(i)
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
        bottom_navigation_view_linear.setNavigationChangeListener { _, position ->
            view_pager.setCurrentItem(position, true)
        }
    }
}