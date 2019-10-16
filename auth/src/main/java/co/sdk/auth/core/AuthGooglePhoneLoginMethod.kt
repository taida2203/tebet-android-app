package co.sdk.auth.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import co.sdk.auth.core.models.ApiCallBack
import co.sdk.auth.core.models.LoginConfiguration
import co.sdk.auth.core.models.LoginException
import co.sdk.auth.view.LoginOTPActivity

class AuthGooglePhoneLoginMethod : AuthMethod {
    private var callback: ApiCallBack<LoginConfiguration>? = null
    private var configuration: LoginConfiguration? = null

    val isLoggedIn: Boolean
        get() {
            return false
        }

    override fun brandedLogin(
        context: Activity,
        configuration: LoginConfiguration,
        callback: ApiCallBack<LoginConfiguration>
    ) {
        this.callback = callback
        this.configuration = configuration
        if (isLoggedIn) {
            // Handle Returning User
//            if (AccountKit.getCurrentAccessToken() != null) {
//                configuration.token = AccountKit.getCurrentAccessToken()?.token
//            }
//            AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
//                override fun onSuccess(account: Account) {
//                    val phoneNumber = account.phoneNumber
//                    val phoneNumberString = phoneNumber.toString()
//                    try {
//                        configuration.phone = phoneNumberString.replace("\\+".toRegex(), "")
//                            .replace(" ".toRegex(), "")
//                            .trim { it <= ' ' }
//                    } catch (ignored: Exception) {
//                        Timber.e(ignored)
//                    }
//
//                    configuration.token = AccountKit.getCurrentAccessToken()?.token
//                    configuration.let { callback.onSuccess(200, it) }
//                }
//
//                override fun onError(error: AccountKitError) {
//                    callback.onFailed(LoginException(400, error.userFacingMessage))
//                }
//            })
        } else {
            val intent = Intent(context, LoginOTPActivity::class.java)
//
//            //            String language = Locale.getDefault().toString();
//            var countryCode = "US"
//            //            if (!TextUtils.isEmpty(language) && !"in-ID".equalsIgnoreCase(language) && !"in_ID".equalsIgnoreCase(language)) {
//            //                countryCode = "US";
//            //            } else {
//            //                countryCode = "ID";
//            //            }
//            countryCode = "ID"
//
//            val configurationBuilder = com.facebook.accountkit.ui.AccountKitConfiguration.AccountKitConfigurationBuilder(
//                    com.facebook.accountkit.ui.LoginType.PHONE,
//                    AccountKitActivity.ResponseType.TOKEN)
//                    .setDefaultCountryCode(countryCode)
//                    .setFacebookNotificationsEnabled(true)
//            if (!TextUtils.isEmpty(configuration.phone)) {
//                val phone = configuration.phone?.let { PhoneNumber(null!!, it, null) }
//                configurationBuilder.setInitialPhoneNumber(phone)
//            }
//            // ... perform additional configuration ...
//            intent.putExtra(
//                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
//                    configurationBuilder.build())
            context.startActivityForResult(intent, REQUEST_CODE_LOGIN_FIRE_BASE)
        }
    }

    override fun logout(context: Context, forceLogout: Boolean, callback: ApiCallBack<*>?) {
//        AccountKit.logOut()
        callback?.onSuccess(200, null)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN_FIRE_BASE) { // confirm that this response matches your request
            when (resultCode) {
                Activity.RESULT_OK -> {
                    configuration?.token = data?.getStringExtra("EXTRA_ID_TOKEN")
                    if (callback != null) {
                        configuration?.let { callback?.onSuccess(200, it) }
                    }
                }
                else -> if (callback != null) {
                    callback?.onFailed(LoginException(502, ""))
                }
            }
        }
    }

    companion object {
        val REQUEST_CODE_LOGIN_FIRE_BASE = 4953
    }

}
