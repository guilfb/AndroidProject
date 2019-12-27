package com.example.tp2.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.viewmodel.ApiListMovieViewModel

class OnScrollListener(
    val layoutManager: LinearLayoutManager,
    val viewModel: ApiListMovieViewModel
) : RecyclerView.OnScrollListener() {

    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var previousTotal = 0
    var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount

        totalItemCount = viewModel.movie.value?.size!!
        if(loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if(!loading && (firstVisibleItem >= totalItemCount - 5)) {
            viewModel.page += 1
            if(viewModel.searchedValue.length >= 3) {
                viewModel.getMoviesSearch()
            } else {
                viewModel.getMovies()
            }
            loading = true
        }
    }
}
