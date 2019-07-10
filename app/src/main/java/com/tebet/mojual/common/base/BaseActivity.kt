package com.tebet.mojual.common.base

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import co.common.network.ApiException
import co.common.view.BaseView
import com.tebet.mojual.R
import com.tebet.mojual.notification.view.NotificationActivity
import kotlinx.android.synthetic.main.activity_base.*
import java.lang.ref.WeakReference

abstract class BaseActivity : AppCompatActivity(), BaseView {
    lateinit var baseToolbar: Toolbar
    lateinit var ivBack: ImageButton
        private set
    lateinit var tvBaseTitle: TextView
        private set

//    protected lateinit var load: Loading
    private var notifExtra: MutableMap<String, String>? = null
    private var isActive: Boolean = false
    protected abstract val contentLayoutId: Int
    protected lateinit var mView: WeakReference<BaseView>

    override fun onCreate(savedInstanceState: Bundle?) {
//        LanguageUtil.instance.updateLanguage(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        baseToolbar = findViewById(R.id.base_toolbar)
        ivBack = findViewById(R.id.iv_back)
        tvBaseTitle = findViewById(R.id.tv_base_title)

//        load = Loading(this)
//        iv_back.setOnClickListener { onBackPressed() }
        setSupportActionBar(baseToolbar)

        title = ""
        mView = WeakReference(this)

        iv_notification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        iv_cakap_history.setOnClickListener {
//            startActivity(Intent(this, CornerHistoryActivity::class.java))
        }
        layoutInflater.inflate(contentLayoutId, placeHolder)

//        ButterKnife.bind(this)
        onCreateBase(savedInstanceState, contentLayoutId)
    }

    fun fullScreenChildHolder(isFullScreen: Boolean) {
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                (placeHolder.layoutParams as RelativeLayout.LayoutParams).removeRule(RelativeLayout.BELOW)
            } else {
                (placeHolder.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.BELOW, 0)
            }
        } else {
            (placeHolder.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.BELOW, R.id.base_toolbar)
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

    fun getActivity(): BaseActivity {
        return this
    }

    fun showpDialog(isShowProgressDialog: Boolean) {
//        if (load.isShowing && isShowProgressDialog) return
//        if (isShowProgressDialog) {
//            load.showpDialog()
//        } else {
//            load.hidepDialog()
//        }
    }

    override fun setTitle(titleId: Int) {
        super.setTitle("")
        tvBaseTitle.text = getString(titleId).toString().toUpperCase()
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("")
        tvBaseTitle.text = title
    }

    override fun getContext(): Context? {
        return this
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

//    fun showError(exception: SquException?, customContent: String? = null) {
//        // setup the alert builder
//        val builder = AlertDialog.Builder(this@BaseActivity, R.style.CustomDialog)
//
//        // builder.setTitle("My title");
//        if (exception?.errorMessage != null &&
//                !exception.errorMessage!!.contains("_", false) &&
//                exception.errorMessage!!.isNotBlank()) {
//
//            builder.setMessage(exception.errorMessage)
//            builder.setPositiveButton(R.string.general_button_ok, null)
//            val dialog = builder.create()
//            dialog.show()
//            return
//        }
//
//        when (exception?.errorMessage) {
//            "INACTIVE_ACCOUNT" -> {
//                ResendEmailPopUp().apply {
//                    mView = this@BaseActivity.mView
//                    contentMessage = customContent
//                }
//                        .setRoundedDialogCallback(object : RoundedDialog.RoundedDialogCallback {
//                            override fun onFirstButtonClicked(selectedValue: Any?) {
//                                showpDialog(true)
//                                val studentDTO = PreferenceUtils.getObject(StudentDTO.PREF_STUDENT_PROFILE, StudentDTO::class.java) as StudentDTO
//                                ApiService.createServiceNew(UpdateService::class.java)
//                                        .postResendEmail(ResendEmailRequest(studentDTO.email))
//                                        .enqueue(object : ApiCallbackV2<EmptyResponse>(mView) {
//                                            override fun onSuccess(response: EmptyResponse?) {
//                                                super.onSuccess(response)
//                                                showpDialog(false)
//                                                Toast.makeText(
//                                                        this@BaseActivity,
//                                                        getString(R.string.activity_send_mail_pop),
//                                                        Toast.LENGTH_LONG
//                                                ).show()
//                                                AnalyticEngine.action(TrackingEvent.VERIFY_EMAIL)
//                                            }
//
//                                            override fun onFail(call: Call<*>?, e: ApiException) {
//                                                super.onFail(call, e)
//                                                showpDialog(false)
//                                            }
//                                        })
//                            }
//
//                            override fun onSecondButtonClicked(selectedValue: Any?) {
//                            }
//
//                        })
//                        .show(supportFragmentManager, ResendEmailPopUp.TAG)
//            }
//            "LEVEL_GAP_0" -> {
//                builder.setMessage(R.string.confirm_booking_error_gap_0)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "HAVE_ANOTHER" -> {
//                builder.setMessage(R.string.confirm_booking_error_have_another)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "BOOK_DISABLED" -> {
//                builder.setMessage(R.string.confirm_booking_error_book_disable)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "TRIAL_END" -> {
//                builder.setMessage(R.string.confirm_booking_error_trial_end)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "NOT_ENOUGH_POINT" -> {
//                builder.setMessage(R.string.confirm_booking_error_point)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "PACKAGE_EXPIRED" -> {
//                builder.setMessage(R.string.confirm_booking_error_package_expired)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "EXERCISE_NOT_DONE" -> {
//                builder.setMessage(R.string.confirm_booking_error_exercise)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "PAST_COURSE" -> {
//                builder.setMessage("PAST_COURSE")
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "DENIED" -> {
//                builder.setMessage("DENIED")
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "TRIAL_CANCEL" -> {
//                builder.setMessage(R.string.confirm_booking_error_trial_cancel)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "GIFT_CANCEL" -> {
//                builder.setMessage(R.string.confirm_booking_error_gift_cancel)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "INSUFFICIENT_CANCEL_POIN" -> {
//                builder.setMessage(R.string.confirm_booking_error_insufficient)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "HAS_PAID_PACKAGE" -> {
//                builder.setMessage(R.string.confirm_booking_error_has_paid)
//                // add a button
//                builder.setPositiveButton("Activate") { dialog, which ->
//                    val sharedPreferences = MySharedPreferences(this)
//                    val courseRequest = CourseRequest()
//                    courseRequest.setToken(AuthSdk.instance.currentToken!!.appToken)
//                    courseRequest.setDeviceId(sharedPreferences.deviceId)
//                    courseRequest.setIdStudy(Integer.valueOf(sharedPreferences.idStudy))
//                    if (exception.data is String && (exception.data as String).isNotBlank()) {
//                        courseRequest.setStartDate(exception.data as String)
//                    }
//
//                    //TODO resolve network fail here
//                    ApiService.createServiceOld(CourseService::class.java).courseActivate(courseRequest)
//                            .enqueue(object : Callback<SquBaseResponse<Void>> {
//                                override fun onFailure(call: Call<SquBaseResponse<Void>>, t: Throwable) {
//                                    val intent = Intent(this@BaseActivity, HomeActivity::class.java)
//                                    this@BaseActivity.startActivity(intent)
//                                }
//
//                                override fun onResponse(call: Call<SquBaseResponse<Void>>, response: Response<SquBaseResponse<Void>>) {
//                                    val intent = Intent(this@BaseActivity, HomeActivity::class.java)
//                                    this@BaseActivity.startActivity(intent)
//                                }
//                            })
//                }
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "REACH_MAX_HOURS_FOR_CANCEL" -> {
//                //TODO: resolve 'rule' here. 'Course cancellation must have been made max $rule hour before the course begin.'
//                builder.setMessage("Course cancellation must have been made max hour before the course begin.")
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "CORPORATE_MEMBER" -> {
//                builder.setMessage(R.string.confirm_booking_error_corporate)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "REACH_ACTIVATED_PACKAGE" -> {
//                //TODO: resolve 'start' here
//                builder.setMessage("Please be reminded that you cannot book class before: " /*+ start!!*/)
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            "EMPTY_COURSE_CORPORATE" -> {
//                builder.setMessage("Your group can not book this class.")
//                // add a button
//                builder.setPositiveButton(R.string.general_button_ok, null)
//                // create and show the alert dialog
//                val dialog = builder.create()
//                dialog.show()
//            }
//            else -> {
//                Toast.makeText(this, getString(R.string.general_message_error), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

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

    override fun isActive(): Boolean {
        return isActive
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
