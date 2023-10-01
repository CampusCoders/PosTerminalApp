package com.campuscoders.posterminalapp.presentation

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.ActivityMainBinding
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.cspos.BuildConfig
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, CashierActivity::class.java)

        binding.imageViewMenu.setOnClickListener {
            binding.drawerParent.openDrawer(GravityCompat.END)
        }

        binding.menulayout.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemDailyReport -> {
                    intent.putExtra("navigation", "1")
                    startActivity(intent)
                }
                R.id.itemCashierOperations -> {
                    intent.putExtra("navigation", "2")
                    startActivity(intent)
                }
                R.id.itemTerminalInformations -> {
                    showTerminalPopup()
                }
            }
            binding.drawerParent.closeDrawer(GravityCompat.END)
            true
        }

    }

    private fun showTerminalPopup() {
        val customSharedPreferences = CustomSharedPreferences(this)
        val mainUserInfos = customSharedPreferences.getMainUserLogin()
        val terminalNo = mainUserInfos["terminal_id"]
        val uyeIsyeriNo = mainUserInfos["uye_isyeri_no"]
        val versionName = BuildConfig.VERSION_NAME
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