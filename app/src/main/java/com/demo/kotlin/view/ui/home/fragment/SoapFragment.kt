package com.demo.kotlin.view.ui.home.fragment

import android.view.View
import com.demo.kotlin.R
import com.demo.kotlin.base.BaseFragment
import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.base.mvp.IPresenter
import com.demo.kotlin.data.entity.SoapEntity
import com.demo.kotlin.databinding.FragmentSoapBinding
import com.demo.kotlin.view.ui.home.contract.SoapContract
import com.demo.kotlin.view.ui.home.presenter.SoapPresenter
import com.demo.kotlin.view.widget.explosion.ExplosionField
import javax.inject.Inject

class SoapFragment : BaseFragment(), SoapContract.View, View.OnClickListener {
    private lateinit var mBinding: FragmentSoapBinding

    @Inject
    lateinit var mPresenter: SoapPresenter
    private lateinit var mExplodeField: ExplosionField
    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    override fun initBinding(): View? {
        mBinding = mViewDataBinding as FragmentSoapBinding
        mBinding.click = this
        return mBinding.root
    }

    override fun initViews() {
        mBinding.root.setBackgroundColor(resources.getColor(R.color.orange_active))
        mExplodeField = ExplosionField.attach2Window(mActivity!!)
    }

    override val layoutId: Int
        get() = R.layout.fragment_soap

    override fun getPresenter(): IPresenter<ILoadView>? {
        return mPresenter as IPresenter<ILoadView>
    }

    override fun getData() {
        mPresenter.getSoapData()
    }

    override fun onGetSoap(data: SoapEntity) {
        mBinding.tvSoap.text = data.content
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_soap -> {
                mExplodeField.explode(v)
            }
        }
    }
}