package com.tebet.mojual.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivityHomeBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.home.content.HomeFragment
import com.tebet.mojual.view.home.content.ProfileFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HasSupportFragmentInjector, HomeNavigator {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HomeViewModel
        get() = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_home

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = "Home"
        openFragment(HomeFragment(), R.id.contentHolder)
//        getData()
//        btnLogout.setOnClickListener {
//            AuthSdk.instance.logout(false, object : ApiCallBack<Any>() {
//                override fun onSuccess(responeCode: Int, response: Any?) {
//                    finish()
//                    startActivity(Intent(this@HomeActivity, Login::class.java))
//                }
//
//                override fun onFailed(exeption: LoginException) {
//                }
//            })
//        }
    }

    override fun openSellNowScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openCheckQualityScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openBorrowScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openTipsScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProfile() {
        openFragment(ProfileFragment(), R.id.contentHolder)
    }

    private fun getData() {
//        Handler().postDelayed({
//            ServiceHelper.createService(ApiInterface::class.java).getData().enqueue(object : retrofit2.Callback<ResponseBody> {
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                }
//
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    val resultString = response.body()?.string()
//                    val document = Jsoup.parse(resultString).select("tr")
//                    val sensorData = SensorData()
//                    document.forEach { info ->
//                        when {
//                            info.select("td")[0].html().toLowerCase().contains("tilt") -> sensorData.tilt = info.select("td")[1].html()
//                            info.select("td")[0].html().toLowerCase().contains("temp") -> sensorData.temperature = info.select("td")[1].html()
//                            info.select("td")[0].html().toLowerCase().contains("battery") -> sensorData.battery = info.select("td")[1].html()
//                            info.select("td")[0].html().toLowerCase().contains("gravity") -> sensorData.gravity = info.select("td")[1].html()
//                        }
//                    }
//                    tvTilt.text = sensorData.tilt
//                    tvTemp.text = sensorData.temperature
//                    tvBattery.text = sensorData.battery
//                    tvGravity.text = sensorData.gravity
//                }
//            })
//            getData()
//        }, 5000)
    }
}
