package com.example.iconify.fragments.otherFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iconify.R
import com.example.iconify.adapters.IconSetDetailsAdapter
import com.example.iconify.adapters.SearchIconsAdapter
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import com.example.iconify.viewModel.allIconsInIconSet.AllIconsInIconSetViewModel
import com.example.iconify.viewModel.allIconsInIconSet.AllIconsInIconSetViewModelFactory
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModel
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModelFactory
import com.example.iconify.viewModel.icons.SearchIconsViewModel
import com.example.iconify.viewModel.icons.SearchIconsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_icon_set_details.*
import kotlinx.android.synthetic.main.fragment_icon_set_details.view.*
import kotlinx.android.synthetic.main.fragment_icons.*


class IconSetDetailsFragment : Fragment(R.layout.fragment_icon_set_details) {

    private val args: IconSetDetailsFragmentArgs by navArgs()
    lateinit var iconsSetDetailsViewModel: IconSetDetailsViewModel
    lateinit var allIconSetDetailsViewModel: AllIconsInIconSetViewModel
    lateinit var allIconsInIconSetAdapter: IconSetDetailsAdapter


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()


        //Getting icon set ID for its details
        val iconset_id = args.iconsetId
        val iconSet = args.iconSet
        val type = args.type
        val author = args.author
        val price = args.price
        val license = args.license
        val user_id = args.userId

        tv_name.text = iconSet
        tv_type.text = "Type: $type"
        tv_license.text = "License: $license"
        tv_author.text = author
        tv_price.text = "$${price}"


        val bundle = Bundle()
        bundle.putInt("user_id", user_id)
        bundle.putString("name", author)
        bundle.putString("license", license)
        //Clicking on User Name
        tv_author.setOnClickListener {
            findNavController().navigate(
                R.id.action_iconSetDetailsFragment_to_authorDetailsFragment,
                bundle
            )
        }

        val repository = IconifyRepository()
        val iconSetDetailsViewModelFactory =
            IconSetDetailsViewModelFactory(requireActivity().application, repository)

        iconsSetDetailsViewModel = ViewModelProvider(
            requireActivity(),
            iconSetDetailsViewModelFactory
        ).get(IconSetDetailsViewModel::class.java)


        iconsSetDetailsViewModel.getAllDetails(iconset_id)

        iconsSetDetailsViewModel.iconSetDetails.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { iconDetailResponse ->

                        if (iconDetailResponse.readme == null) {
                            tv_readme.text = "N/A"
                        } else {
                            tv_readme.text = iconDetailResponse.readme
                        }
                    }
                }
                is Resource.Error -> Snackbar.make(
                    view,
                    "Failed due to ${response.message}", Snackbar.LENGTH_SHORT
                ).show()

                is Resource.Loading -> Snackbar.make(view, "Loading", Snackbar.LENGTH_SHORT).show()
            }

        })


        val allIconInIconSetViewModelFactory =
            AllIconsInIconSetViewModelFactory(requireActivity().application, repository)

        allIconSetDetailsViewModel = ViewModelProvider(
            requireActivity(),
            allIconInIconSetViewModelFactory
        ).get(AllIconsInIconSetViewModel::class.java)

        allIconSetDetailsViewModel.getAllIconsInIconSet(iconset_id)

        allIconSetDetailsViewModel.allIconsInIconSet.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { allIconsInIconSetResponse ->
                            allIconsInIconSetAdapter.differ.submitList(allIconsInIconSetResponse.icons.toList())
                            rv_icon_set_details.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Error -> {
                        Snackbar.make(
                            view,
                            "Failed due to ${response.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                    is Resource.Loading -> {
                        Snackbar.make(view, "Loading", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            })

    }

    private fun setUpRecyclerView() {
        allIconsInIconSetAdapter = IconSetDetailsAdapter()
        rv_icon_set_details.apply {
            adapter = allIconsInIconSetAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}