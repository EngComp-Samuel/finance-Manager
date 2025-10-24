package br.com.engcomp.financemanager.onboard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// OnboardingScreen.kt
data class OnboardingPage(
    val image: Int,
    val title: String,
    val description: String
)

class OnboardingViewModel : ViewModel() {
    private val _currentPage = mutableStateOf(0)
    val currentPage: MutableState<Int> = _currentPage

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }
}