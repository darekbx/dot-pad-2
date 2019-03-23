package com.dotpad2.di

import com.dotpad2.di.viewmodels.ViewModelModule
import com.dotpad2.ui.archive.ArchiveActivity
import com.dotpad2.ui.dot.DotDialog
import com.dotpad2.ui.dots.DotsActivity
import com.dotpad2.ui.dots.list.DotsListFragment
import com.dotpad2.ui.statistics.StatisticsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, DatabaseModule::class, ViewModelModule::class))
interface AppComponent {

    fun inject(dotsActivity: DotsActivity)
    fun inject(dotDialog: DotDialog)
    fun inject(listFragment: DotsListFragment)
    fun inject(statisticsActivity: StatisticsActivity)
    fun inject(archiveActivity: ArchiveActivity)
}