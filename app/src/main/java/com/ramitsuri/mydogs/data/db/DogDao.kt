package com.ramitsuri.mydogs.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {
    @Query("SELECT * FROM dogs")
    fun getAllDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs")
    suspend fun getAllDogsList(): List<DogEntity>

    @Query("SELECT * FROM dogs WHERE id = :id")
    suspend fun getDogById(id: String): DogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDog(dog: DogEntity)

    @Update
    suspend fun updateDog(dog: DogEntity)

    @Delete
    suspend fun deleteDog(dog: DogEntity)

    @Query("DELETE FROM dogs WHERE id = :id")
    suspend fun deleteDogById(id: String)
}
