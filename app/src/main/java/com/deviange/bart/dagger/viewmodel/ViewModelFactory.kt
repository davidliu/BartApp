package com.deviange.bart.dagger.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.deviange.bart.dagger.scope.ActivityScope
import java.lang.IllegalArgumentException
import javax.inject.Inject

@ActivityScope
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
            ?: throw IllegalArgumentException("Unknown ViewModel class")
    }
}