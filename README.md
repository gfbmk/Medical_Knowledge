# 🏥 Medical Knowledge - 医学知识宝库

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=flat-square" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=flat-square" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-orange?style=flat-square" alt="UI Framework">
  <img src="https://img.shields.io/badge/Architecture-MVVM-red?style=flat-square" alt="Architecture">
  <img src="https://img.shields.io/badge/License-MIT-lightgrey?style=flat-square" alt="License">
</div>

> 📱 一款专注于医学健康知识的Android应用，通过网络自动抓取最新医学资讯和疾病知识

## ✨ 功能特色

### 🔍 智能搜索
- 支持关键词搜索医学知识
- 自动从网络获取最新、最相关的医学信息
- 一键打开原始文章链接

### 🏥 常见疾病库
涵盖15种常见疾病，包括：
- 高血压、糖尿病、甲状腺
- 冠心病、高血脂、脂肪肝
- 胃炎、颈椎病、骨质疏松
- 痛风、哮喘、支气管炎
- 等更多疾病...

### 📰 最新资讯
- 自动获取最新的医学健康新闻
- 智能识别新闻来源
- 实时更新，保持资讯新鲜度

### 💊 用药知识
- 常见药物使用指南
- 用药注意事项
- 药物相互作用提醒

### 📋 详细信息
每种疾病包含：
- 症状表现
- 治疗方法
- 常用药物
- 预防措施
- 饮食建议
- 注意事项

## 🛠️ 技术栈

### 核心框架
- **Jetpack Compose** - 现代Android UI框架
- **Kotlin** - 编程语言
- **MVVM架构** - 清晰的分层架构

### 主要依赖
- **Compose BOM 2023.10.01** - UI组件库
- **Material Design 3** - 最新设计规范
- **JSoup 1.16.2** - HTML解析和网络爬虫
- **Kotlin Coroutines** - 异步编程
- **Navigation Compose** - 页面导航
- **Lifecycle ViewModel** - 生命周期管理

### 数据处理
- **StateFlow** - 响应式状态管理
- **协程Flow** - 异步数据流处理
- **Gson** - JSON解析

## 📂 项目结构

```
knows/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/medicalknowledge/
│   │   │   ├── data/
│   │   │   │   ├── model/
│   │   │   │   │   └── MedicalModels.kt          # 数据模型
│   │   │   │   ├── repository/
│   │   │   │   │   └── MedicalRepository.kt      # 仓库层
│   │   │   │   └── source/
│   │   │   │       └── MedicalDataSource.kt       # 数据源（网络爬虫）
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   │   └── MainScreen.kt              # 主界面
│   │   │   │   └── theme/                         # 主题配置
│   │   │   ├── viewmodel/
│   │   │   │   └── MedicalViewModel.kt           # 视图模型
│   │   │   └── MainActivity.kt                   # 主活动
│   │   ├── res/                                  # 资源文件
│   │   └── AndroidManifest.xml                  # 应用清单
│   └── build.gradle.kts                         # 应用构建配置
├── build.gradle.kts                             # 根构建配置
├── settings.gradle.kts                          # 项目设置
├── gradle.properties                            # Gradle配置
└── gradlew.bat                                 # Gradle启动脚本
```

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17 或更高版本
- Android SDK 34
- Gradle 8.0

### 编译运行

1. **克隆项目**
   ```bash
   git clone https://github.com/gfbmk/Medical_Knowledge.git
   cd Medical_Knowledge
   ```

2. **使用Android Studio打开**
   - 打开 Android Studio
   - 选择 "Open an existing project"
     - 选择项目根目录
   - 等待 Gradle sync 完成

3. **编译运行**
   - 选择运行设备（模拟器或真机）
   - 点击 Run 按钮 (▶️)
   - 等待应用安装并启动

### 命令行编译

```bash
# 调试版本
./gradlew assembleDebug

# 发布版本
./gradlew assembleRelease
```

APK输出位置：`app/build/outputs/apk/debug/app-debug.apk`

## 📱 应用截图

### 首页
- 搜索框：输入疾病名称或关键词
- 常见疾病快捷入口
- 最新资讯入口
- 用药知识入口

### 搜索结果
- 显示相关医学知识
- 展示来源信息
- 点击打开原文

### 疾病详情
- 症状、治疗、药物
- 预防、饮食建议
- 注意事项

## ⚙️ 核心功能实现

### 网络爬虫
应用使用 **JSoup** 库从百度搜索实时获取医学知识：

```kotlin
suspend fun searchMedicalKnowledge(keyword: String): List<MedicalArticle> {
    val encodedKeyword = URLEncoder.encode(keyword, "UTF-8")
    val searchUrl = "https://www.baidu.com/s?wd=${encodedKeyword}+医学知识"
    
    val doc = Jsoup.connect(searchUrl)
        .userAgent("Mozilla/5.0...")
        .timeout(15000)
        .get()
    
    // 解析搜索结果...
}
```

### MVVM架构
清晰的分层设计，确保代码可维护性和可测试性：

```
UI层（Compose界面）
    ↓ 观察 StateFlow
ViewModel层（业务逻辑）
    ↓ 调用
Repository层（数据仓库）
    ↓ 获取
DataSource层（网络爬虫）
```

## ⚠️ 重要提示

### 免责声明
- 本应用提供的医学知识仅供参考
- 不能替代专业医生的诊断和治疗
- 如有身体不适，请及时就医
- 药物使用请遵医嘱

### 数据来源
- 搜索结果来自百度搜索
- 资讯来源于各大健康网站
- 数据准确性由来源网站负责

## 🔄 更新日志

### v1.0.0 (2026-06-02)
- ✨ 实现智能搜索功能
- ✨ 添加15种常见疾病信息
- ✨ 实现最新医学资讯获取
- ✨ 添加用药知识查询
- ✨ 完整的MVVM架构
- ✨ Material Design 3 UI

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 📄 开源协议

本项目采用 MIT 开源协议

## 🙏 致谢

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 现代Android UI工具包
- [JSoup](https://jsoup.org/) - HTML解析库
- [Material Design](https://material.io/design) - 设计系统

## 📧 联系方式

- GitHub: https://github.com/gfbmk
- 项目地址: https://github.com/gfbmk/Medical_Knowledge

---

<div align="center">
  <p>⭐ 如果这个项目对您有帮助，请给我们一个星标！</p>
  <p>Made with ❤️ for better health knowledge</p>
</div>