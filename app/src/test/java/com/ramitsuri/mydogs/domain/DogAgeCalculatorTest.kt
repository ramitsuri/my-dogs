package com.ramitsuri.mydogs.domain

import com.ramitsuri.mydogs.data.model.SizeClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class DogAgeCalculatorTest {

    private val calculator = DogAgeCalculator()

    @Test
    fun testGreatDaneWorkedExample() {
        val result = calculator.calculateDogAge(
            dogAgeYears = 3.0,
            breedName = "Great Dane",
            fallbackSizeClass = SizeClass.LARGE,
        )

        assertEquals("Great Dane", result.resolvedBreedName)
        assertEquals("giant", result.resolvedSizeClass)
        // breedAdjustedHumanAge ≈ 55.1
        assertTrue("Expected approx 55.1, got ${result.breedAdjustedHumanAge}", abs(result.breedAdjustedHumanAge - 55.1) < 0.2)
        // sizeChartHumanAge = 31
        assertEquals(31, result.sizeChartHumanAge)
        // lifeStage = "adult"
        assertEquals("adult", result.lifeStage)
    }

    @Test
    fun testShibaInuWorkedExample() {
        val result = calculator.calculateDogAge(
            dogAgeYears = 3.0,
            breedName = "Shiba Inu",
            fallbackSizeClass = SizeClass.SMALL,
        )

        assertEquals("Shiba Inu", result.resolvedBreedName)
        assertEquals("small", result.resolvedSizeClass)
        // breedAdjustedHumanAge ≈ 45.0 (3 * 12/15 = 2.4, 16 * ln(2.4) + 31 ≈ 45.0)
        assertTrue("Expected approx 45.0, got ${result.breedAdjustedHumanAge}", abs(result.breedAdjustedHumanAge - 45.0) < 0.2)
        assertEquals(28, result.sizeChartHumanAge)
        assertEquals("adult", result.lifeStage)
    }

    @Test
    fun testFallbackSizeClassWhenBreedNotFound() {
        val result = calculator.calculateDogAge(
            dogAgeYears = 2.0,
            breedName = "Unknown Breed",
            fallbackSizeClass = SizeClass.SMALL,
        )

        assertEquals(null, result.resolvedBreedName)
        assertEquals("small", result.resolvedSizeClass)
        assertEquals("puppy", result.lifeStage) // 2 / 14 = ~0.142 < 0.15
    }

    @Test
    fun testLifeStages() {
        // Puppy: fraction < 0.15
        assertEquals("puppy", calculator.lifeStage(1.0, 10.0)) // 1/10 = 0.1
        // Adult: 0.15 <= fraction < 0.75
        assertEquals("adult", calculator.lifeStage(3.0, 10.0)) // 3/10 = 0.3
        // Senior: fraction >= 0.75
        assertEquals("senior", calculator.lifeStage(8.0, 10.0)) // 8/10 = 0.8
    }

    @Test
    fun testMythAge() {
        assertEquals(21.0, calculator.mythAge(3.0), 0.001)
    }
}
