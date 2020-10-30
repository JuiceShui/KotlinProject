package com.demo.kotlin.base

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import com.demo.kotlin.App
import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.base.mvp.IPresenter
import com.demo.kotlin.base.view.ICreateInit
import com.demo.kotlin.base.view.ILoadingIndicator
import com.demo.kotlin.data.bus.AppEvent
import com.demo.kotlin.data.bus.AppEventType
import com.demo.kotlin.data.exception.ApiException
import com.demo.kotlin.inject.component.ActivityComponent
import com.demo.kotlin.inject.component.DaggerActivityComponent
import com.demo.kotlin.inject.module.ActivityModule
import com.demo.kotlin.utils.ToastUtil
import com.demo.kotlin.utils.bus.AppBus
import com.demo.kotlin.view.widget.AppBar
import com.demo.kotlin.view.widget.DefaultLoadingDialog
import com.demo.kotlin.view.widget.SlidingLayout
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), ICreateInit, ILoadView, Handler.Callback {
    // binding
    @Inject
    lateinit var mViewDataBinding: ViewDataBinding

    // Presenters
    protected val mPresenters: MutableList<IPresenter<ILoadView>?>? =
        ArrayList<IPresenter<ILoadView>?>()
    protected var isInit = false

    // Application实例
    private var mApp: BaseApplication? = null

    // Handler实例
    protected var mHandler: Handler? = null

    // activity引用
    protected var mActivity: Activity? = null

    // 管理RxJava任务
    protected var mCompositeDisposable: CompositeDisposable? = null

    // loading
    private var mLoadingDialog: ILoadingIndicator? = null

    // loading count
    private var mLoadingCount = 0

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        if (canSlidingClose()) {
            val layout = SlidingLayout(this)
            layout.bindActivity(this)
        }
        AppBus.register(this)
        mApp = BaseApplication.instance
        mApp?.addActivity(this)
        // inject
        initInject()
        mHandler = Handler(this)
        val presenter: IPresenter<ILoadView>? = getPresenter()
        if (presenter != null) {
            mPresenters!!.add(presenter)
        }
        val presenters: List<IPresenter<ILoadView>?>? = getExtraPresenters()
        if (presenters != null && presenters.isNotEmpty()) {
            mPresenters!!.addAll(presenters)
        }

        // data binding, this method will call setContentView()
        initBinding()
        // view created
        onViewCreated()
    }

    protected open fun setAppBar(appBar: AppBar) {
        appBar.setOnLeftClickListener(View.OnClickListener { backAction() })
    }

    protected open fun setAppBar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { backAction() }
    }

    /**
     * called after [.initBinding], before [.initParams]
     */
    protected open fun onViewCreated() {
        if (mPresenters != null && mPresenters.size > 0) {
            for (presenter in mPresenters) {
                presenter?.attachView(this)
                presenter?.onViewCreate()
            }
        }
        // init
        initParams()
        initViews()
        initListeners()
        getData()
        isInit = true
    }

    override fun initParams() {}

    override fun initViews() {}

    override fun initListeners() {}

    override fun getData() {}

    override fun <O> bindLoading(): ObservableTransformer<O, O>? {    //compose简化线程
        return object : ObservableTransformer<O, O> {
            private fun hideLoading() {
                safeRun { hideLoadingIndicator() }
            }

            override fun apply(observable: Observable<O>): Observable<O> {
                return observable.doOnSubscribe { safeRun { showLoadingIndicator() } }
                    .doOnComplete { hideLoading() }
                    .doOnError { hideLoading() }.doOnDispose { hideLoading() }
            }
        }
    }

    override fun showLoadingIndicator() {
        if (mLoadingDialog == null) {
            mLoadingDialog = getLoadingDialog()
        }
        if (mLoadingDialog == null) {
            // Use default loading dialog.
            mLoadingDialog = DefaultLoadingDialog(this)
        }
        mLoadingCount++
        if (mLoadingCount > 0) {
            mLoadingDialog?.showLoading()
        }
    }

    override fun hideLoadingIndicator() {
        if (mLoadingDialog == null) {
            return
        }
        mLoadingCount--
        if (mLoadingCount <= 0) {
            mLoadingDialog?.dismissLoading()
            mLoadingCount = 0
        }
    }

    /**
     * 添加到集合，统计管理
     */
    protected open fun addSubscribe(subscription: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(subscription)
    }

    /**
     * 私有方法
     * 取消所有订阅,onDestroy()时调用
     */
    protected open fun unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.dispose()
        }
    }


    override fun safeRun(action: Runnable) {
        mHandler!!.post(action)
    }

    /**
     * 返回上一页的响应动作
     */
    open fun backAction() {
        super.onBackPressed()
    }


    /**
     * 处理消息
     *
     * @param msg
     * @return 是否处理该消息
     */
    override fun handleMessage(msg: Message?): Boolean {
        return false
    }

    /**
     * 重写返回方法
     *//*
    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
    }*/

    override fun onResume() {
        super.onResume()
        if (mPresenters != null && mPresenters.size > 0) {
            for (presenter in mPresenters) {
                presenter?.onViewResume()
            }
        }
        mApp?.resumeActivity(this)
    }

    override fun onPause() {
        super.onPause()
        if (mPresenters != null && mPresenters.size > 0) {
            for (presenter in mPresenters) {
                presenter?.onViewPause()
            }
        }
    }

    override fun onDestroy() {
        AppBus.unregister(this)
        mApp?.removeActivity(this)
        if (mPresenters != null && mPresenters.size > 0) {
            for (presenter in mPresenters) {
                presenter?.let {
                    it.onViewDestroy()
                    it.detachView()
                }
            }
        }
        unSubscribe()
        super.onDestroy()
        mActivity = null
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
        }
//        mApp.removeActivity(this);
    }

    override fun showToast(@StringRes resId: Int) {
        ToastUtil.showToast(resId)
    }

    override fun showToast(toast: String?) {
        ToastUtil.showToast(toast)
    }

    override fun showLoadingView() {}

    override fun showLoadErrorView() {}

    override fun showMainView() {}

    protected fun getLoadingDialog(): ILoadingIndicator? {
        return null
    }

    protected abstract fun initInject()

    protected abstract fun initBinding()

    open protected fun getPresenter(): IPresenter<ILoadView>? {
        return null
    }

    protected fun getExtraPresenters(): List<IPresenter<ILoadView>?>? {
        return null
    }

    override fun handleException(throwable: Throwable?) {
        when (throwable) {
            !is ApiException -> {
                // 暂时没有写的
            }
            is ApiException -> {
                // 登录过期 或者第三方登录
                AppBus.post(AppEvent(AppEventType.LOGOUT))
                // 清空alias
                // 弹框吧 提示一次
                //showLogExpireDialog()
                showLoadErrorView()
            }
            else -> {
                showToast(throwable.message)
            }
        }
    }

    protected open fun getActivityComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
            .appComponent(App.getAppComponent())
            .activityModule(getActivityModule())
            .build()
    }

    protected open fun getActivityModule(): ActivityModule {
        return ActivityModule(this)
    }

    protected open fun canSlidingClose(): Boolean {
        return false
    }
}