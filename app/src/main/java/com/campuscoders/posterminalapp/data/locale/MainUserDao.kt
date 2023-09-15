package com.campuscoders.posterminalapp.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.campuscoders.posterminalapp.domain.model.MainUser

@Dao
interface MainUserDao {
    @Insert
    suspend fun insertMainUser(mainUser: MainUser): Long  //primary id if success

    @Query("SELECT * FROM MainUser WHERE main_user_terminal_id = :terminalId")
    suspend fun queryMainUser(terminalId: String): MainUser?

    @Query("SELECT main_user_password FROM MainUser WHERE main_user_uye_isyeri_no = :memberStoreId")
    suspend fun queryMainUserWithMemberStoreId(memberStoreId: String): String?

    @Query("SELECT main_user_cellphone_number FROM MainUser WHERE main_user_vkn_tckn = :vknTckn")
    suspend fun queryMainUserWithVknTckn(vknTckn: String): String?

    @Query("UPDATE MainUser SET main_user_password = :newPassword WHERE main_user_vkn_tckn = :vknTckn")
    suspend fun updateMainUserPassword(vknTckn: String, newPassword: String): Int
}