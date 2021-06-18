package com.example.iconify.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

        snackbar = Snackbar.make(
            requireParentFragment().requireView(),
            "Loading...",
            Snackbar.LENGTH_INDEFINITE
        )
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






        searchIconsAdapter.setOnItemClickListener { icon ->

            val count = icon.raster_sizes.size

            val bundle = Bundle()
            bundle.putString("icon_url", icon.raster_sizes[count - 1].formats[0].preview_url)
            bundle.putString("icon_name", icon.tags[0])
            bundle.putString("author_name", "")
            bundle.putString("icon_type", icon.type)
            bundle.putInt("icon_id", icon.icon_id)

            if (icon.prices == null) {
                bundle.putInt("icon_price", 0)
                bundle.putString("icon_license", "N/A")
            } else {
                bundle.putInt("icon_price", icon.prices[0].price.toInt())
                bundle.putString("icon_license", icon.prices[0].license.name)
            }

            findNavController().navigate(
                R.id.action_viewPagerFragment_to_iconsDetailsFragment,
                bundle
            )


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