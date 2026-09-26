package com.ramitsuri.mydogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ramitsuri.mydogs.data.db.DogDatabase
import com.ramitsuri.mydogs.data.repository.DogRepository
import com.ramitsuri.mydogs.ui.home.HomeScreen
import com.ramitsuri.mydogs.ui.home.HomeViewModel
import com.ramitsuri.mydogs.ui.theme.MyDogsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = DogDatabase.getDatabase(applicationContext)
        val repository = DogRepository(applicationContext, database.dogDao())

        setContent {
            MyDogsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: HomeViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer {
                                HomeViewModel(repository)
                            }
                        }
                    )
                    HomeScreen(
                        viewModel = viewModel,
                        repository = repository
                    )
                }
            }
        }
    }
}
