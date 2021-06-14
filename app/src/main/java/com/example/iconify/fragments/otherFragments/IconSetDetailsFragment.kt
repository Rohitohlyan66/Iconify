package com.example.iconify.fragments.otherFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.iconify.R
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModel
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModelFactory
import com.example.iconify.viewModel.icons.SearchIconsViewModel
import com.example.iconify.viewModel.icons.SearchIconsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_icon_set_details.*


class IconSetDetailsFragment : Fragment(R.layout.fragment_icon_set_details) {

    private val args: IconSetDetailsFragmentArgs by navArgs()
    lateinit var iconsSetDetailsViewModel: IconSetDetailsViewModel

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Getting icon set ID for its details
        val iconset_id = args.iconsetId
        val iconSet = args.iconSet
        val type = args.type
        val author = args.author
        val price = args.price
        val license = args.license

        tv_name.text = iconSet
        tv_type.text = "Type: $type"
        tv_license.text = "License: $license"
        tv_author.text = author
        tv_price.text = "$${price}"

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
                    Toast.makeText(requireActivity(), "Success", Toast.LENGTH_LONG).show()
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
                    "Failed due to ${response.message}",
                    Snackbar.LENGTH_SHORT
                ).show()

                is Resource.Loading -> Snackbar.make(view, "Loading", Snackbar.LENGTH_SHORT).show()
            }

        })

    }


}