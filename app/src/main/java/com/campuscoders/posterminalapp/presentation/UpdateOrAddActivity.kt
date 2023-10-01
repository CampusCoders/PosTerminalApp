package com.campuscoders.posterminalapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.presentation.edit.views.UpdateOrAddCategoryFragment
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
        val destinationValue = intent.getStringExtra("from")
        val categoryOrProductId = intent.getIntExtra("category_or_product_id",-1)

        destinationValue?.let {
            when(it) {
                "category" -> {
                    ftransaction?.let {
                        val updateOrAddCategoryFragment = UpdateOrAddCategoryFragment()
                        val bundle = Bundle()
                        bundle.putInt("category_or_product_id",categoryOrProductId)
                        updateOrAddCategoryFragment.arguments = bundle
                        it.replace(R.id.fragmentContainerViewUpdateOrEditActivity,UpdateOrAddCategoryFragment())
                        it.commit()
                    }
                }
                "product" -> {
                    /*
                    ftransaction?.let {
                        val updateOrAddProductFragment = UpdateOrAddProductFragment()
                        val bundle = Bundle()
                        bundle.putString("update_or_add",updateOrAdd)
                        updateOrAddProductFragment.arguments = bundle
                        it.replace(R.id.fragmentContainerViewUpdateOrEditActivity,UpdateOrAddProductFragment())
                        it.commit()
                    }
                    */
                }
                else -> throw NullPointerException()
            }
        }
    }

}