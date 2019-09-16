package com.tebet.mojual.view.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.common.view.dialog.RoundedOkDialog
import co.sdk.auth.core.models.LoginException
import com.google.firebase.messaging.RemoteMessage
import com.tapadoo.alerter.Alerter
import com.tebet.mojual.App
import com.tebet.mojual.R
import com.tebet.mojual.ViewModelProviderFactory
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.ActivityBaseBinding
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_base.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseFragment.Callback, BaseActivityNavigator {
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

    protected lateinit var baseBinding: ActivityBaseBinding

    @Inject
    lateinit var factory: ViewModelProviderFactory

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    open var enableBackButton: Boolean = true
        set(isEnable) {
            field = isEnable
            when {
                isEnable -> {
                    baseBinding.ivBack.visibility = View.VISIBLE
                    baseBinding.topLeftHolder.visibility = View.GONE
                }
                else -> {
                    baseBinding.ivBack.visibility = View.GONE
                    baseBinding.topLeftHolder.visibility = View.VISIBLE
                }
            }
        }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        baseBinding.viewModel =
            ViewModelProviders.of(this, factory).get(BaseActivityViewModel::class.java)
        baseBinding.viewModel?.navigator = this
        performDataBinding()
        mViewModel?.baseErrorHandlerData?.observe(this, Observer<String> {
            handleError(it)
        })
        setSupportActionBar(baseBinding.baseToolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        (application as App).notificationHandlerData.observe(this, Observer { remoteMessage ->
            remoteMessage?.first?.let {
                val alert = Alerter.create(this)
                    .setIcon(R.drawable.logosmall)
                    .setBackgroundColorRes(R.color.green_dark) // or setBackgroundColorInt(Color.CYAN)
                    .setText(it.body.toString())
                    .setTextAppearance(android.R.style.TextAppearance_Large_Inverse)
                    .setDuration(5000)
                    .enableSwipeToDismiss()

                remoteMessage.second?.let { extraData ->
                    extraData["orderId"]?.let { orderId ->
                        alert.addButton("View", R.style.AlertButton, View.OnClickListener {
                            openFromNotification(extraData)
                        }).setOnClickListener(View.OnClickListener {
                            openFromNotification(extraData)
                        })
                    }
                }
                alert.show()
                (application as App).notificationHandlerData.postValue(null)
            }
        })
        onCreateBase(savedInstanceState, contentLayoutId)
    }

    open fun showOrderDetailScreen(dataResponse: Order) {}

    protected fun openFromNotification(extras: Map<String, String?>): Boolean {
        extras["orderId"]?.let {
            showOrderDetailScreen(Order(orderId = it.toLong()))
            return true
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    //    public boolean isNetworkConnected() {
    //        return NetworkUtils.isNetworkConnected(getApplicationContext());
    //    }


    fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    private fun performDataBinding() {
        viewDataBinding =
            DataBindingUtil.inflate(layoutInflater, contentLayoutId, baseBinding.placeHolder, true)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding?.setVariable(bindingVariable, mViewModel)
        viewDataBinding?.executePendingBindings()
    }

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    protected abstract val contentLayoutId: Int

    fun fullScreenChildHolder(isFullScreen: Boolean) {
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                (baseBinding.placeHolder.layoutParams as RelativeLayout.LayoutParams).removeRule(
                    RelativeLayout.BELOW
                )
            } else {
                (baseBinding.placeHolder.layoutParams as RelativeLayout.LayoutParams).addRule(
                    RelativeLayout.BELOW,
                    0
                )
            }
        } else {
            (baseBinding.placeHolder.layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.BELOW,
                R.id.baseToolbar
            )
        }
    }

    fun showNotification(isShow: Boolean) {
        if (isShow) {
            rlHomeNotificationContainer.visibility = View.VISIBLE
        } else {
            rlHomeNotificationContainer.visibility = View.GONE
        }
    }

    override fun showLoading(isLoading: Boolean) {
        viewModel.setIsLoading(isLoading)
        baseBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun handleError(exeption: LoginException?) {
        exeption?.errorMessage?.let { show(it) }
    }

    fun handleError(exception: Throwable) {
        exception.let { it.message?.let { it1 -> show(it1) } }
    }

    override fun setTitle(titleId: Int) {
        super.setTitle("")
        baseBinding.tvBaseTitle.text = getString(titleId).toString().toUpperCase()
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("")
        baseBinding.tvBaseTitle.text = title
    }

    protected abstract fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int)

    override fun onBackPressed() {
        showLoading(false)
        hideKeyboard()
        super.onBackPressed()
    }

    open fun openFragment(fragment: Fragment, placeHolder: Int, tag: String = "") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !isDestroyed || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(placeHolder, fragment, tag)
            transaction.commitAllowingStateLoss()
        }
    }

    @SuppressLint("CommitTransaction")
    open fun openFragmentSlideRight(fragment: Fragment, placeHolder: Int, backStackTag : String? = null) {
        run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !isDestroyed || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val manager = supportFragmentManager
                manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(placeHolder, fragment, backStackTag).addToBackStack(backStackTag).commitAllowingStateLoss()
            }
        }
    }

    open fun addFragment(fragment: Fragment, placeHolder: Int, tag: String = "") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !isDestroyed || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(placeHolder, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

    protected fun setActionBarColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    fun isLowEndDevice(): Boolean {
        val memInfo = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE)
        if (activityManager is ActivityManager) {
            activityManager.getMemoryInfo(memInfo)
            val totalMemory = memInfo.totalMem / 0x100000L
            return totalMemory < 1024 || Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP
        }
        return true
    }

    fun handleError(error: String?) {
        error?.let { show(it) }
    }

    override fun activity(): Activity {
        return this
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun finish() {
        super.finish()
    }

    override fun show(message: String) {
        RoundedOkDialog(message).show(supportFragmentManager, message)
    }

    override fun show(messageResId: Int) {
        RoundedOkDialog(getString(messageResId)).show(supportFragmentManager, getString(messageResId))
    }
}

