package com.tebet.mojual.common.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import co.common.view.BaseView
import java.lang.ref.WeakReference

open class BaseFragment : Fragment(), BaseView {
    protected lateinit var mView: WeakReference<BaseView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = WeakReference(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.fullScreenChildHolder(false)
    }

    protected fun showpDialog(isShowProgressDialog: Boolean) {
        showpDialog(isShowProgressDialog, false)
    }

    protected fun showpDialog(isShowProgressDialog: Boolean, isForceShow : Boolean) {
        if (activity != null && activity is BaseActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity!!.isDestroyed) return
            }
            if(!isForceShow && !isVisible) return
            (activity as BaseActivity).showpDialog(isShowProgressDialog)
        }
    }

//    protected fun showError(exception: SquException?, customMessage : String? = null) {
//        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            if (activity!!.isDestroyed) return
//        }
//        if(!isVisible) return
//
//        if (activity != null && activity is BaseActivity) {
////            (activity as BaseActivity).showError(exception, customMessage)
//        } else {
////            if (exception != null && !TextUtils.isEmpty(exception.errorMessage)) {
////                Toast.makeText(context, exception.errorMessage, Toast.LENGTH_SHORT).show()
////                return
////            }
//            Toast.makeText(context, getString(R.string.general_message_error), Toast.LENGTH_SHORT).show()
//        }
//    }

    protected fun getBaseActivity(): BaseActivity? {
        if (activity is BaseActivity) {
            return activity as BaseActivity
        }
        return null
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
    protected fun getTitleBack(): TextView? {
        return getBaseActivity()?.tvBaseTitle
    }

    protected fun getBack(): ImageButton? {
        return getBaseActivity()?.ivBack
    }

    override fun isActive(): Boolean {
        return isVisible
    }

    protected fun openFragment(fragment: Fragment, placeHolder: Int) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).openFragment(fragment, placeHolder)
        } else {
            //            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !isDestroyed())
            //                    || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            run {
                val manager = activity?.supportFragmentManager
                val transaction = manager?.beginTransaction()
                transaction?.replace(placeHolder, fragment)
                transaction?.commitAllowingStateLoss()
            }
        }
    }

//    @SuppressLint("CommitTransaction")
//    protected fun openFragmentSlideUp(fragment: Fragment, placeHolder: Int, backStackTag : String? = null) {
//        run {
//            val manager = activity?.supportFragmentManager
//            manager?.beginTransaction()?.
//                    setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_up)?.
//                    add(placeHolder, fragment)?.
//                    addToBackStack(backStackTag)?.
//                    commitAllowingStateLoss()
//        }
//    }

//    @SuppressLint("CommitTransaction")
//    protected fun openFragmentSlideRight(fragment: Fragment, placeHolder: Int, backStackTag : String? = null) {
//        run {
//            val manager = activity?.supportFragmentManager
//            manager?.beginTransaction()?.
//                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)?.
//                    replace(placeHolder, fragment)?.
//                    addToBackStack(backStackTag)?.
//                    commitAllowingStateLoss()
//        }
//    }

//    @SuppressLint("CommitTransaction")
//    protected fun openFragmentSlideFromTop(fragment: Fragment, placeHolder: Int, backStackTag : String? = null) {
//        run {
//            val manager = activity?.supportFragmentManager
//            manager?.beginTransaction()?.
//                    setCustomAnimations(R.anim.alerter_slide_in_from_top, 0, R.anim.alerter_slide_in_from_top, 0)?.
//                    replace(placeHolder, fragment)?.
//                    addToBackStack(backStackTag)?.
//                    commitAllowingStateLoss()
//        }
//    }
}
