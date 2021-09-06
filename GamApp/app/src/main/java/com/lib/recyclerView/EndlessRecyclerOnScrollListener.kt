package com.lib.recyclerView

import androidx.recyclerview.widget.RecyclerView


/**
 * Custom Scroll listener for RecyclerView.
 * Based on implementation https://gist.github.com/ssinss/e06f12ef66c51252563e
 */
abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {
    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private val visibleThreshold =
        5 // The minimum amount of items to have below your current scroll position before loading more.
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    private var currentPage = 1
    var mRecyclerViewHelper: RecyclerViewPositionHelper? = null
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView)
        visibleItemCount = recyclerView.childCount
        totalItemCount = mRecyclerViewHelper!!.itemCount
        firstVisibleItem = mRecyclerViewHelper!!.findFirstVisibleItemPosition()
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount
            <= firstVisibleItem + visibleThreshold
        ) {
            // End has been reached
            // Do something
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    //Start loading
    abstract fun onLoadMore(currentPage: Int)

    companion object {
        var TAG = "EndlessScrollListener"
    }
}