package com.tebet.mojual.view.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tebet.mojual.ViewModelProviderFactory
import com.tebet.mojual.data.models.NetworkError
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment(), BaseActivityNavigator {
    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

    @Inject
    lateinit var factory: ViewModelProviderFactory

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
                if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity
            activity?.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        mViewModel?.baseErrorHandlerData?.observe(this, Observer<NetworkError> {
            (activity as BaseActivity<*, *>).handleError(it)
        })
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = viewDataBinding!!.root
        return mRootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.lifecycleOwner = this
        viewDataBinding!!.executePendingBindings()
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    //    public boolean isNetworkConnected() {
    //        return mActivity != null && mActivity.isNetworkConnected();
    //    }
    //
    //    public void openActivityOnTokenExpire() {
    //        if (mActivity != null) {
    //            mActivity.openActivityOnTokenExpire();
    //        }
    //    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    override fun showLoading(isLoading: Boolean) {
        viewModel.setIsLoading(isLoading)
        (activity() as BaseActivity<*, *>).showLoading(isLoading)
    }

    override fun showEmpty(isEmpty: Boolean) {
        viewModel.setIsEmpty(isEmpty)
        (activity() as BaseActivity<*, *>).showEmpty(isEmpty)
    }

    override fun activity(): Activity? {
        return baseActivity
    }

    override fun finish() {
        activity?.finish()
    }

    override fun show(message: String) {
        (activity() as BaseActivity<*, *>).show(message)
    }

    override fun show(messageResId: Int) {
        (activity() as BaseActivity<*, *>).show(messageResId)
    }

    override fun onBackPressed() {
        (activity() as BaseActivity<*, *>).onBackPressed()
    }
}
