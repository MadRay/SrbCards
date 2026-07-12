package com.srbcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.srbcards.ui.screen.CardScreen
import com.srbcards.ui.screen.MainScreen
import com.srbcards.ui.theme.SrbCardsTheme
import com.srbcards.ui.viewmodel.FlashcardViewModel

/**
 * Single-activity host for the SrbCards application.
 *
 * Navigation graph:
 *  ┌───────────────────────────────────────────────────────┐
 *  │  "main"  (MainScreen)  ────────►  "card"  (CardScreen) │
 *  │                        ◄────────                       │
 *  └───────────────────────────────────────────────────────┘
 *
 * The [FlashcardViewModel] is instantiated at the NavHost level so it survives
 * back-stack navigation and shares session state between both screens.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SrbCardsTheme {
                val navController = rememberNavController()
                // Single shared ViewModel — scoped to the Activity's ViewModelStore
                val viewModel: FlashcardViewModel = viewModel()
                val uiState   by viewModel.uiState.collectAsState()
                val categories by viewModel.categories.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        MainScreen(
                            categories   = categories,
                            uiState      = uiState,
                            onModeSelected = { mode ->
                                viewModel.setMode(mode)
                                navController.navigate("card")
                            },
                            onCategorySelected = { viewModel.setCategory(it) }
                        )
                    }

                    composable("card") {
                        CardScreen(
                            uiState  = uiState,
                            onAnswer = { viewModel.submitAnswer(it) },
                            onNext   = { viewModel.loadNextCard() },
                            onBack   = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
