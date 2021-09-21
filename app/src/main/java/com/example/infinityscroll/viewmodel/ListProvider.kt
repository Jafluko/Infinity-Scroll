package com.example.infinityscroll.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinityscroll.Adapter.RvAdapter
import com.example.infinityscroll.R
import com.example.infinityscroll.model.ApiService
import com.example.infinityscroll.model.Api
import com.example.infinityscroll.model.ItemData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ListProvider : ViewModel() {

    val adapter = RvAdapter(R.layout.item_rv_view, this)

    private var api: Api = ApiService().createApi()

    var pgVisible = ObservableInt(View.VISIBLE)
    var rvVisible = ObservableInt(View.GONE)
    var pbItemVisible = ObservableInt(View.GONE)

    var list: MutableLiveData<MutableList<ItemData>> = MutableLiveData()
    private var tempList: MutableList<ItemData> = mutableListOf()

    init {
        updateList(1)
    }

    fun updateList(page: Int) {
        if (list.value != null) {
            pbItemVisible.set(View.VISIBLE)
        }

        api.getNextPage(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({ result ->
                tempList.addAll(result)
                list.postValue(tempList)
                pbItemVisible.set(View.GONE)
                pgVisible.set(View.GONE)
                rvVisible.set(View.VISIBLE)
            }, { error ->
                Log.d("Error", error.localizedMessage!!)
            })
    }

    fun getItem(position: Int): ItemData? =
        if (list.value!!.size > position) list.value!![position] else null
}