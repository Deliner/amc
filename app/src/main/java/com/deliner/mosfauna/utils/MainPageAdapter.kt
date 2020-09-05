package com.deliner.mosfauna.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.deliner.mosfauna.fragment.GuideFragment
import com.deliner.mosfauna.fragment.MapFragment

class MainPageAdapter(fragmentManager: FragmentManager, private val context: Context) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val tabs = arrayOf("Гид", "Карта")

    override fun getCount(): Int = tabs.size

    override fun getItem(position: Int): Fragment {
        return if (position == 0) GuideFragment() else MapFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }
}