package com.example.iconify.fragments.otherFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iconify.R
import com.example.iconify.adapters.IconSetDetailsAdapter
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import com.example.iconify.viewModel.allIconsInIconSet.AllIconsInIconSetViewModel
import com.example.iconify.viewModel.allIconsInIconSet.AllIconsInIconSetViewModelFactory
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModel
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_icon_set_details.*


class IconSetDetailsFragment : Fragment(R.layout.fragment_icon_set_details) {

    private val args: IconSetDetailsFragmentArgs by navArgs()
    lateinit var iconsSetDetailsViewModel: IconSetDetailsViewModel
    lateinit var allIconSetDetailsViewModel: AllIconsInIconSetViewModel
    lateinit var allIconsInIconSetAdapter: IconSetDetailsAdapter
    lateinit var snackbar: Snackbar


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(view, "Loading...", Snackbar.LENGTH_INDEFINITE)
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
                is Resource.Error -> {
                    snackbar.dismiss()
                    Snackbar.make(
                        view,
                        "Failed due to ${response.message}", Snackbar.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    snackbar.show()
                }
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
                            snackbar.dismiss()
                            allIconsInIconSetAdapter.differ.submitList(allIconsInIconSetResponse.icons.toList())
                            rv_icon_set_details.visibility = View.VISIBLE
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


        //Handling Icon Clicks
        allIconsInIconSetAdapter.setOnItemClickListener { icon ->

            val count = icon.raster_sizes.size

            val bundle = Bundle()
            bundle.putString("icon_url", icon.raster_sizes[count - 1].formats[0].preview_url)
            bundle.putString("icon_name", icon.tags[0])
            bundle.putString("author_name", author)
            bundle.putString("icon_type", icon.type)
            bundle.putInt("icon_id", icon.icon_id)

            if (icon.prices == null) {
                bundle.putInt("icon_price", 0)
                bundle.putString("icon_license", "N/A")
            } else {
                bundle.putInt("icon_price", icon.prices[0].price)
                bundle.putString("icon_license", icon.prices[0].license.name)
            }

            findNavController().navigate(
                R.id.action_iconSetDetailsFragment_to_iconsDetailsFragment,
                bundle
            )

        }

    }

    private fun setUpRecyclerView() {
        allIconsInIconSetAdapter = IconSetDetailsAdapter()
        rv_icon_set_details.apply {
            adapter = allIconsInIconSetAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}