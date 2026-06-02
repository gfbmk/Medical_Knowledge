package com.example.medicalknowledge.data.repository

import com.example.medicalknowledge.data.model.DiseaseInfo
import com.example.medicalknowledge.data.model.MedicalArticle
import com.example.medicalknowledge.data.source.MedicalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MedicalRepository(
    private val dataSource: MedicalDataSource = MedicalDataSource()
) {

    fun searchMedicalKnowledge(keyword: String): Flow<Result<List<MedicalArticle>>> = flow {
        try {
            val results = dataSource.searchMedicalKnowledge(keyword)
            emit(Result.success(results))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getLatestMedicalNews(): Flow<Result<List<MedicalArticle>>> = flow {
        try {
            val news = dataSource.getLatestMedicalNews()
            emit(Result.success(news))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getDiseaseInfo(diseaseName: String): Flow<Result<DiseaseInfo?>> = flow {
        try {
            val info = dataSource.getDiseaseInfo(diseaseName)
            emit(Result.success(info))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getCommonDiseases(): List<String> = MedicalDataSource.commonDiseases
}