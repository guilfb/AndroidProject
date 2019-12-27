package com.example.tp2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tp2.adapter.UserAdapter
import com.example.tp2.adapter.UserListener
import com.example.tp2.database.MyDatabase
import com.example.tp2.databinding.FragmentListBinding
import com.example.tp2.model.User
import com.example.tp2.viewmodel.ListViewModel
import com.example.tp2.viewmodelfactory.ListViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ListFragment :  Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = MyDatabase.getInstance(application).userDao

        val viewModelFactory = ListViewModelFactory(dataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = UserAdapter(UserListener { user ->
            val msg = "" + user.firstname + " " + user.lastname
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
        })

        binding.list.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}

@BindingAdapter("userDate")
fun TextView.setUserDate(item: User) {
    val date = Date(item.birthdayDate)
    val f = SimpleDateFormat("dd/MM/yy")
    val dateText = f.format(date)
    text = dateText
}

@BindingAdapter("userImage")
fun ImageView.setUserImage(item: User) {
    setImageResource(when (item.gender) {
        "Homme" -> R.mipmap.ic_man
        else -> R.mipmap.ic_woman
    })
}
