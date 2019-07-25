/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.tebet.mojual.view.base

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import co.common.network.ApiException
import co.sdk.auth.core.models.LoginException
import com.tebet.mojual.R
import com.tebet.mojual.notification.view.NotificationActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_base.*

/**
 * Created by amitshekhar on 07/07/17.
 */

abstract class BaseActivityNew<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseFragmentNew.Callback {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private val mProgressDialog: ProgressDialog? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

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

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    //    @Override
    //    protected void attachBaseContext(Context newBase) {
    //        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    //    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
//        ivBack.setOnClickListener { onBackPressed() }
//        setSupportActionBar(baseToolbar)
//        iv_notification.setOnClickListener {
//            startActivity(Intent(this, NotificationActivity::class.java))
//        }
//        iv_cakap_history.setOnClickListener {
//            //            startActivity(Intent(this, CornerHistoryActivity::class.java))
//        }
        layoutInflater.inflate(contentLayoutId, placeHolder)
        onCreateBase(savedInstanceState, contentLayoutId)
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

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing) {
            mProgressDialog.cancel()
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

    //    public void showLoading() {
    //        hideLoading();
    //        mProgressDialog = CommonUtils.showLoadingDialog(this);
    //    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, contentLayoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    protected lateinit var navLayout : RelativeLayout
    //    protected lateinit var load: Loading
    private var notifExtra: MutableMap<String, String>? = null
    private var isActive: Boolean = false

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    protected abstract val contentLayoutId: Int

    fun fullScreenChildHolder(isFullScreen: Boolean) {
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                (placeHolder.layoutParams as RelativeLayout.LayoutParams).removeRule(RelativeLayout.BELOW)
            } else {
                (placeHolder.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.BELOW, 0)
            }
        } else {
            (placeHolder.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.BELOW, R.id.baseToolbar)
        }
    }

    fun showNotification(isShow: Boolean) {
        if (isShow) {
            rlHomeNotificationContainer.visibility = View.VISIBLE
        } else {
            rlHomeNotificationContainer.visibility = View.GONE
        }
    }

    fun showCakapMenu(isShow: Boolean) {
        if (isShow) {
            rlHomeCakapCornerMenuContainer.visibility = View.VISIBLE
        } else {
            rlHomeCakapCornerMenuContainer.visibility = View.GONE
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if (this is HomeActivity) {
////            activity_komentar.setBackgroundResource(R.drawable.bg_choose_course)
//            base_toolbar.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, android.R.color.transparent))
//        }
//    }

//    @Subscribe
//    fun onForceLogout(event: OnForceLogoutEvent) {
//        if (getActivity() !is ActivityProfile && getActivity() !is ActivityLogin) {
//            val intent = Intent(getContext(), ActivityProfile::class.java)
//            intent.putExtra(ActivityProfile.EXTRA_FORCE_LOGOUT, true)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }
//    }


    fun showpDialog(isShowProgressDialog: Boolean) {
//        if (load.isShowing && isShowProgressDialog) return
//        if (isShowProgressDialog) {
//            load.showpDialog()
//        } else {
//            load.hidepDialog()
//        }
    }

    fun showLoading(isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun handleError(exeption: LoginException?) {
        if (exeption != null) {
            Toast.makeText(this, exeption.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun handleError(exception: Throwable) {
        if (exception != null) {
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setTitle(titleId: Int) {
        super.setTitle("")
        tvBaseTitle.text = getString(titleId).toString().toUpperCase()
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("")
        tvBaseTitle.text = title
    }


    fun enableBackButton(isEnable: Boolean) {
        when {
            isEnable -> {
                ivBack.visibility = View.VISIBLE
                tvBaseTitle.setOnClickListener { onBackPressed() }
            }
            else -> {
                ivBack.visibility = View.INVISIBLE
                tvBaseTitle.setOnClickListener(null)
            }
        }
    }

    protected fun showError(e: ApiException) {
        Toast.makeText(this, getString(R.string.general_message_error), Toast.LENGTH_SHORT).show()
    }


    protected abstract fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int)

    //    protected void showBackButton(boolean isShow) {
    //        if (getSupportActionBar() != null) {
    //            getSupportActionBar().setDisplayHomeAsUpEnabled(isShow);
    //        }
    //    }

    override fun onStart() {
        super.onStart()
//        EventBus.getDefault().register(this)
        isActive = true
    }

    override fun onStop() {
        super.onStop()
//        EventBus.getDefault().unregister(this)
        isActive = false
    }

    override fun onPause() {
        super.onPause()
        isActive = false
    }

    override fun onBackPressed() {
//        if (load.isShowing) {
//            load.hidepDialog()
//            return
//        }
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

    open fun addFragment(fragment: Fragment, placeHolder: Int, tag: String = "") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !isDestroyed || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(placeHolder, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

//    @SuppressLint("CommitTransaction")
//    protected fun openFragmentSlideRight(fragment: Fragment, placeHolder: Int, backStackTag : String? = null) {
//        run {
//            val manager = supportFragmentManager
//            manager.beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
//                    .replace(placeHolder, fragment).addToBackStack(backStackTag)?.
//                    commitAllowingStateLoss()
//        }
//    }


    protected fun setActionBarColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    open fun onNotificationEvent(notificationEvent: NotificationEvent?) {
//        if (notificationEvent == null)
//            return
//
//        val properties = Bundle().apply {
//            putString("code", notificationEvent.eventCode)
//            putString("source", "notification_push")
//            putString("extraParams", Gson().toJson(notificationEvent.map))
//        }
//        AnalyticEngine.action(TrackingEvent.NOTIFICATION_CLICK, properties)
//        handleNotification(notificationEvent)
//    }

//    private fun handleNotification(notificationEvent: NotificationEvent?) {
//        when (notificationEvent?.notyType) {
//            NotificationEvent.NOTY_TYPE.FORWARD_DIRECTLY -> {
//                navigateTo(notificationEvent)
//            }
//            NotificationEvent.NOTY_TYPE.SHOW_ALERT -> Alerter.create(getActivity())
//                    .setText(notificationEvent.message)
//                    .setDuration(4000)
//                    .setBackgroundColor(NotificationFactory.colorFrom(notificationEvent))
//                    .setOnClickListener { navigateTo(notificationEvent) }
//                    .show()
//            else -> {
//            }
//        }
//    }

//    protected fun navigateTo(notificationEvent: NotificationEvent) {
//        var intent: Intent? = null
//        when (NotificationFactory.pageFrom(notificationEvent)) {
//            NotificationFactory.PAGE.BUY -> {
////                if (getActivity() !is PrivateClassPaymentActivity) {
//                    intent = Intent(this, PrivateClassPaymentActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                }
//            }
////            NotificationFactory.PAGE.MY_INVOICE -> {
////                if (getActivity() !is ActivityMyInvoiceKt) {
////                    intent = Intent(this@BaseActivity, ActivityMyInvoiceKt::class.java)
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                }
////            }
//            NotificationFactory.PAGE.MY_PACKAGE -> {
//                if (getActivity() !is ActivityMyPackage) {
//                    intent = Intent(this@BaseActivity, ActivityMyPackage::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//            }
//            NotificationFactory.PAGE.UPCOMING_CLASS -> {
//                if (getActivity() !is UpcomingClassActivity) {
//                    intent = Intent(this@BaseActivity, UpcomingClassActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//            }
//            NotificationFactory.PAGE.HOME, NotificationFactory.PAGE.BOOKING, NotificationFactory.PAGE.PROFILE, NotificationFactory.PAGE.UNKNOWN -> {
//                if (getActivity() !is HomeActivity) {
//                    intent = Intent(this@BaseActivity, HomeActivity::class.java)
//                    intent.putExtra(NotificationEvent.FORCE_NAVIGATE_TO, notificationEvent.eventCode)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                } else {
//                    EventBus.getDefault().post(HomeChangeTabEvent(notificationEvent.eventCode))
//                }
//            }
//            NotificationFactory.PAGE.RTC -> {
//                // TODO redirect to VIDEO CALL
//                if (getActivity() as AppCompatActivity !is VideoCallActivity) {
//                    val courseId: Long? = try {
//                        (notificationEvent.map?.get("courseId") as Int?)?.toLong()
//                    } catch (e: Exception) {
//                        (notificationEvent.map?.get("courseId") as Double?)?.toLong()
//                    }
//                    intent = courseId?.let { VideoCallActivity.generateIntent(this, it) }
//                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                } else {
//                    EventBus.getDefault().post(HomeChangeTabEvent(notificationEvent.eventCode))
//                }
//            }
//        }
//        intent?.let {
//            startActivity(it)
//        }
//    }

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
}

