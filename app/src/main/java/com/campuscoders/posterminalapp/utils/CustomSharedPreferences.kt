package com.campuscoders.posterminalapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.campuscoders.posterminalapp.R

class CustomSharedPreferences {

    companion object {
        private const val PREFERENCE_MAIN_USER_SAVE = "main_user_save"
        private const val PREFERENCE_MAIN_USER_LOGIN = "main_user_login"
        private const val PREFERENCE_TERMINAL_USER_LOGIN = "terminal_user_login"

        private const val NULL = "null"

        private var sharedPreferences: SharedPreferences? = null
        private var sharedPreferencesLogin: SharedPreferences? = null
        private var sharedPreferencesLoginTerminalUser: SharedPreferences? = null

        @Volatile private var instance: CustomSharedPreferences? = null

        operator fun invoke(context: Context): CustomSharedPreferences = instance ?: synchronized(Any()) {
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferencesLoginTerminalUser = context.getSharedPreferences(PREFERENCE_TERMINAL_USER_LOGIN,Context.MODE_PRIVATE)
            sharedPreferencesLogin = context.getSharedPreferences(PREFERENCE_MAIN_USER_LOGIN,Context.MODE_PRIVATE)
            sharedPreferences = context.getSharedPreferences(PREFERENCE_MAIN_USER_SAVE,Context.MODE_PRIVATE)
            return CustomSharedPreferences()
        }
    }

    // TERMINAL USER
    fun setTerminalUserLogin(terminalId: String, vknTckn: String, memberStoreId: String, password: String,
                             fullName: String, date: String, time: String, iptalIade: Boolean,
                             tahsilat: Boolean, kasiyerGoruntuleme: Boolean, kasiyerEklemeDuzenleme: Boolean,
                             kasiyerSilme: Boolean, urunGoruntuleme: Boolean, urunEklemeDuzenleme: Boolean,
                             urunSilme: Boolean, tumRaporlariGoruntuleme: Boolean, raporKaydetGonder: Boolean,
                             posYonetimi: Boolean, admin: Boolean, context: Context) {
        sharedPreferencesLoginTerminalUser?.edit(commit = true) {
            putString(context.getString(R.string.user_terminal_id),terminalId)
            putString(context.getString(R.string.user_vkn_tckn),vknTckn)
            putString(context.getString(R.string.user_uye_isyeri_no),memberStoreId)
            putString(context.getString(R.string.user_password), password)
            putString(context.getString(R.string.user_full_name),fullName)
            putString(context.getString(R.string.user_date),date)
            putString(context.getString(R.string.user_time),time)
            putBoolean(context.getString(R.string.user_iptal_iade),iptalIade)
            putBoolean(context.getString(R.string.user_tahsilat),tahsilat)
            putBoolean(context.getString(R.string.user_kasiyer_goruntuleme),kasiyerGoruntuleme)
            putBoolean(context.getString(R.string.user_kasiyer_ekleme_duzenleme),kasiyerEklemeDuzenleme)
            putBoolean(context.getString(R.string.user_kasiyer_silme),kasiyerSilme)
            putBoolean(context.getString(R.string.user_urun_goruntuleme),urunGoruntuleme)
            putBoolean(context.getString(R.string.user_urun_ekleme_duzenleme),urunEklemeDuzenleme)
            putBoolean(context.getString(R.string.user_urun_silme),urunSilme)
            putBoolean(context.getString(R.string.user_tum_raporları_goruntuleme),tumRaporlariGoruntuleme)
            putBoolean(context.getString(R.string.user_rapor_kaydet_gonder),raporKaydetGonder)
            putBoolean(context.getString(R.string.user_pos_yonetimi),posYonetimi)
            putBoolean(context.getString(R.string.user_admin),admin)
        }
    }
    fun getTerminalUserLogin(context: Context): HashMap<String,Any> {
        val hashMap = hashMapOf<String,Any>()
        hashMap[context.getString(R.string.user_terminal_id)] = sharedPreferencesLoginTerminalUser?.getString(context.getString(R.string.user_terminal_id),NULL)?:NULL
        hashMap[context.getString(R.string.user_vkn_tckn)] = sharedPreferencesLoginTerminalUser?.getString(context.getString(R.string.user_vkn_tckn),NULL)?:NULL
        hashMap[context.getString(R.string.user_uye_isyeri_no)] = sharedPreferencesLoginTerminalUser?.getString(context.getString(R.string.user_uye_isyeri_no),NULL)?:NULL
        hashMap[context.getString(R.string.user_password)] = sharedPreferencesLoginTerminalUser?.getString(context.getString(R.string.user_password),NULL)?: NULL
        hashMap[context.getString(R.string.user_full_name)] = sharedPreferencesLoginTerminalUser?.getString(context.getString(R.string.user_full_name),NULL)?:NULL
        hashMap[context.getString(R.string.user_date)] = sharedPreferencesLoginTerminalUser?.getString(context.getString(R.string.user_date),NULL)?:NULL
        hashMap[context.getString(R.string.user_time)] = sharedPreferencesLoginTerminalUser?.getString(context.getString(R.string.user_time),NULL)?:NULL
        hashMap[context.getString(R.string.user_iptal_iade)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_iptal_iade),false)?:NULL
        hashMap[context.getString(R.string.user_tahsilat)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_tahsilat),false)?:NULL
        hashMap[context.getString(R.string.user_kasiyer_goruntuleme)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_kasiyer_goruntuleme),false)?:NULL
        hashMap[context.getString(R.string.user_kasiyer_ekleme_duzenleme)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_kasiyer_ekleme_duzenleme),false)?:NULL
        hashMap[context.getString(R.string.user_kasiyer_silme)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_kasiyer_silme),false)?:NULL
        hashMap[context.getString(R.string.user_urun_goruntuleme)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_urun_goruntuleme),false)?:NULL
        hashMap[context.getString(R.string.user_urun_ekleme_duzenleme)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_urun_ekleme_duzenleme),false)?:NULL
        hashMap[context.getString(R.string.user_urun_silme)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_urun_silme),false)?:NULL
        hashMap[context.getString(R.string.user_tum_raporları_goruntuleme)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_tum_raporları_goruntuleme),false)?:NULL
        hashMap[context.getString(R.string.user_rapor_kaydet_gonder)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_rapor_kaydet_gonder),false)?:NULL
        hashMap[context.getString(R.string.user_pos_yonetimi)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_pos_yonetimi),false)?:NULL
        hashMap[context.getString(R.string.user_admin)] = sharedPreferencesLoginTerminalUser?.getBoolean(context.getString(R.string.user_admin),false)?:NULL
        return hashMap
    }

    // MAIN USER
    fun setMainUserLogin(terminalId: String, vknTckn: String, memberStoreId: String, password: String, context: Context) {
        sharedPreferencesLogin?.edit(commit = true) {
            putString(context.getString(R.string.user_terminal_id),terminalId)
            putString(context.getString(R.string.user_vkn_tckn),vknTckn)
            putString(context.getString(R.string.user_uye_isyeri_no),memberStoreId)
            putString(context.getString(R.string.user_password),password)
        }
    }
    fun setMainUserLoginRememberMeManager(rememberMeManager: Boolean, context: Context) {
        sharedPreferencesLogin?.edit(commit = true) {
            putBoolean(context.getString(R.string.user_remember_me_manager),rememberMeManager)
        }
    }
    fun setMainUserLoginRememberMeCashier(rememberMeTerminal: Boolean, context: Context) {
        sharedPreferencesLogin?.edit(commit = true) {
            putBoolean(context.getString(R.string.user_remember_me_terminal),rememberMeTerminal)
        }
    }
    fun getMainUserLogin(context: Context): HashMap<String,Any> {
        val hashMap = hashMapOf<String,Any>()
        hashMap[context.getString(R.string.user_terminal_id)] = sharedPreferencesLogin?.getString(context.getString(R.string.user_terminal_id),NULL)?:NULL
        hashMap[context.getString(R.string.user_vkn_tckn)] = sharedPreferencesLogin?.getString(context.getString(R.string.user_vkn_tckn),NULL)?:NULL
        hashMap[context.getString(R.string.user_uye_isyeri_no)] = sharedPreferencesLogin?.getString(context.getString(R.string.user_uye_isyeri_no),NULL)?:NULL
        hashMap[context.getString(R.string.user_password)] = sharedPreferencesLogin?.getString(context.getString(R.string.user_password), NULL)?:NULL
        hashMap[context.getString(R.string.user_remember_me_manager)] = sharedPreferencesLogin?.getBoolean(context.getString(R.string.user_remember_me_manager),false)?: NULL
        hashMap[context.getString(R.string.user_remember_me_terminal)] = sharedPreferencesLogin?.getBoolean(context.getString(R.string.user_remember_me_terminal),false)?: NULL
        return hashMap
    }

    // SAVE MAIN USER
    fun setControl(control: Boolean) {
        sharedPreferences?.edit(commit = true) {
            putBoolean("control",control)
        }
    }
    fun getControl() = sharedPreferences?.getBoolean("control",true)
}