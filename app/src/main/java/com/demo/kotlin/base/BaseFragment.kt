package com.demo.kotlin.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.demo.kotlin.App
import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.base.mvp.IPresenter
import com.demo.kotlin.base.view.ICreateInit
import com.demo.kotlin.inject.component.DaggerFragmentComponent
import com.demo.kotlin.inject.component.FragmentComponent
import com.demo.kotlin.inject.module.FragmentModule
import com.demo.kotlin.utils.ToastUtil
import com.demo.kotlin.utils.bus.AppBus
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import javax.inject.Inject


/**
 * Created by adu on 2017/5/2.
 */
abstract class BaseFragment : Fragment(), ILoadView, ICreateInit,
    Handler.Callback {
    @Inject
    @JvmField
    protected var mViewDataBinding: ViewDataBinding? = null
    private lateinit var mApp: App

    // Presenters
    private val mPresenters: MutableList<IPresenter<ILoadView>?>? =
        ArrayList<IPresenter<ILoadView>?>()

    // Handler实例
    protected var mHandler: Handler? = null

    // 保存的activity引用
    protected var mActivity: BaseActivity? = null

    // 是否开启懒加载
    protected var mLazyInitEnabled = false

    // 是否初始化标志
    protected var mInit = false

    // 管理RxJava任务
    private var mCompositeDisposable: CompositeDisposable? = null

    protected var mFitSystemBar = false
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity as BaseActivity?
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mApp = App.instance!!
        AppBus.register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inject
        initInject()
        mHandler = Handler(this)
        // 初始化Presenter
        val presenter: IPresenter<ILoadView>? = getPresenter()
        if (presenter != null) {
            mPresenters!!.add(presenter)
        }
        val presenters: List<IPresenter<ILoadView>?>? = getExtraPresenters()
        if (presenters != null && presenters.isNotEmpty()) {
            mPresenters!!.addAll(presenters)
        }
        // data binding
        return initBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 给Presenter绑定View
        if (mPresenters != null && mPresenters.size > 0) {
            for (p in mPresenters) {
                p?.let {
                    it.attachView(this)
                    it.onViewCreate()
                }

            }
        }
        if (mFitSystemBar) {
            val statusBarHeight = ImmersionBar.getStatusBarHeight(this)
            mViewDataBinding?.root?.setPadding(0, statusBarHeight, 0, 0)
        }
        if (!mLazyInitEnabled) {
            initParams()
            initViews()
            initListeners()
            getData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !mInit) {
            // onLazyInitView(null)
            mInit = true
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && !mInit) {
            // onLazyInitView(null)
            mInit = true
        }
    }

    /**
     * 懒加载初始化
     *
     * @param savedInstanceState
     */
    fun onLazyInitView(savedInstanceState: Bundle?) {
        if (mLazyInitEnabled) {
            initParams()
            initViews()
            initListeners()
        }
        getData()
        mInit = true
    }

    override fun initParams() {}
    override fun initViews() {}

    //    /**
    //     * 设置字体格式
    //     */
    //    public void setTypeface(Typeface tfBlack, Typeface tfBold, Typeface tfExtraBold, Typeface tfLight, Typeface tfMedium, Typeface tfRegular, Typeface tfThin) {
    //
    //    }
    override fun initListeners() {}
    override fun getData() {
    }

    override fun <O> bindLoading(): ObservableTransformer<O, O>? {
        return mActivity?.bindLoading()
    }

    override fun showLoadingIndicator() {
        mHandler?.post { mActivity?.showLoadingIndicator() }
    }

    override fun hideLoadingIndicator() {
        mHandler?.post { mActivity?.hideLoadingIndicator() }
    }

    /**
     * 添加到集合，统计管理
     */
    protected fun addSubscribe(subscription: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(subscription)
    }

    /**
     * 私有方法
     * 取消所有订阅,onDestroy()时调用
     */
    private fun unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.dispose()
        }
    }

    /**
     * 处理消息
     *
     * @param msg
     * @return 是否处理该消息
     */
    override fun handleMessage(msg: Message): Boolean {
        return false
    }

    override fun showToast(@StringRes resId: Int) {
        ToastUtil.showToast(resId)
    }

    override fun showToast(toast: String?) {
        ToastUtil.showToast(toast)
    }

    override fun safeRun(action: Runnable) {
        mHandler!!.post(action)
    }

    override fun onResume() {
        super.onResume()
        if (mPresenters != null && mPresenters.size > 0) {
            for (presenter in mPresenters) {
                presenter?.onViewResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mPresenters != null && mPresenters.size > 0) {
            for (presenter in mPresenters) {
                presenter?.onViewPause()
            }
        }
    }

    override fun onDetach() {
        AppBus.unregister(this)
        super.onDetach()
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
        }
        mActivity = null
    }

    override fun onDestroyView() {
        mLazyInitEnabled = false
        if (mPresenters != null && mPresenters.size > 0) {
            for (presenter in mPresenters) {
                presenter?.let {
                    it.onViewDestroy()
                    it.detachView()
                }

            }
        }
        unSubscribe()
        super.onDestroyView()
    }

    override fun showLoadingView() {}
    override fun showLoadErrorView() {}
    override fun showMainView() {}
    protected abstract fun initInject()
    protected abstract fun initBinding(): View?
    protected open fun getPresenter(): IPresenter<ILoadView>? {
        return null
    }

    protected fun getExtraPresenters(): List<IPresenter<ILoadView>?>? {
        return null
    }

    protected open fun getFragmentComponent(): FragmentComponent {
        return DaggerFragmentComponent.builder()
            .appComponent(App.mAppComponent)
            .fragmentModule(getFragmentModule())
            .build()
    }

    protected open fun getFragmentModule(): FragmentModule {
        return FragmentModule(this)
    }

    override fun handleException(throwable: Throwable?) {

    }

    protected open fun fitSystemBar(fit: Boolean) {
        this.mFitSystemBar = fit
    }
}