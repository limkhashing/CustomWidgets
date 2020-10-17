package io.limkhashing.customwidgets.pagination

import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.limkhashing.customwidgets.utils.AppConstants

abstract class EndlessRecyclerViewScrollListener(private val mLinearLayoutManager: LinearLayoutManager,
    // True if we are still waiting for the last set of data to load.
    private val loading: ObservableBoolean,
    // List data, use to get count
    private val mItems: List<*>) : RecyclerView.OnScrollListener() {

    // The current offset index of data you have loaded
    private var currentPage = 1

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if has more data
    private var mHasMore = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition()
        val totalItemCount = mItems.size

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount == previousTotalItemCount - 1) {
            mHasMore = false
        }
        if (totalItemCount <= 1) {
            currentPage = STARTING_PAGE_INDEX
            mHasMore = true
        }
        previousTotalItemCount = totalItemCount

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (totalItemCount > 1 && mHasMore
            && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD && !loading.get()
            && (currentPage == 1 && totalItemCount >= AppConstants.PAGINATION_LIMIT || dy > 0)
        ) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
        }
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    companion object {
        // The minimum amount of mItems to have below your current scroll position
        // before loading more.
        private const val VISIBLE_THRESHOLD = 3

        // Sets the starting page index
        private const val STARTING_PAGE_INDEX = 1
    }
}