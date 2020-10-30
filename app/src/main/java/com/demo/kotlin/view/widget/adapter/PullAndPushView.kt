package com.demo.kotlin.view.widget.adapter

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demo.kotlin.R
import com.demo.kotlin.utils.ToastUtil
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import io.reactivex.rxjava3.core.Observable

class PullAndPushView<T>(private val mContext: Context, attrs: AttributeSet?) :
    LinearLayout(mContext, attrs) {
    private var mDataList: MutableList<T>? = null
    private var mPageSize = 10

    // 这个是记录我们的offfset
    private var mOffset = 0

    // 这个是我们最开始的数值
    private var mOff = 0
    private var hasMoreData = true

    /**
     * 设置相关参数时调用
     */
    var swipeRefreshLayout: SwipeRefreshLayout? = null
        private set

    /**
     * 如果外部需要使用 RecycleView 我们可以通过这个方法获取得到
     */
    var recyclerView: RecyclerView? = null
        private set
    private var mFooterContainer: LinearLayout? = null
    private var mNoDataContainer: FrameLayout? = null
    private var mFooterLoadView: View? = null
    private var mFooterNoDataView: View? = null
    private var mDisposable: CompositeDisposable?

    // 上拉加载需要的View
    private var mLastVisibleItem = 0
    private var mHeaderAndFooterWrapperZhy: HeaderAndFooterWrapper? = null
    private var mOnScroller: OnScroller? = null
    private fun init() {
        inflate(mContext, R.layout.widget_pull_and_push, this)
        swipeRefreshLayout = findViewById<View>(R.id.pull_and_push_srl) as SwipeRefreshLayout
        recyclerView = findViewById<View>(R.id.pull_and_push_rv) as RecyclerView
        recyclerView!!.itemAnimator!!.changeDuration = 0
        mNoDataContainer =
            findViewById<View>(R.id.pull_and_push_fl_no_data_container) as FrameLayout
        mNoDataContainer!!.visibility = GONE
        mFooterContainer = LinearLayout(mContext)
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mFooterContainer!!.layoutParams = lp
        mFooterContainer!!.gravity = Gravity.CENTER
        (recyclerView!!.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
        //        mFootView = LayoutInflater.from(mContext).inflate(R.layout.widght_pull_and_push_footer, null);
        swipeRefreshLayout!!.isEnabled = false
        setLoadMoreStyle(null, null, null)
    }

    fun setLoadParams(pageSize: Int, offset: Int): PullAndPushView<T> {
        mOff = offset
        mOffset = offset
        mPageSize = pageSize
        return this
    }

    /**
     * 返回数据源 设置innerAdapter 的时候需要使用此数据源
     */
    val dataSource: List<T>
        get() {
            if (mDataList == null) {
                mDataList = ArrayList()
            }
            return mDataList!!
        }

    /**
     * 目前手工调用
     */
    fun setData(onGetData: OnGetData<T>?): PullAndPushView<T> {
        mGetData = onGetData
        return this
    }

    private var mGetData: OnGetData<T>? = null

    /**
     * 下拉刷新设置
     *
     * @return
     */
    fun addRefreshEvent(): PullAndPushView<T> {
        setRefreshStyle()
        swipeRefreshLayout!!.setOnRefreshListener { getRefreshData(false) }
        return this
    }

    /**
     *
     */
    fun initData() {
        (mContext as Activity).runOnUiThread { swipeRefreshLayout!!.isRefreshing = true }
        getRefreshData(true)
    }

    /**
     *
     */
    fun initData(isInit: Boolean) {
        (mContext as Activity).runOnUiThread { swipeRefreshLayout!!.isRefreshing = true }
        getRefreshData(isInit)
    }

    /**
     *
     */
    fun initData(needRefresh: Boolean, offset: Int, pagesize: Int) {
        (mContext as Activity).runOnUiThread {
            if (needRefresh) {
                swipeRefreshLayout!!.isRefreshing = true
            }
        }
        getRefreshData(true, offset, pagesize)
    }

    fun addLoadMoreEvent(headerAndFooterWrapper: HeaderAndFooterWrapper): PullAndPushView<T> {
        return addLoadMoreEvent(headerAndFooterWrapper, null)
    }

    fun addLoadMoreEvent(
        headerAndFooterWrapper: HeaderAndFooterWrapper,
        linearLayoutManager: RecyclerView.LayoutManager?
    ): PullAndPushView<T> {
        var linearLayoutManager = linearLayoutManager
        mHeaderAndFooterWrapperZhy = headerAndFooterWrapper
        mHeaderAndFooterWrapperZhy!!.addFootView(mFooterContainer)
        if (linearLayoutManager == null) {
            linearLayoutManager = LinearLayoutManager(mContext)
        }
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = mHeaderAndFooterWrapperZhy
        val finalLinearLayoutManager: RecyclerView.LayoutManager = linearLayoutManager
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItem + 1 == headerAndFooterWrapper.itemCount
                ) {
                    if (mGetData != null) {
                        if (!hasMoreData) {
                            // 没有数据，直接更新View
                            return
                        }
                        mOffset += mPageSize
                        val observable: Observable<List<T>> =
                            mGetData!!.getData(mPageSize, mOffset, mDataList)
                        mDisposable?.add(
                            observable.subscribeOn(Schedulers.io()) //                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Consumer<List<T>?> {
                                    @Throws(Exception::class)
                                    override fun accept(@NonNull t: List<T>?) {
                                        if (mDataList == null) {
                                            throw RuntimeException(
                                                "dataSource of recycleView did not instance;" +
                                                        "please use constructor of adapter with getDataSource()"
                                            )
                                        }
                                        hasMoreData = false
                                        if (t == null || t.size == 0) {
                                            // 没有数据了 更新View
                                            updateView(false)
                                            return
                                        }
                                        mDataList!!.addAll(t)
                                        if (t.size == mPageSize) {
                                            // 还有更多的数据
                                            hasMoreData = true
                                            updateView(true)
                                            return
                                        }
                                        // 更新View
                                        updateView(false)
                                    }
                                }, object : Consumer<Throwable?> {
                                    @Throws(Exception::class)
                                    override fun accept(@NonNull throwable: Throwable?) {
                                        // 发生错误
                                        Log.e("PullAndPushView", throwable?.message)
                                        updateView(false)
                                        if (mDataErrListener != null) {
                                            (mContext as Activity).runOnUiThread {
                                                ToastUtil.showToast(R.string.all_network_error)
                                                mDataErrListener!!.onFirstGetDataErr(throwable)
                                            }
                                        }
                                    }
                                })
                        )
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (finalLinearLayoutManager is GridLayoutManager) {
                    mLastVisibleItem = finalLinearLayoutManager.findLastVisibleItemPosition()
                } else if (finalLinearLayoutManager is LinearLayoutManager) {
                    mLastVisibleItem = finalLinearLayoutManager.findLastVisibleItemPosition()
                }
                if (mOnScroller != null) {
                    // recycle 的滑动监听
                    mOnScroller!!.onScroller()
                }
            }
        })
        return this
    }

    /**
     * 添加分割线
     */
    fun addItemDecoration(itemDecoration: ItemDecoration?): PullAndPushView<*> {
        recyclerView!!.addItemDecoration(itemDecoration!!)
        return this
    }

    fun setScrollerListener(listener: OnScroller?) {
        mOnScroller = listener
    }

    /**
     * 取消之前的 请求
     */
    fun cancelRequest() {
        if (mDisposable != null) {
            // 刷新的时候切断水管
            mDisposable?.dispose()
        }
        mDisposable = CompositeDisposable()
    }

    private fun setRefreshStyle() {
        swipeRefreshLayout!!.isEnabled = true
        swipeRefreshLayout!!.setSize(SwipeRefreshLayout.DEFAULT) // 设置圆圈的大小
    }

    /**
     * 获取刷新数据的data
     */
    private fun getRefreshData(isInit: Boolean) {
        if (mGetData != null) {
            mOffset = mOff
            // 切断之前的连接，重新新建
            cancelRequest()
            val observable: Observable<List<T>> = mGetData!!.getData(mPageSize, mOffset, mDataList)
            mDisposable?.add(
                observable.subscribeOn(Schedulers.io()) //                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Consumer<List<T>?> {
                        @Throws(Exception::class)
                        override fun accept(@NonNull ts: List<T>?) {
                            if (mDataList == null) {
                                throw RuntimeException(
                                    "dataSource of recycleView did not instance;" +
                                            "please use constructor of adapter with getDataSource()"
                                )
                            }
                            //                            if (mDataErrListener != null) {
//                                mDataErrListener.onFirstGetDataErr();
//                            }
                            if (mOnRefreshData != null) {
                                mOnRefreshData!!.onRefreshData(ts, mDataList, isInit)
                                //                                updateView(hasMoreData);
                                return
                            }
                            hasMoreData = false
                            mDataList!!.clear()
                            if (ts == null || ts.size == 0) {
                                // 没有数据了 更新View
                                updateView(false)
                                return
                            }
                            mDataList!!.addAll(ts)
                            if (ts.size == mPageSize) {
                                // 还有更多的数据
                                hasMoreData = true
                                updateView(true)
                                return
                            }
                            // 更新View
                            updateView(false)
                        }
                    }, { throwable -> // 发生错误
                        Log.e("PullAndPushView", throwable?.message)
                        //                            // 清除数据
                        mDataList!!.clear()
                        updateView(false)
                        if (mDataErrListener != null) {
                            (mContext as Activity).runOnUiThread {
                                ToastUtil.showToast(R.string.all_network_error)
                                mDataErrListener!!.onFirstGetDataErr(throwable)
                            }
                        }
                    })
            )
        }
    }

    /**
     * 获取刷新数据的data
     */
    private fun getRefreshData(isInit: Boolean, offset: Int, pagesize: Int) {
        if (mGetData != null) {
            mOffset = mOff
            // 切断之前的连接，重新新建
            cancelRequest()
            val observable: Observable<List<T>> = mGetData!!.getData(pagesize, offset, mDataList)
            mDisposable?.add(
                observable.subscribeOn(Schedulers.io()) //                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Consumer<List<T>?> {
                        @Throws(Exception::class)
                        override fun accept(@NonNull ts: List<T>?) {
                            if (mDataList == null) {
                                throw RuntimeException(
                                    "dataSource of recycleView did not instance;" +
                                            "please use constructor of adapter with getDataSource()"
                                )
                            }
                            //                            if (mDataErrListener != null) {
//                                mDataErrListener.onFirstGetDataErr(throwable);
//                            }
                            if (mOnRefreshData != null) {
                                mOnRefreshData!!.onRefreshData(ts, mDataList, isInit)
                                //                                updateView(hasMoreData);
                                return
                            }
                            hasMoreData = false
                            mDataList!!.clear()
                            if (ts == null || ts.size == 0) {
                                // 没有数据了 更新View
                                updateView(false)
                                return
                            }
                            mDataList!!.addAll(ts)
                            if (ts.size == pagesize) {
                                // 还有更多的数据
                                hasMoreData = true
                                updateView(true)
                                return
                            }
                            // 更新View
                            updateView(false)
                        }
                    }, { throwable -> // 发生错误
                        Log.e("PullAndPushView", throwable?.message)
                        // 清除数据
                        mDataList!!.clear()
                        updateView(false)
                        if (mDataErrListener != null) {
                            (mContext as Activity).runOnUiThread {
                                ToastUtil.showToast(R.string.all_network_error)
                                mDataErrListener!!.onFirstGetDataErr(throwable)
                            }
                        }
                    })
            )
        }
    }

    fun setOnGetDataErr(onFirstGetData: OnGetDataErr?) {
        mDataErrListener = onFirstGetData
    }

    private var mDataErrListener: OnGetDataErr? = null

    /**
     * 第一次获取完数据
     */
    interface OnGetDataErr {
        fun onFirstGetDataErr(throwable: Throwable?)
    }

    /**
     * 设置footer 的样式
     */
    fun setLoadMoreStyle(noDateView: View?, footerLoadView: View?, footerNoDataView: View?) {
        val mNoDataView: View
        if (noDateView == null) {
            val textView = TextView(mContext)
            //            textView.setText("暂无数据");
            textView.textSize = 16f
            textView.gravity = Gravity.CENTER
            //            textView.setBackgroundColor(0XFFF2F2F2);
            mNoDataView = textView
        } else {
            mNoDataView = noDateView
        }
        mNoDataContainer!!.removeAllViews()
        mNoDataContainer!!.addView(mNoDataView)
        mFooterLoadView = footerLoadView
            ?: inflate(mContext, R.layout.widght_pull_and_push_footer_loading, null)
        mFooterNoDataView = if (footerLoadView == null) {
            inflate(mContext, R.layout.widght_pull_and_push_footer_no_data, null)
        } else {
            footerNoDataView
        }
    }

    /**
     * 仅仅只是刷新数据
     */
    fun onlyUpdateViewWithData() {
        if (mHeaderAndFooterWrapperZhy != null) {
            mHeaderAndFooterWrapperZhy!!.notifyDataSetChanged()
        }
    }

    /**
     * 仅仅只是刷新数据
     */
    fun notifyItemChanged(position: Int) {
        if (mHeaderAndFooterWrapperZhy != null) {
            mHeaderAndFooterWrapperZhy!!.notifyItemChanged(position)
        }
    }

    private fun updateCache() {
        if (mHeaderAndFooterWrapperZhy == null) {
            return
        }
        for (i in mDataList!!.indices) {
            mHeaderAndFooterWrapperZhy!!.notifyItemChanged(i)
        }
    }

    /**
     * 更新View  只有在上拉加载时需要调用
     * 外部最好不要调用，后期会关闭此方法为private
     */
    fun updateView(hasMoreData: Boolean) {
        (mContext as Activity).runOnUiThread {
            this@PullAndPushView.hasMoreData = hasMoreData
            //        // flag： 表明有无数据
            //        boolean hasData = mDataList.size() != 0;
            //  这里后面删除
            if (mHeaderAndFooterWrapperZhy != null) {
                mHeaderAndFooterWrapperZhy!!.notifyDataSetChanged()
            }
            if (swipeRefreshLayout!!.isEnabled || swipeRefreshLayout!!.isRefreshing) {
                swipeRefreshLayout!!.isRefreshing = false
            }

            // 因为我们加了一个 footer 所以需要减一
            if (mHeaderAndFooterWrapperZhy != null && mHeaderAndFooterWrapperZhy!!.itemCount > 1) {
                recyclerView!!.visibility = VISIBLE
                mNoDataContainer!!.visibility = GONE
            } else {
                recyclerView!!.visibility = GONE
                mNoDataContainer!!.visibility = VISIBLE
            }

            // 第一次加载数据 不需要footer
            if (hasMoreData) {
                mFooterContainer!!.removeAllViews()
                mFooterContainer!!.addView(mFooterLoadView)
            } else {
                mFooterContainer!!.removeAllViews()
                mFooterContainer!!.addView(mFooterNoDataView)
            }
        }
    }

    /**
     * 自己设置刷新数据后的处理，打断默认的处理
     */
    private var mOnRefreshData: OnRefreshDataListener<T>? = null

    interface OnRefreshDataListener<T> {
        /**
         * @return 是否还有更多的数据
         */
        fun onRefreshData(newDataList: List<T>?, dataSource: List<T>?, isInit: Boolean)
    }

    /**
     * 自定义刷新
     *
     * @param onRefreshData
     * @return
     */
    fun setOnRefreshData(onRefreshData: OnRefreshDataListener<T>?): PullAndPushView<T> {
        mOnRefreshData = onRefreshData
        return this
    }

    /**
     * 获取数据
     *
     * @param <T>
    </T> */
    interface OnGetData<T> {
        fun getData(pageSize: Int, offset: Int, dataList: List<T>?): Observable<List<T>>
    }

    /**
     * 这两个接口中执行异步的方法
     */
    interface OnScroller {
        fun onScroller()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
    }

    init {
        mDisposable = CompositeDisposable()
        init()
    }
}