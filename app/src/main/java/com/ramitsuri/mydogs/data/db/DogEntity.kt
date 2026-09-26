package com.ramitsuri.mydogs.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "dogs")
data class DogEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val birthdayTimestamp: Long,
    val breed: String?,
    val fallbackSizeClass: String
)
