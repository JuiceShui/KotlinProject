package com.demo.kotlin.view.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainFragmentPagerAdapter(
    private val fragments: MutableList<Fragment>,
    manager: FragmentManager
) : FragmentStatePagerAdapter(manager) {
    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

}