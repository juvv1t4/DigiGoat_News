package com.example.digigoat_news

import kotlinx.serialization.Serializable

@Serializable
object NewsPageScreen

@Serializable
data class ArticlePageScreen(
    val url : String
)