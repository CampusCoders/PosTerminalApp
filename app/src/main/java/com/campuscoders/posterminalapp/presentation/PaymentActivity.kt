package com.campuscoders.posterminalapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.campuscoders.posterminalapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
    }
}