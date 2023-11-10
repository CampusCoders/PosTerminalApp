package com.campuscoders.posterminalapp.presentation

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.ActivityMainBinding
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import android.content.pm.PackageManager

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customSharedPreferences = CustomSharedPreferences(this)
        val terminalUser = customSharedPreferences.getTerminalUserLogin()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            val readPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
            val cameraPermission = android.Manifest.permission.CAMERA

            val permissionsToRequest = mutableListOf<String>()

            // CAMERA iznini kontrol et
            if (checkSelfPermission(cameraPermission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(cameraPermission)
            }

            // WRITE_EXTERNAL_STORAGE iznini kontrol et
            if (checkSelfPermission(writePermission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(writePermission)
            }

            // READ_EXTERNAL_STORAGE iznini kontrol et
            if (checkSelfPermission(readPermission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(readPermission)
            }

            // İzinleri talep et
            if (permissionsToRequest.isNotEmpty()) {
                val permissionArray = permissionsToRequest.toTypedArray()
                requestPermissions(permissionArray, 1)
            }
        }

        val intent = Intent(this, CashierAndDailyReportActivity::class.java)

        binding.imageViewMenu.setOnClickListener {
            binding.drawerParent.openDrawer(GravityCompat.END)
        }

        binding.menulayout.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemDailyReport -> {
                    if (terminalUser["tum_raporları_goruntuleme"] as Boolean) {
                        intent.putExtra("navigation", "1")
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                    } else {
                        Toast.makeText(this,"Yetkiniz yok.",Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.itemCashierOperations -> {
                    if (terminalUser["kasiyer_goruntuleme"] as Boolean) {
                        intent.putExtra("navigation", "2")
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                    } else {
                        Toast.makeText(this,"Yetkiniz yok.",Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.itemTerminalInformations -> {
                    showTerminalPopup()
                }
            }
            binding.drawerParent.closeDrawer(GravityCompat.END)
            true
        }

        binding.menulayout.linearLayoutLogOut.setOnClickListener {
            logOut()
        }

    }

    private fun logOut() {
        val customSharedPreferences = CustomSharedPreferences(this)
        customSharedPreferences.setMainUserLoginRememberMeManager(false)
        customSharedPreferences.setMainUserLoginRememberMeCashier(false)
        val intent = Intent(this,LoginActivity::class.java)
        intent.putExtra("logout","logout")
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        finish()
    }

    private fun showTerminalPopup() {
        val customSharedPreferences = CustomSharedPreferences(this)
        val mainUserInfos = customSharedPreferences.getMainUserLogin()
        val terminalNo = mainUserInfos["terminal_id"]
        val uyeIsyeriNo = mainUserInfos["uye_isyeri_no"]
        val versionName = this.packageManager.getPackageInfo(this.packageName, 0).versionName
        val androidVersion = Build.VERSION.RELEASE
        val serialNumber = Build.SERIAL

        // Dialog Create
        val builder = MaterialAlertDialogBuilder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.pop_up_terminal_infos, null)

        // Dialog düzenini ve köşelerini yuvarlamak için
        builder.setView(dialogView)
        val dialog = builder.create()

        val TerminalNo = dialogView.findViewById<TextView>(R.id.textViewTerminalNo)
        val UyeNo = dialogView.findViewById<TextView>(R.id.textViewUyeNo)
        val AppVersNo = dialogView.findViewById<TextView>(R.id.textViewAppVersNo)
        val AndroidVers = dialogView.findViewById<TextView>(R.id.textViewAndroidVersNo)
        val SeriNo = dialogView.findViewById<TextView>(R.id.textViewSeriNo)

        TerminalNo.text = terminalNo.toString()
        UyeNo.text = uyeIsyeriNo.toString()
        AppVersNo.text = versionName
        AndroidVers.text = androidVersion
        SeriNo.text = serialNumber

        val okButton = dialogView.findViewById<Button>(R.id.buttonClose)

        okButton.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

}