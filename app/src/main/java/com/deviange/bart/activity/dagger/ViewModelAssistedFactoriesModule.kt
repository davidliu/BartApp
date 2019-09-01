package com.deviange.bart.activity.dagger

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.deviange.bart.dagger.viewmodel.ViewModelAssistedFactory
import com.deviange.bart.dagger.viewmodel.ViewModelKey
import com.deviange.bart.stations.StationsViewModel
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@AssistedModule
@Module(includes = [AssistedInject_ViewModelAssistedFactoriesModule::class])
abstract class ViewModelAssistedFactoriesModule {

    @Binds
    @IntoMap
    @ViewModelKey(StationsViewModel::class)
    abstract fun bindStationsViewModelFactory(factory: StationsViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    abstract fun bindSavedStateRegistryOwner(activity: AppCompatActivity): SavedStateRegistryOwner
}