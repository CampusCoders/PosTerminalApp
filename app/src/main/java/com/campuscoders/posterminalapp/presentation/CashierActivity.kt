package com.campuscoders.posterminalapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views.CashierFragment
import com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views.DailyReportFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CashierActivity : AppCompatActivity() {

    private var ftransaction: FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier)

        ftransaction = supportFragmentManager.beginTransaction()

        val navigationValue = intent.getStringExtra("navigation")

        navigationValue?.let {
            if (it == "1") {
                ftransaction?.let {ft ->
                    ft.replace(R.id.fragmentContainerViewCashierActivity, DailyReportFragment())
                    ft.commit()
                }
            } else {
                ftransaction?.let {ft ->
                    ft.replace(R.id.fragmentContainerViewCashierActivity, CashierFragment())
                    ft.commit()
                }
            }
        }
    }
}