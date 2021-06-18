package com.example.iconify.fragments.otherFragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iconify.R
import com.example.iconify.adapters.IconSetAdapter
import com.example.iconify.adapters.UserIconSetAdapter
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModel
import com.example.iconify.viewModel.iconSetDetails.IconSetDetailsViewModelFactory
import com.example.iconify.viewModel.userDetails.UserDetailsViewModel
import com.example.iconify.viewModel.userDetails.UserDetailsViewModeldFactory
import com.example.iconify.viewModel.userIconSets.UserIconSetsViewModel
import com.example.iconify.viewModel.userIconSets.UserIconSetsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_author_details.*
import kotlinx.android.synthetic.main.fragment_author_details.rv_icon_set
import kotlinx.android.synthetic.main.fragment_author_details.tv_license
import kotlinx.android.synthetic.main.fragment_icon_set.*
import kotlinx.android.synthetic.main.fragment_icon_set_details.*

class AuthorDetailsFragment : Fragment(R.layout.fragment_author_details) {

    lateinit var userDetailsViewModel: UserDetailsViewModel
    lateinit var userIconSetsViewModel: UserIconSetsViewModel
    lateinit var userIconSetsAdapter: UserIconSetAdapter
    lateinit var snackbar: Snackbar

    val args: AuthorDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(view, "Loading...", Snackbar.LENGTH_INDEFINITE)
        setUpRecyclerView()

        val user_id = args.userId
        val name = args.name
        val license = args.license

        tv_author_name.text = name
        tv_license.text = "License: $license"


        val repository = IconifyRepository()
        val userDetailsViewModelFactory =
            UserDetailsViewModeldFactory(requireActivity().application, repository)

        userDetailsViewModel = ViewModelProvider(
            requireActivity(),
            userDetailsViewModelFactory
        ).get(UserDetailsViewModel::class.java)


        userDetailsViewModel.getUserDetails(user_id)

        userDetailsViewModel.userDetails.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { userDetailResponse ->
                        if (userDetailResponse.website_url == null) {
                            tv_author_website.text = "Website: N/A"
                        } else {
                            tv_author_website.text = userDetailResponse.website_url
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


        val userIconSetsViewModelFactory =
            UserIconSetsViewModelFactory(requireActivity().application, repository)

        userIconSetsViewModel = ViewModelProvider(
            requireActivity(),
            userIconSetsViewModelFactory
        ).get(UserIconSetsViewModel::class.java)

        userIconSetsViewModel.getUserIconSets(user_id)

        userIconSetsViewModel.userIconSets.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { userIconSets ->
                        snackbar.dismiss()
                        userIconSetsAdapter.differ.submitList(userIconSets.iconsets.toList())
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


        //Handling user Icon Set Clicks
        userIconSetsAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putInt("iconset_id", it.iconset_id)
            bundle.putString("iconSet", it.name)
            bundle.putString("type", it.type)
            bundle.putString("author", name)
            if(it.prices != null) {
                bundle.putInt("price", it.prices[0].price)
                bundle.putString("license", it.prices[0].license.name)
            } else {
                bundle.putInt("price", 0)
                bundle.putString("license", "N/A")
            }

            bundle.putInt("user_id", user_id)
            findNavController().navigate(
                R.id.action_authorDetailsFragment_to_iconSetDetailsFragment,
                bundle
            )

        }

    }


    private fun setUpRecyclerView() {
        userIconSetsAdapter = UserIconSetAdapter()
        rv_icon_set.apply {
            adapter = userIconSetsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }


}