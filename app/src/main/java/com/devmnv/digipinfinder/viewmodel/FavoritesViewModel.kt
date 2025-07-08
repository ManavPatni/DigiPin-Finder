package com.devmnv.digipinfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devmnv.digipinfinder.database.Favorites
import com.devmnv.digipinfinder.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModelFactory(private val repository: FavoritesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel Class")
    }

}

class FavoritesViewModel(private val repository: FavoritesRepository): ViewModel() {

    private val _favorites = MutableStateFlow<List<Favorites>>(emptyList())
    val favorites = _favorites.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = repository.getAll()
        }
    }

    fun addFavorite(fav: Favorites) {
        viewModelScope.launch {
            repository.add(fav)
            loadFavorites()
        }
    }

    fun deleteFavorite(digipin: String) {
        viewModelScope.launch {
            repository.delete(digipin)
            loadFavorites()
        }
    }

    suspend fun isFavorite(digipin: String): Boolean {
        return repository.isFavorite(digipin)
    }

}