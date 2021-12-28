package com.example.itacademyhw

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itacademyhw.databinding.FragmentListBinding
import com.example.itacademyhw.model.BeerData
import com.example.itacademyhw.networking.BeerService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    private var currentPage = 1
    private var isLoading  = false
    private val adapter = BeerAdapter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadBeers()

        with(binding) {
            toolbar.menu
                .findItem(R.id.action_search)
                .let { it.actionView as SearchView }
                .let {
                    it.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            handleSearch(query)
                            return true
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            return true
                        }
                    })
                    it.setOnQueryTextFocusChangeListener { _, b ->
                        if (!b) {
                            adapter.submitList(emptyList())
                            adapter.submitList(listOf(PageItem.Loading))
                            loadBeers()
                        }
                    }
                }

            linearLayoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = linearLayoutManager

            recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val totalItemCount = recyclerView.layoutManager!!.itemCount
                    println("$lastVisibleItemPosition $totalItemCount")
                    if (lastVisibleItemPosition >= ((totalItemCount - 1) / 2) && !isLoading) {
                        loadBeers(currentPage + 1)
                        currentPage += 1
                    }
                }
            })

            recyclerView.adapter = adapter
            recyclerView.addHorizontalSpaceDecoration(RECYCLER_ITEM_SPACE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadBeers(page: Int = 1, perPage: Int = 30,onFinishLoading: () -> Unit = {}) {
        isLoading = true
        BeerService.beerApi.getBeers(page, perPage)
            .enqueue(object : Callback<List<BeerData>> {
                override fun onResponse(
                    call: Call<List<BeerData>>,
                    response: Response<List<BeerData>>
                ) {
                    if (response.isSuccessful) {
                        val listData = adapter.currentList.toMutableList()
                        if (listData.contains(PageItem.Loading)) {
                            listData.remove(PageItem.Loading)
                        }
                        val newData = response.body() ?: emptyList()
                        for (item in newData) {
                            listData.add(PageItem.Content(item))
                        }
                        listData.add(PageItem.Loading)
                        adapter.submitList(listData.toList())
                    }
                    onFinishLoading().apply {
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<List<BeerData>>, t: Throwable) {
                    onFinishLoading().apply {
                        isLoading = false
                    }
                    Snackbar.make(binding.root, t.message ?: "", Snackbar.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun loadSearchedBeers(query: String, page: Int = 1, perPage: Int = 30,onFinishLoading: () -> Unit = {}) {
        isLoading = true
        BeerService.beerApi.searchBeer(query,page, perPage)
            .enqueue(object : Callback<List<BeerData>> {
                override fun onResponse(
                    call: Call<List<BeerData>>,
                    response: Response<List<BeerData>>
                ) {
                    if (response.isSuccessful) {
                        adapter.submitList(emptyList())
                        val listData = adapter.currentList.toMutableList()
                        if (listData.contains(PageItem.Loading)) {
                            listData.remove(PageItem.Loading)
                        }
                        val newData = response.body() ?: emptyList()
                        for (item in newData) {
                            listData.add(PageItem.Content(item))
                        }
                        adapter.submitList(listData.toList())
                    }
                    onFinishLoading().apply {
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<List<BeerData>>, t: Throwable) {
                    onFinishLoading().apply {
                        isLoading = false
                    }
                    Snackbar.make(binding.root, t.message ?: "", Snackbar.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun handleSearch(query: String) {
        adapter.submitList(emptyList())
        adapter.submitList(listOf(PageItem.Loading))
        loadSearchedBeers(query) {
            Snackbar.make(binding.root, "This is what we've found", Snackbar.LENGTH_SHORT)
                .show()
        }

    }

    companion object {
        private const val RECYCLER_ITEM_SPACE = 50
    }
}

fun RecyclerView.addHorizontalSpaceDecoration(space: Int) {
    addItemDecoration(
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position != 0 && position != parent.adapter?.itemCount) {
                    outRect.top = space
                }
            }
        }
    )
}