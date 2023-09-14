package com.campuscoders.posterminalapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "TerminalUsers")
data class TerminalUsers(
    @ColumnInfo(name = "terminal_user_terminal_id") var terminalUserTerminalId: String? = null,  //FK
    @ColumnInfo(name = "terminal_user_vkn_tckn") var terminalUserVknTckn: String? = null,
    @ColumnInfo(name = "terminal_user_uye_isyeri_no") var terminalUserUyeIsyeriNo: String? = null,
    @ColumnInfo(name = "terminal_user_full_name") var terminalUserFullName: String? = null,
    @ColumnInfo(name = "terminal_user_password") var terminalUserPassword: String? = null,
    @ColumnInfo(name = "terminal_user_date") var terminalUserDate: String? = null,
    @ColumnInfo(name = "terminal_user_time") var terminalUserTime: String? = null,
    @ColumnInfo(name = "terminal_user_iptal_iade") var terminalUserIptalIade: Boolean? = null,
    @ColumnInfo(name = "terminal_user_tahsilat") var terminalUserTahsilat: Boolean? = null,
    @ColumnInfo(name = "terminal_user_kasiyer_goruntuleme") var terminalUserKasiyerGoruntuleme: Boolean? = null,
    @ColumnInfo(name = "terminal_user_kasiyer_ekleme_duzenleme") var terminalUserKasiyerEklemeDuzenleme: Boolean? = null,
    @ColumnInfo(name = "terminal_user_kasiyer_silme") var terminalUserKasiyerSilme: Boolean? = null,
    @ColumnInfo(name = "terminal_user_urun_goruntuleme") var terminalUserUrunGoruntuleme: Boolean? = null,
    @ColumnInfo(name = "terminal_user_urun_ekleme_duzenleme") var terminalUserUrunEklemeDuzenleme: Boolean? = null,
    @ColumnInfo(name = "terminal_user_urun_silme") var terminalUserUrunSilme: Boolean? = null,
    @ColumnInfo(name = "terminal_user_tum_raporları_goruntuleme") var terminalUserTumRaporlariGoruntule: Boolean? = null,
    @ColumnInfo(name = "terminal_user_rapor_kaydet_gonder") var terminalUserRaporKaydetGonder: Boolean? = null,
    @ColumnInfo(name = "terminal_user_pos_yonetimi") var terminalUserPosYonetimi: Boolean? = null,
    @ColumnInfo(name = "terminal_user_admin") var terminalUserAdmin: Boolean? = null
) {
    @PrimaryKey(autoGenerate = true)
    var terminalUserId: Int = 0
}
