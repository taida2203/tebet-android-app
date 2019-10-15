package com.tebet.mojual.view.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.common.constant.AppConstant
import co.common.util.LanguageUtil
import co.common.util.PreferenceUtils
import co.common.view.dialog.RoundedOkDialog
import co.sdk.auth.core.models.LoginException
import com.tapadoo.alerter.Alerter
import com.tebet.mojual.App
import com.tebet.mojual.R
import com.tebet.mojual.ViewModelProviderFactory
import com.tebet.mojual.common.services.ScreenListenerService
import com.tebet.mojual.data.models.Message
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.request.MessageRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.databinding.ActivityBaseBinding
import com.tebet.mojual.view.help.QualityHelp
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.login.Login
import com.tebet.mojual.view.profile.ProfileViewModel
import com.tebet.mojual.view.profilepin.PinCode
import com.tebet.mojual.view.qualitydetail.OrderDetailActivity
import com.tebet.mojual.view.signup.SignUpInfo
import com.tebet.mojual.view.signup.step0.SignUpPassword
import com.tebet.mojual.view.splash.Splash
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseFragment.Callback, BaseActivityNavigator {
    var viewDataBinding: T? = null
        private set

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

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    protected abstract val contentLayoutId: Int

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

    private var mReceiver: ScreenReceiver? = null
    protected var forceLock: Boolean = PreferenceUtils.getBoolean(EXTRA_FORCE_LOCK, false)

    companion object {
        const val REQUEST_CODE_PIN = 1991
        const val EXTRA_FORCE_LOCK = "EXTRA_FORCE_LOCK"
        const val EXTRA_FORCE_REFRESH = "EXTRA_FORCE_REFRESH"
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageUtil.instance.updateLanguage(this)
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        baseBinding.viewModel =
            ViewModelProviders.of(this, factory).get(BaseActivityViewModel::class.java)
        baseBinding.viewModel?.navigator = this
        performDataBinding()
        viewModel.baseErrorHandlerData.observe(this, Observer<NetworkError> {
            handleError(it)
        })
        setSupportActionBar(baseBinding.baseToolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        (application as App).notificationHandlerData.observe(this, Observer { remoteMessage ->
            remoteMessage?.let { message ->
                val alert = Alerter.create(this)
                    .setIcon(R.drawable.logosmall)
                    .setBackgroundColorRes(R.color.green_dark) // or setBackgroundColorInt(Color.CYAN)
                    .setText(message.message.toString())
                    .setTextAppearance(android.R.style.TextAppearance_Large_Inverse)
                    .setDuration(5000)
                    .enableSwipeToDismiss()
                if (!message.data?.get("orderId").isNullOrBlank()) {
                    alert.addButton("View", R.style.AlertButton, View.OnClickListener {
                        message.data?.let { it1 -> openFromNotification(it1, true) }
                        Alerter.hide()
                    }).setOnClickListener(View.OnClickListener {
                        message.data?.let { it1 -> openFromNotification(it1, true) }
                        Alerter.hide()
                    })
                }
                alert.show()
                refreshData(message)
                (application as App).notificationHandlerData.postValue(null)
            }
        })

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        mReceiver = ScreenReceiver()
        try {
            registerReceiver(mReceiver, filter)
        } catch (e: Exception) {
            Timber.d(e)
        }
        if (!isMyServiceRunning(ScreenListenerService::class.java)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                try {
                    val i = Intent(applicationContext, ScreenListenerService::class.java)
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> applicationContext.startForegroundService(
                            i
                        ) // https://www.fabric.io/masbro/android/apps/co.masbro.consumer/issues/5ac742c036c7b23527af337b?time=last-seven-days
                        else -> applicationContext.startService(i)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

        if (intent != null) forceLock = intent.getBooleanExtra(EXTRA_FORCE_LOCK, false)

        onCreateBase(savedInstanceState, contentLayoutId)
    }

    fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    private fun performDataBinding() {
        viewDataBinding =
            DataBindingUtil.inflate(layoutInflater, contentLayoutId, baseBinding.placeHolder, true)
        viewDataBinding?.setVariable(bindingVariable, viewModel)
        viewDataBinding?.executePendingBindings()
    }

    protected abstract fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int)

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    protected open fun refreshData(notification: Message) {}

    open fun showCheckQualityScreen() {}

    fun openFromNotification(extras: Map<String, String?>, isMarkRead: Boolean = false): Boolean {
        if (isMarkRead) {
            viewModel.compositeDisposable.add(
                viewModel.dataManager.getUserProfileDB()
                    .map { it.data?.profileId }
                    .concatMap { profileId ->
                        val responseObs: Observable<Long?> = when {
                            extras["notificationHistoryId"]?.isNotBlank() == true -> Observable.just(extras["notificationHistoryId"]?.toLong())
                            else -> viewModel.dataManager.getMessages(
                                MessageRequest(
                                    profileId = profileId,
                                    offset = 0,
                                    limit = 1,
                                    read = false
                                )
                            ).map { it.data?.data?.firstOrNull() }
                                .map {
                                    if (extras["templateCode"] != null && extras["templateCode"] == it.data?.get(
                                            "templateCode"
                                        )
                                    ) it.notificationHistoryId
                                    else null
                                }.concatMap { Observable.just(it) }
                        }
                        responseObs
                    }
                    .concatMap { viewModel.dataManager.markRead(it) }
                    .concatMap { read -> viewModel.dataManager.getUnreadCount().concatMap { Observable.just(read) } }
                    .subscribeOn(viewModel.schedulerProvider.io())
                    .observeOn(viewModel.schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Message>() {
                        override fun onSuccess(dataResponse: Message) {
                            refreshData(dataResponse)
                        }

                        override fun onFailure(error: NetworkError) {
                        }
                    })
            )
        }
        return openFromNotification(extras)
    }

    private fun openFromNotification(extras: Map<String, String?>): Boolean {
        when (extras["templateCode"]) {
            "WELCOME" -> {
                showTipScreen()
                return true
            }
            "CUSTOMER_VERIFIED"
                , "CUSTOMER_REJECTED"
                , "CUSTOMER_BLOCKED"
                , "CUSTOMER_UNBLOCKED" -> {
                showHomeScreen(true)
                return true
            }
            "FUTURE_SALE_REMINDER" -> {
                showCheckQualityScreen()
                return true
            }
            "ORDER_PICKED_TO_FACTORY"
                , "ORDER_ARRIVED_TO_FACTORY"
                , "ORDER_FINAL_VERIFIED_DETAIL"
                , "ORDER_NEGOTIATED_VERIFIED_DETAIL"
                , "ORDER_PAID_TO_CUSTOMER"
                , "ORDER_PICKED_TO_RETURN"
                , "ORDER_RETURNED_TO_CUSTOMER"
                , "ORDER_NOTE_MESSAGE"
                , "ORDER_CONTACT_LOGISTIC_TO_PICKUP" -> extras["orderId"]?.let {
                showOrderDetailScreen(Order(orderId = it.toLong()))
                return true
            }
            else -> {
                extras["orderId"]?.let {
                    showOrderDetailScreen(Order(orderId = it.toLong()))
                }
            }
        }
        return false
    }

    open fun showTipScreen() {
        if (this !is Home) {
            val tipIntent = Intent(this, QualityHelp::class.java)
            tipIntent.putExtra("EXTRA_URL", "https://mo-jual.com/tips")
            startActivity(tipIntent)
        }
    }

    open fun showHomeScreen(forceUpdate: Boolean) {
        if (this !is Home) {
            val intent = Intent(activity(), Home::class.java)
            intent.putExtra(EXTRA_FORCE_REFRESH, true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    open fun showOrderDetailScreen(dataResponse: Order) {
        if (this !is Home) {
            val mIntent = Intent(this, OrderDetailActivity::class.java)
            mIntent.putExtra("EXTRA_ORDER", dataResponse)
            startActivity(mIntent)
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun showLoading(isLoading: Boolean) {
        viewModel.setIsLoading(isLoading)
        baseBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        showEmpty(false)
    }

    override fun showEmpty(isEmpty: Boolean) {
        viewModel.setIsEmpty(isEmpty)
        baseBinding.emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    fun handleError(exception: LoginException?) {
        exception?.errorMessage?.let { show(it) }
    }

    fun handleError(exception: NetworkError) {
        exception.errorMessage?.let { show(it) }
        if (exception.errorCode == 401) {
            viewModel.compositeDisposable.add(ProfileViewModel.logoutStream(viewModel.dataManager).subscribeWith(
                object :
                    DisposableObserver<Any>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Any) {
                        forceLogin()
                    }

                    override fun onError(e: Throwable) {
                        forceLogin()
                    }
                }))
        }
    }

    fun handleError(error: String?) {
        error?.let { show(it) }
    }

    fun forceLogin() {
        finish()
        val loginIntent = Intent(this@BaseActivity, Login::class.java)
        loginIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(loginIntent)
    }

    override fun setTitle(titleId: Int) {
        super.setTitle("")
        baseBinding.tvBaseTitle.text = getString(titleId).toString().toUpperCase()
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("")
        baseBinding.tvBaseTitle.text = title
    }

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
    open fun openFragmentSlideRight(
        fragment: Fragment,
        placeHolder: Int,
        backStackTag: String? = null
    ) {
        run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !isDestroyed || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val manager = supportFragmentManager
                manager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(placeHolder, fragment, backStackTag).addToBackStack(backStackTag)
                    .commitAllowingStateLoss()
            }
        }
    }

    protected fun setActionBarColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    override fun activity(): Activity = this

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))

    override fun show(message: String) = RoundedOkDialog(message).show(supportFragmentManager, message)

    override fun show(messageResId: Int) {
        RoundedOkDialog(getString(messageResId)).show(
            supportFragmentManager,
            getString(messageResId)
        )
    }

    protected fun isPinSetted(): Boolean {
        val pin = PreferenceUtils.getString(AppConstant.PIN_CODE, "")
        return pin.isNotEmpty()
    }

    protected fun startPinCodeScreen() {
        if (isPinSetted()) {
            if (activity() !is PinCode) startActivityForResult(
                Intent(this, PinCode::class.java),
                REQUEST_CODE_PIN
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if ((App.wasInBackground || forceLock)
            && activity() !is Splash
            && activity() !is Login
            && activity() !is SignUpInfo
            && activity() !is SignUpPassword
        ) {
            App.wasInBackground = false
            PreferenceUtils.saveBoolean(EXTRA_FORCE_LOCK, false)
            startPinCodeScreen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_PIN -> if (resultCode != Activity.RESULT_OK) finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(mReceiver)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    class ScreenReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                App.wasInBackground = true
            }
        }
    }
}

