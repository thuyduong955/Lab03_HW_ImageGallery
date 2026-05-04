package com.example.homework

data class PixabayResponse(
    val hits: List<ImageItem>,
    val totalHits: Int
)

data class ImageItem(
    val id: Int,
    val webformatURL: String,
    val tags: String
)

data class ProcessedImage(
    val imageItem: ImageItem,
    val aiTags: List<String>
)