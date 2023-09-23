package com.campuscoders.posterminalapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.presentation.login.views.LoginFragment
import com.campuscoders.posterminalapp.presentation.login.views.LoginTwoFragment
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.campuscoders.posterminalapp.utils.DatabaseInitializer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity() : AppCompatActivity() {

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    private var ftransaction: FragmentTransaction? = null

    private var logOut = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ftransaction = supportFragmentManager.beginTransaction()

        val intent = intent
        val isLoggedOut = intent.getStringExtra("logout")

        if (isLoggedOut == "logout") {
            setTheme(R.style.Base_Theme_PosTerminalApp)
            logOut = true
        } else {
            Thread.sleep(3000)
            val splashScreen = installSplashScreen()
        }

        setContentView(R.layout.activity_login)

        if (logOut) {
            ftransaction?.let {
                it.replace(R.id.fragmentContainerView,LoginTwoFragment())
                it.commit()
            }
        } else {
            ftransaction?.let {
                it.replace(R.id.fragmentContainerView,LoginFragment())
                it.commit()
            }
        }

        val customSharedPreferences = CustomSharedPreferences(this)
        if (customSharedPreferences.getControl()!!) {
            startDatabaseInitialization()
        }
    }

    private fun startDatabaseInitialization() {
        CoroutineScope(Dispatchers.Default).launch {
            databaseInitializer.initializeCategories(
                this@LoginActivity,
                "com.campuscoders.posterminalapp"
            )
            databaseInitializer.initializeProducts(
                this@LoginActivity,
                "com.campuscoders.posterminalapp"
            )
            databaseInitializer.initializeCustomer()
        }
    }
}