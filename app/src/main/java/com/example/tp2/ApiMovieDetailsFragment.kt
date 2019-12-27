package com.example.tp2

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.tp2.databinding.ApiFragmentMovieDetailsBinding
import com.example.tp2.viewmodel.ApiMovieDetailsViewModel
import com.example.tp2.viewmodelfactory.ApiMovieDetailsViewModelFactory

class ApiMovieDetailsFragment :  Fragment() {

    private lateinit var binding: ApiFragmentMovieDetailsBinding
    private lateinit var viewModel: ApiMovieDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.api_fragment_movie_details, container, false)

        val args = ApiMovieDetailsFragmentArgs.fromBundle(arguments!!)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = ApiMovieDetailsViewModelFactory(application,args.movie)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApiMovieDetailsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}

@BindingAdapter("movieImage")
fun ImageView.setMovieImage(imgLink: String?) {
    if (imgLink != null) {
        context?.let {
            if(imgLink.isNotEmpty()){
                Glide
                    .with(it)
                    .load("https://image.tmdb.org/t/p/w300/"+imgLink)
                    .into(this)
            }
        }
    } else {
        setImageResource(R.mipmap.ic_launcher)
    }
}
