package com.dotpad2.di

import android.content.Context
import com.dotpad2.DotPadApplication
import com.dotpad2.ui.dot.DotReminder
import com.dotpad2.repository.LocalPreferences
import com.dotpad2.repository.Repository
import com.dotpad2.repository.local.AppDatabase
import com.dotpad2.repository.local.LegacyAppDatabase
import com.dotpad2.utils.PermissionsHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: DotPadApplication) {

    @Provides
    @Singleton
    fun provideApplication(): DotPadApplication = application

    @Provides
    fun provideContext(): Context = application

    @Provides
    fun providePermissionsHelper() = PermissionsHelper()

    @Provides
    fun preferences(context: Context) = LocalPreferences(context)

    @Provides
    fun dotReminder(context: Context, localPreferences: LocalPreferences)
            = DotReminder(context, localPreferences)

    @Provides
    fun provideRepository(appDatabase: AppDatabase, legacyAppDatabase: LegacyAppDatabase): Repository =
        Repository(appDatabase.getDotsDao(), legacyAppDatabase.getLegacyDotsDao())
}