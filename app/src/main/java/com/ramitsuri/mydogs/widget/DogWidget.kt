package com.ramitsuri.mydogs.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.ramitsuri.mydogs.MainActivity
import com.ramitsuri.mydogs.data.db.DogDatabase
import com.ramitsuri.mydogs.data.model.SizeClass
import com.ramitsuri.mydogs.domain.DogAgeCalculator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

data class DogWidgetModel(
    val id: String,
    val name: String,
    val formattedDogAge: String,
    val formattedBirthday: String,
    val formattedHumanAge: String,
    val formattedBreedAdjustedAge: String
)

class DogWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val calculator = DogAgeCalculator()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val dogs = try {
            DogDatabase.getDatabase(context).dogDao().getAllDogsList()
        } catch (_: Exception) {
            emptyList()
        }

        val now = System.currentTimeMillis()
        val dogModels = dogs.map { dog ->
            val diffMillis = now - dog.birthdayTimestamp
            val diffDays = diffMillis / (1000.0 * 60 * 60 * 24)
            val dogAgeYears = max(0.0, diffDays / 365.25)
            val sizeClass = SizeClass.fromString(dog.fallbackSizeClass)
            val ageResult = calculator.calculateDogAge(
                dogAgeYears = dogAgeYears,
                breedName = dog.breed,
                fallbackSizeClass = sizeClass
            )

            val formattedBirthday = dateFormatter.format(Date(dog.birthdayTimestamp))
            val formattedDogAge = if (dogAgeYears < 1.0) {
                val months = (dogAgeYears * 12).roundToInt()
                if (months <= 1) "1 month old" else "$months months old"
            } else {
                String.format(Locale.getDefault(), "%.1f years old", dogAgeYears)
            }
            val formattedHumanAge = "${ageResult.sizeChartHumanAge} human years"
            val formattedBreedAdjustedAge =
                "${ageResult.breedAdjustedHumanAge.roundToInt()} human years"

            DogWidgetModel(
                id = dog.id,
                name = dog.name,
                formattedDogAge = formattedDogAge,
                formattedBirthday = "Birthday: $formattedBirthday",
                formattedHumanAge = "Size Chart: $formattedHumanAge",
                formattedBreedAdjustedAge = formattedBreedAdjustedAge
            )
        }

        provideContent {
            DogWidgetContent(dogs = dogModels)
        }
    }

    @Composable
    private fun DogWidgetContent(dogs: List<DogWidgetModel>) {
        val size = LocalSize.current
        val isCompact = size.height < 140.dp || size.width < 180.dp

        val backgroundColor = ColorProvider(day = Color(0xFFFEF7FF), night = Color(0xFF141218))
        val surfaceVariantColor = ColorProvider(day = Color(0xFFE7E0EC), night = Color(0xFF49454F))
        val textColor = ColorProvider(day = Color(0xFF1D1B20), night = Color(0xFFE6E1E5))
        val primaryColor = ColorProvider(day = Color(0xFF6750A4), night = Color(0xFFD0BCFF))

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(backgroundColor)
                .cornerRadius(16.dp)
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>()),
            verticalAlignment = Alignment.Top
        ) {
            if (dogs.isEmpty()) {
                Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    Text(
                        text = "No dogs saved yet. Open app to add.",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = textColor
                        )
                    )
                }
            } else {
                LazyColumn(
                    modifier = GlanceModifier.fillMaxSize()
                ) {
                    itemsIndexed(dogs) { index, dog ->
                        Column(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(surfaceVariantColor)
                                .cornerRadius(12.dp)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = dog.name,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = textColor
                                )
                            )
                            Spacer(modifier = GlanceModifier.height(2.dp))
                            // Prominent Breed-Adjusted Age
                            Text(
                                text = dog.formattedBreedAdjustedAge,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = primaryColor
                                )
                            )

                            // Show extra details if not compact
                            if (!isCompact) {
                                Spacer(modifier = GlanceModifier.height(2.dp))
                                Text(
                                    text = dog.formattedDogAge,
                                    style = TextStyle(
                                        fontSize = 11.sp,
                                        color = textColor
                                    )
                                )
                                Spacer(modifier = GlanceModifier.height(2.dp))
                                Row(
                                    modifier = GlanceModifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Horizontal.Start
                                ) {
                                    Text(
                                        text = dog.formattedBirthday,
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            color = textColor
                                        )
                                    )
                                    Spacer(modifier = GlanceModifier.width(6.dp))
                                    Text(
                                        text = dog.formattedHumanAge,
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            color = textColor
                                        )
                                    )
                                }
                            }
                            if (index != dogs.lastIndex) {
                                Spacer(modifier = GlanceModifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
