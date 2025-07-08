package com.devmnv.digipinfinder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorites(favorites: Favorites)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<Favorites>

    @Query("DELETE FROM favorites WHERE digipin = :digipin")
    suspend fun deleteFromFavorites(digipin: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE digipin = :digipin)")
    suspend fun isFavorite(digipin: String): Boolean

}