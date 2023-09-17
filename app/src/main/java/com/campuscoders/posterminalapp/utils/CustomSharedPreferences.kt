package com.campuscoders.posterminalapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CustomSharedPreferences {

    companion object {
        private const val PREFERENCE_MAIN_USER_SAVE = "main_user_save"
        private const val PREFERENCE_MAIN_USER_LOGIN = "main_user_login"
        private const val PREFERENCE_TERMINAL_USER_LOGIN = "terminal_user_login"
        // Fields
        private const val USER_TERMINAL_ID = "terminal_id"
        private const val USER_VKN_TCKN = "vkn_tckn"
        private const val USER_UYE_ISYERI_NO = "uye_isyeri_no"
        private const val USER_PASSWORD = "password"
        private const val USER_FULL_NAME = "full_name"
        private const val USER_DATE = "date"
        private const val USER_TIME = "time"
        private const val USER_IPTAL_IADE = "iptal_iade"
        private const val USER_TAHSILAT = "tahsilat"
        private const val USER_KASIYER_GORUNTULEME = "kasiyer_goruntuleme"
        private const val USER_KASIYER_EKLEME_DUZENLEME = "kasiyer_ekleme_duzenleme"
        private const val USER_KASIYER_SILME = "kasiyer_silme"
        private const val USER_URUN_GORUNTULEME = "urun_goruntuleme"
        private const val USER_URUN_EKLEME_DUZENLEME = "urun_ekleme_duzenleme"
        private const val USER_URUN_SILME = "urun_silme"
        private const val USER_TUM_RAPORLARI_GORUNTULEME = "tum_raporları_goruntuleme"
        private const val USER_RAPOR_KAYDET_GONDER = "rapor_kaydet_gonder"
        private const val USER_POS_YONETIMI = "pos_yonetimi"
        private const val USER_ADMIN = "admin"
        private const val REMEMBER_ME_MANAGER = "remember_me_manager"
        private const val REMEMBER_ME_TERMINAL = "remember_me_terminal"
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
                             posYonetimi: Boolean, admin: Boolean) {
        sharedPreferencesLoginTerminalUser?.edit(commit = true) {
            putString(USER_TERMINAL_ID,terminalId)
            putString(USER_VKN_TCKN,vknTckn)
            putString(USER_UYE_ISYERI_NO,memberStoreId)
            putString(USER_PASSWORD, password)
            putString(USER_FULL_NAME,fullName)
            putString(USER_DATE,date)
            putString(USER_TIME,time)
            putBoolean(USER_IPTAL_IADE,iptalIade)
            putBoolean(USER_TAHSILAT,tahsilat)
            putBoolean(USER_KASIYER_GORUNTULEME,kasiyerGoruntuleme)
            putBoolean(USER_KASIYER_EKLEME_DUZENLEME,kasiyerEklemeDuzenleme)
            putBoolean(USER_KASIYER_SILME,kasiyerSilme)
            putBoolean(USER_URUN_GORUNTULEME,urunGoruntuleme)
            putBoolean(USER_URUN_EKLEME_DUZENLEME,urunEklemeDuzenleme)
            putBoolean(USER_URUN_SILME,urunSilme)
            putBoolean(USER_TUM_RAPORLARI_GORUNTULEME,tumRaporlariGoruntuleme)
            putBoolean(USER_RAPOR_KAYDET_GONDER,raporKaydetGonder)
            putBoolean(USER_POS_YONETIMI,posYonetimi)
            putBoolean(USER_ADMIN,admin)
        }
    }
    fun getTerminalUserLogin(): HashMap<String,Any> {
        val hashMap = hashMapOf<String,Any>()
        hashMap[USER_TERMINAL_ID] = sharedPreferencesLoginTerminalUser?.getString(USER_TERMINAL_ID,NULL)?:NULL
        hashMap[USER_VKN_TCKN] = sharedPreferencesLoginTerminalUser?.getString(USER_VKN_TCKN,NULL)?:NULL
        hashMap[USER_UYE_ISYERI_NO] = sharedPreferencesLoginTerminalUser?.getString(USER_UYE_ISYERI_NO,NULL)?:NULL
        hashMap[USER_PASSWORD] = sharedPreferencesLoginTerminalUser?.getString(USER_PASSWORD,NULL)?: NULL
        hashMap[USER_FULL_NAME] = sharedPreferencesLoginTerminalUser?.getString(USER_FULL_NAME,NULL)?:NULL
        hashMap[USER_DATE] = sharedPreferencesLoginTerminalUser?.getString(USER_DATE,NULL)?:NULL
        hashMap[USER_TIME] = sharedPreferencesLoginTerminalUser?.getString(USER_TIME,NULL)?:NULL
        hashMap[USER_IPTAL_IADE] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_IPTAL_IADE,false)?:NULL
        hashMap[USER_TAHSILAT] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_TAHSILAT,false)?:NULL
        hashMap[USER_KASIYER_GORUNTULEME] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_KASIYER_GORUNTULEME,false)?:NULL
        hashMap[USER_KASIYER_EKLEME_DUZENLEME] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_KASIYER_EKLEME_DUZENLEME,false)?:NULL
        hashMap[USER_KASIYER_SILME] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_KASIYER_SILME,false)?:NULL
        hashMap[USER_URUN_GORUNTULEME] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_URUN_GORUNTULEME,false)?:NULL
        hashMap[USER_URUN_EKLEME_DUZENLEME] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_URUN_EKLEME_DUZENLEME,false)?:NULL
        hashMap[USER_URUN_SILME] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_URUN_SILME,false)?:NULL
        hashMap[USER_TUM_RAPORLARI_GORUNTULEME] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_TUM_RAPORLARI_GORUNTULEME,false)?:NULL
        hashMap[USER_RAPOR_KAYDET_GONDER] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_RAPOR_KAYDET_GONDER,false)?:NULL
        hashMap[USER_POS_YONETIMI] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_POS_YONETIMI,false)?:NULL
        hashMap[USER_ADMIN] = sharedPreferencesLoginTerminalUser?.getBoolean(USER_ADMIN,false)?:NULL
        return hashMap
    }

    // MAIN USER
    fun setMainUserLogin(terminalId: String, vknTckn: String, memberStoreId: String, password: String) {
        sharedPreferencesLogin?.edit(commit = true) {
            putString(USER_TERMINAL_ID,terminalId)
            putString(USER_VKN_TCKN,vknTckn)
            putString(USER_UYE_ISYERI_NO,memberStoreId)
            putString(USER_PASSWORD,password)
        }
    }
    fun setMainUserLoginRememberMeManager(rememberMeManager: Boolean) {
        sharedPreferencesLogin?.edit(commit = true) {
            putBoolean(REMEMBER_ME_MANAGER,rememberMeManager)
        }
    }
    fun setMainUserLoginRememberMeCashier(rememberMeTerminal: Boolean) {
        sharedPreferencesLogin?.edit(commit = true) {
            putBoolean(REMEMBER_ME_TERMINAL,rememberMeTerminal)
        }
    }
    fun getMainUserLogin(): HashMap<String,Any> {
        val hashMap = hashMapOf<String,Any>()
        hashMap[USER_TERMINAL_ID] = sharedPreferencesLogin?.getString(USER_TERMINAL_ID,NULL)?:NULL
        hashMap[USER_VKN_TCKN] = sharedPreferencesLogin?.getString(USER_VKN_TCKN,NULL)?:NULL
        hashMap[USER_UYE_ISYERI_NO] = sharedPreferencesLogin?.getString(USER_UYE_ISYERI_NO,NULL)?:NULL
        hashMap[USER_PASSWORD] = sharedPreferencesLogin?.getString(USER_PASSWORD, NULL)?:NULL
        hashMap[REMEMBER_ME_MANAGER] = sharedPreferencesLogin?.getBoolean(REMEMBER_ME_MANAGER,false)?: NULL
        hashMap[REMEMBER_ME_TERMINAL] = sharedPreferencesLogin?.getBoolean(REMEMBER_ME_TERMINAL,false)?: NULL
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