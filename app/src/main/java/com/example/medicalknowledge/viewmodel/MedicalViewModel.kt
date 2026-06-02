package com.example.medicalknowledge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalknowledge.data.model.DiseaseInfo
import com.example.medicalknowledge.data.model.MedicalArticle
import com.example.medicalknowledge.data.repository.MedicalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MedicalUiState(
    val isLoading: Boolean = false,
    val articles: List<MedicalArticle> = emptyList(),
    val latestNews: List<MedicalArticle> = emptyList(),
    val diseaseInfo: DiseaseInfo? = null,
    val commonDiseases: List<String> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val selectedArticle: MedicalArticle? = null,
    val currentScreen: Screen = Screen.HOME
)

enum class Screen {
    HOME,
    SEARCH,
    DETAIL,
    DISEASE_INFO,
    NEWS
}

class MedicalViewModel : ViewModel() {
    
    private val repository = MedicalRepository()
    
    private val _uiState = MutableStateFlow(MedicalUiState())
    val uiState: StateFlow<MedicalUiState> = _uiState.asStateFlow()
    
    init {
        loadCommonDiseases()
        loadLatestNews()
    }
    
    private fun loadCommonDiseases() {
        _uiState.value = _uiState.value.copy(
            commonDiseases = repository.getCommonDiseases()
        )
    }
    
    fun loadLatestNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                currentScreen = Screen.NEWS
            )
            
            repository.getLatestMedicalNews().collect { result ->
                result.onSuccess { news ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        latestNews = news
                    )
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "加载失败"
                    )
                }
            }
        }
    }
    
    fun searchMedicalKnowledge(query: String) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                searchQuery = query,
                errorMessage = null,
                currentScreen = Screen.SEARCH
            )
            
            repository.searchMedicalKnowledge(query).collect { result ->
                result.onSuccess { articles ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        articles = articles
                    )
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "搜索失败"
                    )
                }
            }
        }
    }
    
    fun getDiseaseInfo(diseaseName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                currentScreen = Screen.DISEASE_INFO
            )
            
            repository.getDiseaseInfo(diseaseName).collect { result ->
                result.onSuccess { info ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        diseaseInfo = info
                    )
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "加载失败"
                    )
                }
            }
        }
    }
    
    fun selectArticle(article: MedicalArticle) {
        _uiState.value = _uiState.value.copy(
            selectedArticle = article,
            currentScreen = Screen.DETAIL
        )
    }
    
    fun navigateTo(screen: Screen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}