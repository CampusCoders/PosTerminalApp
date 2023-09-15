package com.campuscoders.posterminalapp.data.repository.locale

import com.campuscoders.posterminalapp.data.locale.MainUserDao
import com.campuscoders.posterminalapp.data.locale.TerminalUsersDao
import com.campuscoders.posterminalapp.domain.model.MainUser
import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val mainUserDao: MainUserDao,
    private val terminalUsersDao: TerminalUsersDao
): LoginRepository {

    override suspend fun saveMainUserToDatabase(mainUser: MainUser): Long {
        return mainUserDao.insertMainUser(mainUser)
    }

    override suspend fun fetchMainUserFromDatabase(terminalId: String): MainUser? {
        return mainUserDao.queryMainUser(terminalId)
    }

    override suspend fun fetchMainUserPassword(memberStoreId: String): String? {
        return mainUserDao.queryMainUserWithMemberStoreId(memberStoreId)
    }

    override suspend fun saveTerminalUserToDatabase(terminalUser: TerminalUsers): Long {
        return terminalUsersDao.insertTerminalUser(terminalUser)
    }

    override suspend fun fetchTerminalUserFromDatabase(terminalId: String): TerminalUsers? {
        return terminalUsersDao.queryTerminalUser(terminalId)
    }

    override suspend fun fetchTerminalUserFromDatabaseByMemberStoreId(memberStoreId: String): TerminalUsers? {
        return terminalUsersDao.queryTerminalUserByMemberStoreId(memberStoreId)
    }

    override suspend fun fetchTerminalUserPassword(terminalId: String): String? {
        return terminalUsersDao.queryTerminalUserForPassword(terminalId)
    }

    override suspend fun fetchMainUserCellPhoneNumber(vknTckn: String): String? {
        return mainUserDao.queryMainUserWithVknTckn(vknTckn)
    }

    override suspend fun updateMainUserPassword(vknTckn: String, newPassword: String): Int {
        return mainUserDao.updateMainUserPassword(vknTckn, newPassword)
    }

    override suspend fun updateTerminalUserPassword(vknTckn: String, newPassword: String): Int {
        return terminalUsersDao.updateTerminalUserPassword(vknTckn, newPassword)
    }
}