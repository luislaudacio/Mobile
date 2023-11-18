package com.example.projetointegrador.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity):

    FragmentStateAdapter(fragmentActivity) {
        private val fragmentList: MutableList<Fragment> = arrayListOf()


        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }


        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

}