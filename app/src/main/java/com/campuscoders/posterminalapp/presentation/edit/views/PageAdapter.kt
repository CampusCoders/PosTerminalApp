package com.campuscoders.posterminalapp.presentation.edit.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager,lifecycle) {

    private var changeableFragment: Fragment? = null

    fun changeState(fragment: Fragment) {
        changeableFragment = fragment
        notifyDataSetChanged()
        if (changeableFragment is EditProductFragment) {
            println("EditProductFragment")
        } else if (changeableFragment is EditCategoryFragment) {
            println("EditCategoryFragment")
        }
        createFragment(1)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        println("position -> $position")
        if (position == 0) {
            return ServicesFragment()
        } else {
            if (changeableFragment == null) {
                changeableFragment = EditCategoryFragment()
            }
            return changeableFragment!!
        }
    }
}