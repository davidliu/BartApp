package com.deviange.bart.dagger.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.deviange.bart.dagger.scope.PerActivity
import javax.inject.Inject

@PerActivity
class ViewModelFactory @Inject constructor(
    private val viewModelMap: MutableMap<Class<out ViewModel>, ViewModelAssistedFactory<out ViewModel>>,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return viewModelMap[modelClass]?.create(handle) as? T
            ?: throw IllegalStateException("Unknown ViewModel class")
    }
}