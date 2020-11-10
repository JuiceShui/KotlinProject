package com.demo.kotlin.view.ui.home.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.demo.kotlin.R
import com.demo.kotlin.base.BaseFragment
import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.base.mvp.IPresenter
import com.demo.kotlin.data.entity.TXDouYinVideoEntity
import com.demo.kotlin.databinding.FragmentHomeBinding
import com.demo.kotlin.view.ui.home.adapter.HomeAdapter
import com.demo.kotlin.view.ui.home.adapter.HomeVideoAdapter
import com.demo.kotlin.view.ui.home.contract.HomeContract
import com.demo.kotlin.view.ui.home.presenter.HomePresenter
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import javax.inject.Inject

class HomeFragment(val text: String) : BaseFragment(), View.OnClickListener, HomeContract.View {

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mBannerAdapter: HomeAdapter
    private lateinit var mVideoAdapter: HomeVideoAdapter
    private lateinit var mVideoData: ArrayList<TXDouYinVideoEntity>
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mPresenter: HomePresenter
    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    override fun initBinding(): View? {
        mBinding = mViewDataBinding as FragmentHomeBinding
        mBinding.click = this
        return mBinding.root
    }


    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onClick(v: View?) {

    }

    override fun getPresenter(): IPresenter<ILoadView>? {
        return mPresenter as IPresenter<ILoadView>
    }

    override fun getData() {
        mPresenter.getHomeData()
    }

    override fun initViews() {
        mVideoData = ArrayList<TXDouYinVideoEntity>()
        mVideoAdapter = HomeVideoAdapter(mVideoData, mActivity!!)
        mLinearLayoutManager = LinearLayoutManager(mActivity)
        mBinding.homeRecycler.layoutManager = mLinearLayoutManager
        mBinding.homeRecycler.adapter = mVideoAdapter
        mBinding.banner.apply {
            mBannerAdapter = HomeAdapter()
            setAutoPlay(true)
            setLifecycleRegistry(lifecycle)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setIndicatorSliderGap(resources.getDimensionPixelOffset(R.dimen.dp_4))
            setIndicatorMargin(0, 0, 0, resources.getDimension(R.dimen.dp_100).toInt())
            setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
            setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_35))
            setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            setIndicatorSliderRadius(
                resources.getDimension(R.dimen.dp_3).toInt(),
                resources.getDimension(R.dimen.dp_4_5).toInt()
            )
            mActivity?.let {
                setIndicatorSliderColor(
                    ContextCompat.getColor(it, R.color.white),
                    ContextCompat.getColor(it, R.color.half_transport_white)
                )
            }
            setAdapter(mBannerAdapter)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    BannerUtils.log("position:$position")
                }
            })
        }.create()
    }

    override fun initListeners() {
        mBinding.homeRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition()
                mBinding.blur.visibility =
                    if (currentVisiblePosition == 0) View.GONE else View.VISIBLE
            }
        })
    }

    override fun onGetBanner(data: MutableList<TXDouYinVideoEntity>) {
        mBinding.banner.refreshData(data)
    }

    override fun onGetHomeList(data: MutableList<TXDouYinVideoEntity>) {
        mVideoData.clear()
        mVideoData.addAll(data)
        mVideoAdapter.notifyDataSetChanged()
    }
}