package com.devmnv.digipinfinder.repository

import com.devmnv.digipinfinder.database.Favorites
import com.devmnv.digipinfinder.database.FavoritesDao

class FavoritesRepository(private val dao: FavoritesDao) {

    suspend fun add(fav: Favorites) = dao.addToFavorites(fav)

    suspend fun delete(digipin: String) = dao.deleteFromFavorites(digipin)

    suspend fun getAll(): List<Favorites> = dao.getAllFavorites()

    suspend fun isFavorite(digipin: String): Boolean = dao.isFavorite(digipin)
}