package com.ramitsuri.mydogs.domain

import com.ramitsuri.mydogs.data.model.DogYearsDataset
import com.ramitsuri.mydogs.data.model.SizeClass
import kotlinx.serialization.json.Json
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.roundToInt

data class DogAgeResult(
    val dogAgeYears: Double,
    val breedName: String?,
    val fallbackSizeClass: SizeClass,
    val resolvedSizeClass: String,
    val resolvedBreedName: String?,
    val breedLifespanYears: Double,
    val breedAdjustedHumanAge: Double,
    val sizeChartHumanAge: Int,
    val lifeStage: String,
    val mythAge: Double
)

class DogAgeCalculator(
    private val dataset: DogYearsDataset = defaultDataset
) {
    fun resolve(breedNameOrNull: String?, fallbackSizeClass: SizeClass): ResolvedBreedInfo {
        val matchedBreed =
            dataset.breeds.find { it.name.equals(breedNameOrNull?.trim(), ignoreCase = true) }
        return if (matchedBreed != null) {
            ResolvedBreedInfo(
                breedName = matchedBreed.name,
                lifespanYears = matchedBreed.lifespanYears,
                sizeClassStr = matchedBreed.sizeClass,
                sizeClass = SizeClass.fromString(matchedBreed.sizeClass)
            )
        } else {
            val sizeInfo =
                dataset.sizeClasses[fallbackSizeClass.jsonKey] ?: dataset.sizeClasses["large"]!!
            ResolvedBreedInfo(
                breedName = null,
                lifespanYears = sizeInfo.defaultLifespanYears,
                sizeClassStr = fallbackSizeClass.jsonKey,
                sizeClass = fallbackSizeClass
            )
        }
    }

    fun breedAdjustedHumanAge(dogAgeYears: Double, breedLifespanYears: Double): Double {
        val referenceLifespanYears = 12.0
        val minAgeYears = 0.08
        val effectiveAge = max(minAgeYears, dogAgeYears)
        val adjusted = effectiveAge * (referenceLifespanYears / breedLifespanYears)
        val result = 16.0 * ln(adjusted) + 31.0
        return max(0.0, result)
    }

    fun sizeChartHumanAge(dogAgeYears: Double, sizeClass: SizeClass): Int {
        val sizeInfo = dataset.sizeClasses[sizeClass.jsonKey] ?: dataset.sizeClasses["large"]!!
        val chart = sizeInfo.lifeStageChart.sortedBy { it.dogAgeYears }
        if (chart.isEmpty()) return (dogAgeYears * 7).roundToInt()

        if (dogAgeYears <= chart.first().dogAgeYears) {
            return chart.first().humanYears.roundToInt()
        }
        if (dogAgeYears >= chart.last().dogAgeYears) {
            val p1 = chart[chart.size - 2]
            val p2 = chart.last()
            val slope = (p2.humanYears - p1.humanYears) / (p2.dogAgeYears - p1.dogAgeYears)
            val ext = p2.humanYears + (dogAgeYears - p2.dogAgeYears) * slope
            return max(0.0, ext).roundToInt()
        }

        for (i in 0 until chart.size - 1) {
            val p1 = chart[i]
            val p2 = chart[i + 1]
            if (dogAgeYears >= p1.dogAgeYears && dogAgeYears <= p2.dogAgeYears) {
                if (p2.dogAgeYears == p1.dogAgeYears) return p1.humanYears.roundToInt()
                val fraction = (dogAgeYears - p1.dogAgeYears) / (p2.dogAgeYears - p1.dogAgeYears)
                val interpolated = p1.humanYears + fraction * (p2.humanYears - p1.humanYears)
                return max(0.0, interpolated).roundToInt()
            }
        }
        return (dogAgeYears * 7).roundToInt()
    }

    fun lifeStage(dogAgeYears: Double, breedLifespanYears: Double): String {
        if (breedLifespanYears <= 0.0) return "adult"
        val fraction = dogAgeYears / breedLifespanYears
        return when {
            fraction < 0.15 -> "puppy"
            fraction < 0.75 -> "adult"
            else -> "senior"
        }
    }

    fun mythAge(dogAgeYears: Double): Double {
        return dogAgeYears * 7.0
    }

    fun calculateDogAge(
        dogAgeYears: Double,
        breedName: String?,
        fallbackSizeClass: SizeClass
    ): DogAgeResult {
        val resolved = resolve(breedName, fallbackSizeClass)
        val adjAge = breedAdjustedHumanAge(dogAgeYears, resolved.lifespanYears)
        val chartAge = sizeChartHumanAge(dogAgeYears, resolved.sizeClass)
        val stage = lifeStage(dogAgeYears, resolved.lifespanYears)
        val myth = mythAge(dogAgeYears)

        return DogAgeResult(
            dogAgeYears = dogAgeYears,
            breedName = breedName,
            fallbackSizeClass = fallbackSizeClass,
            resolvedSizeClass = resolved.sizeClassStr,
            resolvedBreedName = resolved.breedName,
            breedLifespanYears = resolved.lifespanYears,
            breedAdjustedHumanAge = adjAge,
            sizeChartHumanAge = chartAge,
            lifeStage = stage,
            mythAge = myth
        )
    }

    companion object {
        private val jsonParser = Json { ignoreUnknownKeys = true }

        val defaultDataset: DogYearsDataset by lazy {
            jsonParser.decodeFromString<DogYearsDataset>(jsonStr)
        }
    }
}

data class ResolvedBreedInfo(
    val breedName: String?,
    val lifespanYears: Double,
    val sizeClassStr: String,
    val sizeClass: SizeClass
)
