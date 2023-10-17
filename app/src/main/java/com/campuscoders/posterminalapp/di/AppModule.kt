package com.campuscoders.posterminalapp.di

import android.content.Context
import androidx.room.Room
import com.campuscoders.posterminalapp.data.locale.CategoriesDao
import com.campuscoders.posterminalapp.data.locale.CustomersDao
import com.campuscoders.posterminalapp.data.locale.MainUserDao
import com.campuscoders.posterminalapp.data.locale.OrdersDao
import com.campuscoders.posterminalapp.data.locale.OrdersProductsDao
import com.campuscoders.posterminalapp.data.locale.PosDatabase
import com.campuscoders.posterminalapp.data.locale.ProductsDao
import com.campuscoders.posterminalapp.data.locale.TerminalUsersDao
import com.campuscoders.posterminalapp.data.repository.locale.CashierAndReportRepositoryImpl
import com.campuscoders.posterminalapp.data.repository.locale.DocumentRepositoryImpl
import com.campuscoders.posterminalapp.data.repository.locale.EditRepositoryImpl
import com.campuscoders.posterminalapp.data.repository.locale.LoginRepositoryImpl
import com.campuscoders.posterminalapp.data.repository.locale.SaleRepositoryImpl
import com.campuscoders.posterminalapp.domain.repository.locale.CashierAndReportRepository
import com.campuscoders.posterminalapp.domain.repository.locale.DocumentRepository
import com.campuscoders.posterminalapp.domain.repository.locale.EditRepository
import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, PosDatabase::class.java,"PosDatabase"
    ).build()

    @Singleton
    @Provides
    fun provideMainUserDao(database: PosDatabase) = database.mainUserDao()

    @Singleton
    @Provides
    fun provideTerminalUsersDao(database: PosDatabase) = database.terminalUsersDao()

    @Singleton
    @Provides
    fun provideCategoriesDao(database: PosDatabase) = database.categoriesDao()

    @Singleton
    @Provides
    fun provideProductsDao(database: PosDatabase) = database.productsDao()

    @Singleton
    @Provides
    fun provideOrdersDao(database: PosDatabase) = database.ordersDao()

    @Singleton
    @Provides
    fun provideCustomersDao(database: PosDatabase) = database.customersDao()

    @Singleton
    @Provides
    fun provideOrdersProductsDao(database: PosDatabase) = database.ordersProductsDao()

    @Singleton
    @Provides
    fun provideLoginRepository(mainUserDao: MainUserDao, terminalUsersDao: TerminalUsersDao): LoginRepository {
        return LoginRepositoryImpl(mainUserDao, terminalUsersDao)
    }

    @Singleton
    @Provides
    fun provideSaleRepository(categoriesDao: CategoriesDao, productsDao: ProductsDao, ordersDao: OrdersDao, customersDao: CustomersDao, ordersProductsDao: OrdersProductsDao): SaleRepository {
        return SaleRepositoryImpl(categoriesDao,productsDao,ordersDao,customersDao,ordersProductsDao)
    }

    @Singleton
    @Provides
    fun provideEditRepository(categoriesDao: CategoriesDao, productsDao: ProductsDao): EditRepository {
        return EditRepositoryImpl(categoriesDao, productsDao)
    }

    @Singleton
    @Provides
    fun provideCashierAndReportRepository(terminalUsersDao: TerminalUsersDao): CashierAndReportRepository {
        return CashierAndReportRepositoryImpl(terminalUsersDao)
    }

    @Singleton
    @Provides
    fun provideDocumentRepository(ordersDao: OrdersDao, ordersProductsDao: OrdersProductsDao): DocumentRepository {
        return DocumentRepositoryImpl(ordersDao, ordersProductsDao)
    }
}