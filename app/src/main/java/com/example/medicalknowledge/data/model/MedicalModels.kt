package com.example.medicalknowledge.data.model

data class MedicalArticle(
    val title: String,
    val description: String,
    val url: String,
    val source: String,
    val publishDate: String,
    val category: String
)

data class DiseaseInfo(
    val name: String,
    val alias: String,
    val symptoms: String,
    val treatment: String,
    val medication: String,
    val prevention: String,
    val diet: String,
    val attention: String
)

data class SearchResult(
    val query: String,
    val articles: List<MedicalArticle>,
    val diseaseInfo: DiseaseInfo?
)