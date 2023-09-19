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

    // callback

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return ServicesFragment()
        } else {
            return EditCategoryFragment()
            /*
            if (changeableFragment == null) {
                changeableFragment = CategoriesFragment()
            }
            return changeableFragment!!

             */
        }
    }
}