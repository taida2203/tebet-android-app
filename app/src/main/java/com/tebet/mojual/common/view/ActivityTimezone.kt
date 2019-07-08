package com.tebet.mojual.common.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squline.student.common.base.BaseActivity
import com.tebet.mojual.R
import com.tebet.mojual.common.models.GMTResponse
import com.tebet.mojual.common.rtc.view.AbstractRecycleViewAdapter
import retrofit2.Call
import java.util.*

class ActivityTimezone : BaseActivity() {
    private val countries = ArrayList<GMTResponse>()
    private var adapter: MyAdapter? = null
    private var swipe: SwipeRefreshLayout? = null

    override val contentLayoutId: Int
        get() = R.layout.activity_timezone

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        title = ""
        intent.extras?.getString("timezone")

        swipe = findViewById(R.id.swipe_refresh)
        val inputsearch = findViewById<EditText>(R.id.inputsearch)

        val rv = findViewById<RecyclerView>(R.id.myRecycler)
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()


        adapter = MyAdapter(this, countries)
        adapter?.setOnActionClick(object : AbstractRecycleViewAdapter.OnRecycleViewClick<GMTResponse> {
            override fun onRemoveClick(removeObject: GMTResponse) {}

            override fun onShowDetailClick(country: GMTResponse) {
                val resultIntent = Intent()
                resultIntent.putExtra("timezone", country.getGmtOffset())
                resultIntent.putExtra("timezoneId", country.getTimezone())
                setResult(111, resultIntent)
                finish()
            }
        })
        rv.adapter = adapter

        inputsearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter?.filter?.filter(s)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        swipe?.setOnRefreshListener { loadData() }

        loadData()
    }

    private fun loadData() {
        showpDialog(true)
//        ApiService.createServiceNew(ProfileService::class.java).getTimeZone().enqueue(object : ApiCallbackV2<List<GMTResponse>>(mView) {
//            override fun onSuccess(response: List<GMTResponse>?) {
//                showpDialog(false)
//                response?.let { countries.addAll(it) }
//                adapter?.notifyDataSetChanged()
//
//                swipe?.isRefreshing = false
//            }
//
//            override fun onFail(call: Call<*>?, e: ApiException) {
//                showpDialog(false)
//                swipe?.isRefreshing = false
//                Toast.makeText(applicationContext, R.string.general_message_error, Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    internal inner class MyAdapter(context: Activity, dataSet: List<GMTResponse>) :
        AbstractRecycleViewAdapter<GMTResponse>(context, dataSet), Filterable {
        internal inner class MyViewHolder(view: View) : AbstractRecycleViewAdapter<GMTResponse>.BaseViewHolder(view) {
            var name: TextView = view.findViewById(R.id.nama_country)
            var utc: TextView = view.findViewById(R.id.utc)
        }


        override fun getContentLayout(viewType: Int): Int {
            return R.layout.timezone_layout
        }

        override fun getViewHolder(v: View, viewType: Int): AbstractRecycleViewAdapter<GMTResponse>.BaseViewHolder {
            return MyViewHolder(v)
        }

        override fun onBindInstanceItemView(holder: RecyclerView.ViewHolder, position: Int) {
            val country = getItem(position)
            val castedHolder = holder as MyViewHolder
            castedHolder.name.text = country.getLocation()
            castedHolder.utc.text = country.getUtcOffset()
        }

        override fun canDelete(position: Int): Boolean {
            return false
        }

        override fun canShowDetail(): Boolean {
            return true
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
                    val results = Filter.FilterResults()
                    val filteredCountry = ArrayList<GMTResponse>()
                    for (gmtResponse in countries) {
                        if (gmtResponse.getLocation()?.toUpperCase()?.contains(constraint.toString().toUpperCase())!!) {
                            filteredCountry.add(gmtResponse)
                        }
                    }
                    results.count = filteredCountry.size
                    results.values = filteredCountry
                    return results
                }

                override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                    adapter?.items = results.values as List<GMTResponse>
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}
