package com.example.itacademyhw.fragment

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itacademyhw.BeerViewModel
import com.example.itacademyhw.R
import com.example.itacademyhw.adapter.BeerAdapter
import com.example.itacademyhw.adapter.StateAdapter
import com.example.itacademyhw.databinding.FragmentListBinding
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val adapterPaging by lazy(LazyThreadSafetyMode.NONE) {
        BeerAdapter(requireContext())
    }
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel by viewModel<BeerViewModel>()

    private val searchQueryFlow: Flow<String>
        get() = callbackFlow {
            val searchView = binding.toolbar
                .menu
                .findItem(R.id.action_search).actionView as SearchView

            val queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    trySend(newText)
                    return true
                }
            }
            searchView.setOnQueryTextListener(queryTextListener)

            awaitClose {
                searchView.setOnQueryTextListener(null)
            }
        }

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
        with(binding) {
            linearLayoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapterPaging.withLoadStateHeaderAndFooter(
                header = StateAdapter(),
                footer = StateAdapter()
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel
                    .pagingFlow
                    .collectLatest {
                        adapterPaging.submitData(it)
                    }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                adapterPaging
                    .loadStateFlow
                    .distinctUntilChangedBy {
                        it.refresh
                    }
                    .collectLatest {
                        when (val state = it.refresh) {
                            is LoadState.Error -> {
                                Toast
                                    .makeText(
                                        requireContext(),
                                        "Error: ${state.error.message}",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    }
            }

            searchQueryFlow
                .debounce(1000)
                .map { query ->
                    viewModel.searchByName(query)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            recyclerView.addHorizontalSpaceDecoration(RECYCLER_ITEM_SPACE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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