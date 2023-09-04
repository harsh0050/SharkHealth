package com.example.hackout.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hackout.fragments.EnterAbhaFragment
import com.example.hackout.fragments.ProfileFragment

class HomePageViewPagerAdapter(val userId: String, fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0->{
                ProfileFragment.newInstance(userId)
            }
            else->{
                EnterAbhaFragment.newInstance(userId)
            }
        }
    }
}