package com.dotpad2.di.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotpad2.viewmodels.DotViewModel
import com.dotpad2.viewmodels.StatisticsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(DotViewModel::class)
    internal abstract fun bindDotViewModel(viewModel: DotViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(StatisticsViewModel::class)
    internal abstract fun bindStatisticsViewModel(viewModel: StatisticsViewModel): ViewModel
}