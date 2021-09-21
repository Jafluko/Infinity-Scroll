package com.example.infinityscroll

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.LifecycleOwner as viewLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.infinityscroll.databinding.ActivityMainBinding
import com.example.infinityscroll.viewmodel.ListProvider

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity(), View.OnScrollChangeListener {

    private var activityBinding: ActivityMainBinding? = null
    private lateinit var viewModel: ListProvider
    private lateinit var rv: RecyclerView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ListProvider::class.java)
        activityBinding!!.viewModel = viewModel
        viewModel.list.observe(this, {
            it?.let {
                viewModel.adapter.list = it
                viewModel.adapter.notifyDataSetChanged()
            }
        })
        rv = findViewById(R.id.r_view)
        rv.setOnScrollChangeListener(this)
    }

    override fun onScrollChange(
        v: View?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val end = (rv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        if (end == viewModel.list.value!!.size - 1 && viewModel.list.value!!.size < 100) {
            viewModel.updateList(viewModel.list.value!![end].userId.toInt() + 1)
        }
    }
}