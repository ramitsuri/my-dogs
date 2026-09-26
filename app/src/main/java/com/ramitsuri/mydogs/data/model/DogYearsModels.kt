package com.ramitsuri.mydogs.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SizeClass {
    @SerialName("small")
    SMALL,
    @SerialName("medium")
    MEDIUM,
    @SerialName("large")
    LARGE,
    @SerialName("giant")
    GIANT;

    companion object {
        fun fromString(value: String?): SizeClass {
            return when (value?.lowercase()) {
                "small" -> SMALL
                "medium" -> MEDIUM
                "large" -> LARGE
                "giant" -> GIANT
                else -> LARGE
            }
        }
    }

    val jsonKey: String
        get() = when (this) {
            SMALL -> "small"
            MEDIUM -> "medium"
            LARGE -> "large"
            GIANT -> "giant"
        }
}

@Serializable
data class AgePoint(
    @SerialName("dogAgeYears")
    val dogAgeYears: Double,
    @SerialName("humanYears")
    val humanYears: Double
)

@Serializable
data class SizeClassInfo(
    @SerialName("label")
    val label: String,
    @SerialName("weightRangeLb")
    val weightRangeLb: String,
    @SerialName("defaultLifespanYears")
    val defaultLifespanYears: Double,
    @SerialName("lifeStageChart")
    val lifeStageChart: List<AgePoint>
)

@Serializable
data class Breed(
    @SerialName("name")
    val name: String,
    @SerialName("lifespanYears")
    val lifespanYears: Double,
    @SerialName("sizeClass")
    val sizeClass: String
)

@Serializable
data class DogYearsDataset(
    @SerialName("sizeClasses")
    val sizeClasses: Map<String, SizeClassInfo>,
    @SerialName("breeds")
    val breeds: List<Breed>
)
