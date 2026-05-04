package com.example.homework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class ImageViewModel(
    private val apiService: PixabayApiService,
    private val apiKey: String,
    private val analyzer: ImageAnalyzer
) : ViewModel() {

    val imageFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { PixabayPagingSource(apiService, apiKey, analyzer) }
    ).flow.cachedIn(viewModelScope)
}