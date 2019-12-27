package com.example.tp2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp2.adapter.MovieAdapter
import com.example.tp2.adapter.MovieListener
import com.example.tp2.adapter.OnScrollListener
import com.example.tp2.apiService.Movie
import com.example.tp2.database.MyDatabase
import com.example.tp2.databinding.ApiFragmentListMovieBinding
import com.example.tp2.model.User
import com.example.tp2.viewmodel.ApiListMovieViewModel
import com.example.tp2.viewmodelfactory.ApiListViewModelMovieFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ApiListMovieFragment :  Fragment() {

    private lateinit var binding: ApiFragmentListMovieBinding
    private lateinit var viewModel: ApiListMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.api_fragment_list_movie, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = MyDatabase.getInstance(application).userDao
        val sharedPreferences = application.getSharedPreferences("SP_USER", Context.MODE_PRIVATE)

        val id = sharedPreferences.getLong("ID",0L)

        val viewModelFactory = ApiListViewModelMovieFactory(application,dataSource,id)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApiListMovieViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MovieAdapter(MovieListener { movie ->
            this.findNavController().navigate(ApiListMovieFragmentDirections.actionApiListMovieFragmentToApiMovieDetailsFragment(movie))
            Toast.makeText(this.context, "" + movie._title, Toast.LENGTH_SHORT).show()
        })

        binding.list.adapter = adapter

        viewModel.movie.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.modifyUser.observe(viewLifecycleOwner, Observer { bool ->
            bool?.let {
                if(bool == true) {
                    this.findNavController().navigate(ApiListMovieFragmentDirections.actionApiListMovieFragmentToIdentityFragment())
                    viewModel.doneNavigating()
                }
            }
        })

        viewModel.logout.observe(viewLifecycleOwner, Observer { bool ->
            bool?.let {
                if(bool == true) {
                    this.findNavController().navigate(ApiListMovieFragmentDirections.actionApiListMovieFragmentToLoginFragment())

                    val userSP = sharedPreferences.edit()
                    userSP.clear()
                    userSP.apply()

                    viewModel.doneLogout()
                }
            }
        })

        binding.refreshVue.setOnRefreshListener {
            viewModel.page = 1
            viewModel.getMovies()
            viewModel.searchedValue = ""
            binding.searchBar.text = null
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.refreshVue.isRefreshing = it
            }
        })

        val layoutManager = LinearLayoutManager(this.activity)
        binding.list.layoutManager = layoutManager
        binding.list.addOnScrollListener(OnScrollListener(layoutManager, viewModel))

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchedValue = p0.toString()
                if(viewModel.searchedValue.length >= 3) {
                    if(!viewModel.startSearching) {
                        viewModel.startSearching = true
                        viewModel.page = 1
                    }
                    binding.refreshVue.isRefreshing = true
                    viewModel.getMoviesSearch()
                } else {
                    if(viewModel.startSearching) {
                        viewModel.startSearching = false
                        viewModel.page = 1
                        binding.searchBar.text = null
                        viewModel.getMovies()
                    }
                }
            }
        })

        return binding.root
    }
}

@BindingAdapter("moviePopularity")
fun TextView.setMoviePopularity(item: Movie) {
    text = item._popularity.toString()
}

@BindingAdapter("movieAverage")
fun TextView.setMovieAverage(item: Movie) {
    text = item._vote_average.toString()
}

@SuppressLint("NewApi")
@BindingAdapter("movieReleaseDate")
fun TextView.setMovieReleaseDate(item: Movie) {
    text = if(!item._release_date.isNullOrEmpty()) {
        DateTimeFormatter.ofPattern("d LLL yyyy", Locale.FRANCE).format(LocalDate.parse(item._release_date, DateTimeFormatter.ISO_DATE))
    } else {
        "N/A"
    }
}

@BindingAdapter("movieAverageProgressBar")
fun ProgressBar.setMovieAverage(item: Movie) {
    progress = 100 - (10*item._vote_average!!).toInt()
}

@BindingAdapter("genderImage")
fun ImageView.setGenderImage(item: User?) {
    setImageResource(when (item?.gender) {
        "Homme" -> R.mipmap.ic_man
        "Femme" -> R.mipmap.ic_woman
        else -> R.mipmap.ic_user
    })
}

