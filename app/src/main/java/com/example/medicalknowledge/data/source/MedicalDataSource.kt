package com.example.medicalknowledge.data.source

import com.example.medicalknowledge.data.model.DiseaseInfo
import com.example.medicalknowledge.data.model.MedicalArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicalDataSource {

    suspend fun searchMedicalKnowledge(keyword: String): List<MedicalArticle> = withContext(Dispatchers.IO) {
        val articles = mutableListOf<MedicalArticle>()
        try {
            val encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8")
            val searchUrl = "https://www.baidu.com/s?wd=${encodedKeyword}+医学知识"
            
            val doc: Document = Jsoup.connect(searchUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(15000)
                .get()

            val results = doc.select("div.result")
            for (result in results.take(10)) {
                val titleElement = result.select("h3 a").first()
                val title = titleElement?.text() ?: continue
                
                val descElement = result.select("div.content-right__3oFE, div.c-abstract").first()
                val description = descElement?.text() ?: ""
                
                val url = titleElement?.attr("href") ?: continue
                
                val dateStr = getCurrentDate()
                
                val source = extractSource(url)

                if (title.isNotBlank() && url.isNotBlank()) {
                    articles.add(
                        MedicalArticle(
                            title = title,
                            description = description.take(200),
                            url = url,
                            source = source,
                            publishDate = dateStr,
                            category = keyword
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        articles
    }

    suspend fun getLatestMedicalNews(): List<MedicalArticle> = withContext(Dispatchers.IO) {
        val articles = mutableListOf<MedicalArticle>()
        try {
            val doc: Document = Jsoup.connect("https://www.baidu.com/s?wd=医学健康新闻")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(15000)
                .get()

            val results = doc.select("div.result")
            val dateStr = getCurrentDate()
            
            for (result in results.take(15)) {
                val titleElement = result.select("h3 a").first() ?: continue
                val title = titleElement.text()
                if (title.isBlank()) continue
                
                val descElement = result.select("div.content-right__3oFE, div.c-abstract").first()
                val description = descElement?.text() ?: ""
                
                val url = titleElement.attr("href")
                if (url.isBlank()) continue
                
                val source = extractSource(url)

                articles.add(
                    MedicalArticle(
                        title = title,
                        description = description.take(200),
                        url = url,
                        source = source,
                        publishDate = dateStr,
                        category = "最新资讯"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        articles
    }

    suspend fun getDiseaseInfo(diseaseName: String): DiseaseInfo? = withContext(Dispatchers.IO) {
        try {
            val encodedName = java.net.URLEncoder.encode("${diseaseName}疾病介绍", "UTF-8")
            val searchUrl = "https://www.baidu.com/s?wd=${encodedName}"
            
            val doc: Document = Jsoup.connect(searchUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(15000)
                .get()

            var symptoms = ""
            var treatment = ""
            var medication = ""
            var prevention = ""
            var attention = ""

            val results = doc.select("div.result")
            for (result in results.take(5)) {
                val text = result.text().lowercase()
                if (text.contains("症状") && symptoms.isEmpty()) {
                    symptoms = result.text().take(300)
                }
                if (text.contains("治疗") && treatment.isEmpty()) {
                    treatment = result.text().take(300)
                }
                if (text.contains("药物") || text.contains("用药") && medication.isEmpty()) {
                    medication = result.text().take(300)
                }
                if (text.contains("预防") && prevention.isEmpty()) {
                    prevention = result.text().take(300)
                }
            }

            DiseaseInfo(
                name = diseaseName,
                alias = "",
                symptoms = symptoms.ifEmpty { "暂无详细信息" },
                treatment = treatment.ifEmpty { "请咨询专业医生" },
                medication = medication.ifEmpty { "请遵医嘱用药" },
                prevention = prevention.ifEmpty { "保持健康生活方式" },
                diet = "均衡饮食，荤素搭配",
                attention = "如有不适请及时就医"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun extractSource(url: String): String {
        return try {
            if (url.contains("baike.baidu.com")) "百度百科"
            else if (url.contains("zhihu.com")) "知乎"
            else if (url.contains("jib.xywy.com")) "寻医问药"
            else if (url.contains("zzk.xywy.com")) "寻医问药"
            else if (url.contains("120ask.com")) "有问必答"
            else if (url.contains("39.net")) "39健康网"
            else if (url.contains("mayo")) "Mayo Clinic"
            else if (url.contains("nih.gov")) "NIH"
            else if (url.contains("who.int")) "WHO"
            else url.substringAfter("://").substringBefore("/").substringBefore("?")
        } catch (e: Exception) {
            "未知来源"
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf.format(Date())
    }

    companion object {
        val commonDiseases = listOf(
            "高血压",
            "糖尿病", 
            "甲状腺",
            "冠心病",
            "高血脂",
            "脂肪肝",
            "胃炎",
            "颈椎病",
            "腰椎间盘突出",
            "骨质疏松",
            "痛风",
            "哮喘",
            "支气管炎",
            "肺炎",
            "肺结核"
        )
    }
}