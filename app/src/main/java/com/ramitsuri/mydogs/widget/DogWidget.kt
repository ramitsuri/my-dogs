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
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.ramitsuri.mydogs.MainActivity
import com.ramitsuri.mydogs.data.db.DogEntity
import com.ramitsuri.mydogs.data.model.SizeClass
import com.ramitsuri.mydogs.domain.DogAgeCalculator
import com.ramitsuri.mydogs.domain.formatAge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.max

@Serializable
data class DogWidgetModel(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("formattedDogAge")
    val formattedDogAge: String,
    @SerialName("formattedBirthday")
    val formattedBirthday: String,
    @SerialName("formattedBreedAdjustedAge")
    val formattedBreedAdjustedAge: String
)

@Serializable
data class WidgetState(
    @SerialName("dogs")
    val dogs: List<DogWidgetModel> = listOf(),
)

class DogWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact
    override val stateDefinition = WidgetDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<WidgetState>()
            DogWidgetContent(state)
        }
    }

    @Composable
    private fun DogWidgetContent(state: WidgetState) {
        val size = LocalSize.current
        val isCompact = size.height < 140.dp || size.width < 180.dp

        val backgroundColor = ColorProvider(day = Color(0xFFFEF7FF), night = Color(0xFF141218))
        val surfaceVariantColor = ColorProvider(day = Color(0xFFE7E0EC), night = Color(0xFF49454F))
        val textColor = ColorProvider(day = Color(0xFF1D1B20), night = Color(0xFFE6E1E5))
        val primaryColor = ColorProvider(day = Color(0xFF6750A4), night = Color(0xFFD0BCFF))
        val dogs = state.dogs

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
                                Text(
                                    text = dog.formattedBirthday,
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = textColor
                                    )
                                )
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

    companion object {
        suspend fun Context.updateWidget(
            dogs: List<DogEntity>,
        ) {
            val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val calculator = DogAgeCalculator()
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
                val formattedDogAge = formatAge(dogAgeYears)
                val formattedBreedAdjustedAge = formatAge(ageResult.breedAdjustedHumanAge)

                DogWidgetModel(
                    id = dog.id,
                    name = dog.name,
                    formattedDogAge = formattedDogAge,
                    formattedBirthday = "Birthday: $formattedBirthday",
                    formattedBreedAdjustedAge = formattedBreedAdjustedAge
                )
            }
            val appWidgetManager = GlanceAppWidgetManager(this)
            appWidgetManager.getGlanceIds(DogWidget::class.java).forEach { glanceId ->
                updateAppWidgetState(
                    context = this,
                    definition = WidgetDefinition,
                    glanceId = glanceId,
                    updateState = {
                        WidgetState(dogModels)
                    },
                )
                DogWidget().update(this, glanceId)
            }
        }
    }
}
