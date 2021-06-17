package com.example.iconify.fragments.otherFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.iconify.R
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import com.example.iconify.viewModel.iconDetails.IconDetailsViewModel
import com.example.iconify.viewModel.iconDetails.IconDetailsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_icons_details.*


class IconsDetailsFragment : Fragment(R.layout.fragment_icons_details) {

    private val args: IconsDetailsFragmentArgs by navArgs()
    lateinit var iconDetailsViewModel: IconDetailsViewModel
    lateinit var snackbar: Snackbar
    var user_id: Int = 0
    lateinit var author: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(view, "Loading...", Snackbar.LENGTH_INDEFINITE)

        val icon_id = args.iconId
        val icon_url = args.iconUrl
        val icon_name = args.iconName
        val author_name = args.authorName
        val icon_type = args.iconType
        val icon_price = args.iconPrice
        val icon_license = args.iconLicense

        Glide.with(this).load(icon_url).placeholder(R.drawable.placeholder).into(iv_icon)

        tv_icon_name.text = icon_name
        tv_icon_type.text = "Type: $icon_type"
        tv_icon_license.text = "License: $icon_license"

        tv_icon_author.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("user_id", user_id)
            bundle.putString("name", author)
            bundle.putString("license", icon_license)
            findNavController().navigate(
                R.id.action_iconsDetailsFragment_to_authorDetailsFragment,
                bundle
            )
        }


        val repository = IconifyRepository()
        val iconDetailsViewModelFactory =
            IconDetailsViewModelFactory(requireActivity().application, repository)

        iconDetailsViewModel = ViewModelProvider(
            requireActivity(),
            iconDetailsViewModelFactory
        ).get(IconDetailsViewModel::class.java)


        iconDetailsViewModel.getIconDetails(icon_id)

        iconDetailsViewModel.iconDetails.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { iconDetails ->
                            snackbar.dismiss()
                            user_id = iconDetails.iconset.author.user_id
                            author = iconDetails.iconset.author.name
                            tv_icon_author.text = author
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

    }


}