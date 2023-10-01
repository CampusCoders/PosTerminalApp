package com.campuscoders.posterminalapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.presentation.payment.views.DocumentQueryFragment
import com.campuscoders.posterminalapp.presentation.payment.views.SaleCancelFragment

class CancelSaleEDocumentActivity : AppCompatActivity() {

    private var ftransaction: FragmentTransaction? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_sale_edocument)

        ftransaction = supportFragmentManager.beginTransaction()
        val intent = intent
        val navigationValue = intent.getStringExtra("navigation")
        if (navigationValue != null && navigationValue == "1") {
            ftransaction?.let {
                it.replace(R.id.fragmentContainerViewCancelSaleEDocument, SaleCancelFragment())
                it.commit()
            }
        } else if (navigationValue != null && navigationValue == "2") {
            ftransaction?.let {
                it.replace(R.id.fragmentContainerViewCancelSaleEDocument, DocumentQueryFragment())
                it.commit()
            }
        }

    }
}