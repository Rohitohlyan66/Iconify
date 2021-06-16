package com.example.iconify.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iconify.R
import com.example.iconify.adapters.SearchIconsAdapter
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import com.example.iconify.viewModel.icons.SearchIconsViewModel
import com.example.iconify.viewModel.icons.SearchIconsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_icons.*

class IconsFragment : Fragment(R.layout.fragment_icons) {


    lateinit var searchIconsViewModel: SearchIconsViewModel
    lateinit var searchIconsAdapter: SearchIconsAdapter
    lateinit var snackbar: Snackbar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(requireParentFragment().requireView(), "Loading...", Snackbar.LENGTH_INDEFINITE)
        setUpRecyclerView()

        val repository = IconifyRepository()
        val searchedIconViewModelFactory =
            SearchIconsViewModelFactory(requireActivity().application, repository)

        searchIconsViewModel = ViewModelProvider(
            requireActivity(),
            searchedIconViewModelFactory
        ).get(SearchIconsViewModel::class.java)

        searchIconsViewModel.searchedIcons.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { searchIconResponse ->
                        snackbar.dismiss()
                        searchIconsAdapter.differ.submitList(searchIconResponse.icons.toList())
                    }
                }
                is Resource.Error -> {
                    snackbar.dismiss()
                    Snackbar.make(
                        view,
                        "Failed due to ${response.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    snackbar.show()

                }
            }

        })


        ib_search.setOnClickListener {
            if (et_search_field.text.trim().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter some Icon", Toast.LENGTH_SHORT).show()
            } else {
                searchIconsViewModel.getAllSearchedIcons(et_search_field.text.toString())
            }
        }

    }


    private fun setUpRecyclerView() {
        searchIconsAdapter = SearchIconsAdapter()
        rv_search_icons.apply {
            adapter = searchIconsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}