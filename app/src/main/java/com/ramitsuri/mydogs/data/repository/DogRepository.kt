package com.ramitsuri.mydogs.data.repository

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.ramitsuri.mydogs.data.db.DogDao
import com.ramitsuri.mydogs.data.db.DogEntity
import com.ramitsuri.mydogs.widget.DogWidget
import kotlinx.coroutines.flow.Flow

class DogRepository(
    private val context: Context,
    private val dogDao: DogDao
) {
    val allDogs: Flow<List<DogEntity>> = dogDao.getAllDogs()

    suspend fun insert(dog: DogEntity) {
        dogDao.insertDog(dog)
        updateWidget()
    }

    suspend fun update(dog: DogEntity) {
        dogDao.updateDog(dog)
        updateWidget()
    }

    suspend fun delete(dog: DogEntity) {
        dogDao.deleteDog(dog)
        updateWidget()
    }

    private suspend fun updateWidget() {
        try {
            DogWidget().updateAll(context)
        } catch (_: Exception) {
            // Ignore if widget is not active or added yet
        }
    }
}
