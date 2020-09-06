package org.abanoubmilad.nut.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_booksearch.*
import org.abanoubmilad.nut.R
import org.abanoubmilad.nut.adapters.BookSearchResultsAdapter
import org.abanoubmilad.nut.viewmodels.BookSearchViewModel

class BookSearchFragment : Fragment() {
    private var viewModel: BookSearchViewModel? = null
    private var adapter: BookSearchResultsAdapter? = null
    private var keywordEditText: TextInputEditText? = null
    private var authorEditText: TextInputEditText? = null
    private var searchButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BookSearchResultsAdapter()
        viewModel =
            ViewModelProviders.of(this).get(BookSearchViewModel::class.java)
        viewModel?.init()
        viewModel?.volumesResponseLiveData?.observe(
            this,
            Observer { volumesResponse ->
                if (volumesResponse != null) {
                    adapter?.setResults(volumesResponse.items.orEmpty())
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_booksearch, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView =
            view.findViewById(R.id.fragment_booksearch_searchResultsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        keywordEditText = view.findViewById(R.id.fragment_booksearch_keyword)
        authorEditText = view.findViewById(R.id.fragment_booksearch_author)
        fragment_booksearch_search.setOnClickListener { performSearch() }
        fragment_booksearch_search_3_parallel.setOnClickListener { performSearchThreeParallel() }
    }

    fun performSearch() {
        val keyword = keywordEditText?.editableText.toString()
        val author = authorEditText?.editableText.toString()
        viewModel?.searchVolumes(keyword, author)
    }

    fun performSearchThreeParallel() {
        val keyword = keywordEditText?.editableText.toString()
        val author = authorEditText?.editableText.toString()
        viewModel?.searchVolumesThreeParallel(keyword, author)
    }
}