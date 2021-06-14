package com.example.iconify.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.iconify.R
import com.example.iconify.adapters.HomeViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            IconSetFragment(),
            IconsFragment()
        )

        val adapter = HomeViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )
        view.vp_home_view_pager.adapter = adapter

        view.vp_home_view_pager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    view.radio_group.check(R.id.rb_icon_set)
                } else if (position == 1) {
                    view.radio_group.check(R.id.rb_icons)
                }
            }
        })

        view.radio_group.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_icon_set)
                vp_home_view_pager.currentItem = 0
            else if (checkedId == R.id.rb_icons)
                vp_home_view_pager.currentItem = 1
        }

        return view
    }

}