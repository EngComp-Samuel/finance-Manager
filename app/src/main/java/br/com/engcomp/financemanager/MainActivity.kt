package br.com.engcomp.financemanager

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import br.com.engcomp.financemanager.onboard.DataStoreManager
import br.com.engcomp.financemanager.onboard.OnboardingScreen
import br.com.engcomp.financemanager.screen.MainScreen
import br.com.engcomp.financemanager.screen.SplashScreen
import br.com.engcomp.financemanager.ui.theme.FinanceManagerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            val dataStoreManager = remember { DataStoreManager(context) }

            // Estado do onboarding
            val onboardingCompleted by dataStoreManager.getOnboardingState().collectAsState(initial = false)

            // Estados de UI
            //var showSplash by remember { mutableStateOf(!onboardingCompleted) }

            FinanceManagerTheme {

                // Estados para controlar o fluxo
                var showSplash by rememberSaveable { mutableStateOf(true) }
                var showOnboarding by rememberSaveable { mutableStateOf(false) }

                /*if (showOnboarding) {
                    *//*SplashScreen(navController = navController, onFinish = {
                        showOnboarding = false
                    })*//*
                    OnboardingScreen(onFinish = {
                        showOnboarding = false
                    })
                }*/

                when{

                    showSplash && !onboardingCompleted -> {
                        SplashScreen(
                            navController = navController,
                            onFinish = {
                                showSplash = false
                                showOnboarding = true
                            }
                        )
                    }

                    showOnboarding -> {
                        OnboardingScreen(
                            onFinish = {
                                // Salva no DataStore e muda estado
                                scope.launch {
                                    dataStoreManager.saveOnboardingState(true)
                                    showOnboarding = false
                                }
                            }
                        )
                    }

                    else -> {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MainScreen(navController = navController,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}