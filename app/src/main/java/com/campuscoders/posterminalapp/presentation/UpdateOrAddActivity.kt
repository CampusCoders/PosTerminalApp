package com.campuscoders.posterminalapp.presentation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.presentation.edit.views.UpdateOrAddCategoryFragment
import com.campuscoders.posterminalapp.presentation.edit.views.UpdateOrAddProductFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException

@AndroidEntryPoint
class UpdateOrAddActivity : AppCompatActivity() {

    private var ftransaction: FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_or_add)

        ftransaction = supportFragmentManager.beginTransaction()

        val intent = intent
        setResult(Activity.RESULT_OK,intent)
        val destinationValue = intent.getStringExtra(getString(R.string.navigation_from))
        val categoryOrProductId = intent.getStringExtra(getString(R.string.category_id_or_product_id))
        val productsCategoryId = intent.getStringExtra(getString(R.string.products_category_id))

        destinationValue?.let {
            when(it) {
                getString(R.string.navigation_from_category) -> {
                    ftransaction?.let { ft ->
                        val updateOrAddCategoryFragment = UpdateOrAddCategoryFragment()
                        val bundle = Bundle()
                        bundle.putString(getString(R.string.category_id_or_product_id), categoryOrProductId)
                        updateOrAddCategoryFragment.arguments = bundle
                        ft.replace(R.id.fragmentContainerViewUpdateOrEditActivity, updateOrAddCategoryFragment)
                        ft.commit()
                    }
                }
                getString(R.string.navigation_from_product) -> {
                    ftransaction?.let { ft ->
                        val updateOrAddProductFragment = UpdateOrAddProductFragment()
                        val bundle = Bundle()
                        bundle.putString(getString(R.string.category_id_or_product_id), categoryOrProductId)
                        bundle.putString(getString(R.string.products_category_id),productsCategoryId)
                        updateOrAddProductFragment.arguments = bundle
                        ft.replace(R.id.fragmentContainerViewUpdateOrEditActivity, updateOrAddProductFragment)
                        ft.commit()
                    }
                }
                else -> throw NullPointerException()
            }
        }
    }

}