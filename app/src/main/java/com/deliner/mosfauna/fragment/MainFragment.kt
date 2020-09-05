package com.deliner.mosfauna.fragment

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.deliner.mosfauna.R
import com.deliner.mosfauna.utils.MainPageAdapter
import com.google.android.material.tabs.TabLayout


class MainFragment : CommonFragment() {

    var lastFragment = 0
    lateinit var viewPager: ViewPager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.fragment_main_viewpager)
        viewPager.adapter = MainPageAdapter(requireActivity().supportFragmentManager, requireContext())
        viewPager.currentItem = lastFragment

        val tabLayout: TabLayout = view.findViewById(R.id.fragment_main_tabs)
        tabLayout.setupWithViewPager(viewPager)
    }


    override fun onPause() {
        lastFragment = viewPager.currentItem
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewPager.currentItem = lastFragment
    }

    override fun getLayoutResource(): Int = R.layout.fragment_main
}