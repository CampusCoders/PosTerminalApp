package com.campuscoders.posterminalapp.presentation.sale.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.utils.Constants

class BarcodeScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val intent = Intent()
                intent.putExtra(Constants.BARCODE,it.text)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                val intent = Intent()
                intent.putExtra(Constants.BARCODE,it.message)
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
