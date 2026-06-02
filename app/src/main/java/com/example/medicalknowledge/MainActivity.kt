package com.example.medicalknowledge

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.medicalknowledge.ui.theme.MedicalKnowledgeTheme
import com.example.medicalknowledge.ui.screens.MainScreen
import com.example.medicalknowledge.viewmodel.MedicalViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicalKnowledgeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = MedicalViewModel()
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}