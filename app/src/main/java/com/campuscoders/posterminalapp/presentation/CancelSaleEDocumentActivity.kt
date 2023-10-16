package com.campuscoders.posterminalapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.presentation.cancel_and_document.views.CancelSaleAndQueryDocumentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelSaleEDocumentActivity : AppCompatActivity() {

    private var ftransaction: FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_sale_edocument)

        ftransaction = supportFragmentManager.beginTransaction()

        val intent = intent
        val navigationValue = intent.getStringExtra("navigation")

        val cancelSaleAndQueryDocumentFragment = CancelSaleAndQueryDocumentFragment()
        val bundle = Bundle()

        if (navigationValue == "1") {
            bundle.putString("from","cancel_sale")
        } else {
            bundle.putString("from","query_document")
        }

        cancelSaleAndQueryDocumentFragment.arguments = bundle

        ftransaction?.let {
            it.replace(R.id.fragmentContainerViewCancelSaleEDocument, cancelSaleAndQueryDocumentFragment)
            it.commit()
        }
    }
}