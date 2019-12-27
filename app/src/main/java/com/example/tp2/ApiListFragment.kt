package com.example.tp2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tp2.adapter.MarsAdapter
import com.example.tp2.adapter.MarsListener
import com.example.tp2.apiService.MarsProperty
import com.example.tp2.databinding.ApiFragmentListBinding
import com.example.tp2.model.User
import com.example.tp2.viewmodel.ApiListViewModel
import com.example.tp2.viewmodelfactory.ApiListViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ApiListFragment :  Fragment() {

    private lateinit var binding: ApiFragmentListBinding
    private lateinit var viewModel: ApiListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.api_fragment_list, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = ApiListViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApiListViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MarsAdapter(MarsListener { mars ->
            Toast.makeText(this.context, "" + mars.id + " " + mars.imgSrcUrl + " " + mars.type + " " + mars.price, Toast.LENGTH_SHORT).show()
        })

        binding.list.adapter = adapter

        viewModel.mars.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}

@BindingAdapter("marsPrice")
fun TextView.setMarsPrice(item: MarsProperty) {
    text = item.price.toString()
}