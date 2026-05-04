package com.example.homework

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

class PixabayPagingSource(
    private val apiService: PixabayApiService,
    private val apiKey: String,
    private val analyzer: ImageAnalyzer
) : PagingSource<Int, ProcessedImage>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProcessedImage> {
        return try {
            val page = params.key ?: 1
            
            // Log URL để kiểm tra thủ công nếu cần
            // URL thực tế sẽ có dạng: https://pixabay.com/api/?key=YOUR_KEY&q=nature&page=1...
            Log.d("PixabayPagingSource", "Requesting: page=$page, size=${params.loadSize}")

            val response = apiService.searchImages(apiKey, "nature", page, params.loadSize)
            
            val processedImages = response.hits.map { item ->
                // Hiển thị tag từ Pixabay trước để tránh đợi AI quá lâu
                val tagsFromPixabay = item.tags.split(",").take(3).map { it.trim() }
                ProcessedImage(item, tagsFromPixabay)
            }

            LoadResult.Page(
                data = processedImages,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (processedImages.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("PixabayPagingSource", "Lỗi nạp dữ liệu: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProcessedImage>): Int? = state.anchorPosition
}