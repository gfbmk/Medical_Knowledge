package com.example.medicalknowledge.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.medicalknowledge.data.model.DiseaseInfo
import com.example.medicalknowledge.data.model.MedicalArticle
import com.example.medicalknowledge.ui.theme.MedicalBlue
import com.example.medicalknowledge.ui.theme.MedicalGreen
import com.example.medicalknowledge.ui.theme.MedicalOrange
import com.example.medicalknowledge.viewmodel.MedicalUiState
import com.example.medicalknowledge.viewmodel.MedicalViewModel
import com.example.medicalknowledge.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MedicalViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "医学知识宝库", 
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MedicalGreen,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { viewModel.loadLatestNews() }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "最新资讯",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState.currentScreen) {
                Screen.HOME -> HomeContent(
                    uiState = uiState,
                    viewModel = viewModel
                )
                Screen.SEARCH -> SearchResultsContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    onOpenUrl = { url -> 
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
                Screen.DISEASE_INFO -> DiseaseInfoContent(
                    diseaseInfo = uiState.diseaseInfo,
                    viewModel = viewModel
                )
                Screen.NEWS -> NewsContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    onOpenUrl = { url -> 
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
                Screen.DETAIL -> {}
            }
        }
    }
}

@Composable
fun HomeContent(
    uiState: MedicalUiState,
    viewModel: MedicalViewModel
) {
    val focusManager = LocalFocusManager.current
    val searchText = remember { mutableStateOf("") }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MedicalGreen
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🔍 搜索医学知识",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = searchText.value,
                        onValueChange = { searchText.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("输入疾病名称或关键词...") },
                        trailingIcon = {
                            IconButton(onClick = {
                                if (searchText.value.isNotBlank()) {
                                    viewModel.searchMedicalKnowledge(searchText.value)
                                    focusManager.clearFocus()
                                }
                            }) {
                                Icon(Icons.Default.Search, contentDescription = "搜索")
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchText.value.isNotBlank()) {
                                    viewModel.searchMedicalKnowledge(searchText.value)
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
        
        item {
            Text(
                text = "常见疾病",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.commonDiseases.chunked(5).forEach { row ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { disease ->
                            DiseaseChip(
                                disease = disease,
                                onClick = { viewModel.getDiseaseInfo(disease) }
                            )
                        }
                    }
                }
            }
        }
        
        item {
            QuickAccessSection(viewModel = viewModel)
        }
        
        item {
            InfoCard()
        }
    }
}

@Composable
fun DiseaseChip(
    disease: String,
    onClick: () -> Unit
) {
    val colors = listOf(MedicalGreen, MedicalBlue, MedicalOrange, Color(0xFF9C27B0))
    val color = colors[disease.hashCode().mod(colors.size)]
    
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text = disease,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun QuickAccessSection(viewModel: MedicalViewModel) {
    Column {
        Text(
            text = "快捷入口",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickAccessCard(
                title = "最新资讯",
                icon = Icons.Default.Newspaper,
                color = MedicalOrange,
                onClick = { viewModel.loadLatestNews() },
                modifier = Modifier.weight(1f)
            )
            QuickAccessCard(
                title = "用药知识",
                icon = Icons.Default.Medication,
                color = MedicalBlue,
                onClick = { viewModel.searchMedicalKnowledge("常用药物") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickAccessCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
fun InfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MedicalGreen.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "提示",
                    tint = MedicalGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "健康提示",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MedicalGreen
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "本应用提供医学知识仅供参考，不能替代专业医生的诊断和治疗。如有身体不适，请及时就医。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SearchResultsContent(
    uiState: MedicalUiState,
    viewModel: MedicalViewModel,
    onOpenUrl: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MedicalGreen)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.HOME) }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
            Text(
                text = "搜索结果: ${uiState.searchQuery}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MedicalGreen)
            }
        } else if (uiState.articles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = "无结果",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "未找到相关结果",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.articles) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { onOpenUrl(article.url) }
                    )
                }
            }
        }
    }
}

@Composable
fun NewsContent(
    uiState: MedicalUiState,
    viewModel: MedicalViewModel,
    onOpenUrl: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MedicalOrange)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.HOME) }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
            Text(
                text = "最新医学资讯",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MedicalOrange)
            }
        } else if (uiState.latestNews.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CloudOff,
                        contentDescription = "加载失败",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "加载失败，请稍后重试",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadLatestNews() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MedicalOrange
                        )
                    ) {
                        Text("重试")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.latestNews) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { onOpenUrl(article.url) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: MedicalArticle,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (article.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.source,
                    style = MaterialTheme.typography.labelSmall,
                    color = MedicalBlue
                )
                Text(
                    text = article.publishDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DiseaseInfoContent(
    diseaseInfo: DiseaseInfo?,
    viewModel: MedicalViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MedicalBlue)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.HOME) }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
            Text(
                text = diseaseInfo?.name ?: "疾病信息",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (viewModel.uiState.collectAsState().value.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MedicalBlue)
            }
        } else if (diseaseInfo != null) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    InfoSection(
                        title = "症状",
                        content = diseaseInfo.symptoms,
                        icon = Icons.Default.Healing,
                        color = MedicalOrange
                    )
                }
                
                item {
                    InfoSection(
                        title = "治疗方法",
                        content = diseaseInfo.treatment,
                        icon = Icons.Default.MedicalServices,
                        color = MedicalGreen
                    )
                }
                
                item {
                    InfoSection(
                        title = "常用药物",
                        content = diseaseInfo.medication,
                        icon = Icons.Default.Medication,
                        color = MedicalBlue
                    )
                }
                
                item {
                    InfoSection(
                        title = "预防措施",
                        content = diseaseInfo.prevention,
                        icon = Icons.Default.HealthAndSafety,
                        color = Color(0xFF9C27B0)
                    )
                }
                
                item {
                    InfoSection(
                        title = "饮食建议",
                        content = diseaseInfo.diet,
                        icon = Icons.Default.Restaurant,
                        color = MedicalOrange
                    )
                }
                
                item {
                    InfoSection(
                        title = "注意事项",
                        content = diseaseInfo.attention,
                        icon = Icons.Default.Warning,
                        color = MedicalRed
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "无法加载疾病信息",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun InfoSection(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

val MedicalRed = Color(0xFFF44336)