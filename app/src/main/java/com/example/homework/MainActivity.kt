package com.example.homework

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Cấu hình OkHttpClient với User-Agent từ thiết bị thật để tránh lỗi 403
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Linux; Android 11; SM-G975F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.72 Mobile Safari/537.36")
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        // 2. Base URL của Pixabay
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pixabay.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(PixabayApiService::class.java)
        val analyzer = ImageAnalyzer(this)
        val imageAdapter = ImageAdapter()

        // 3. Ánh xạ Giao diện
        val rvGallery = findViewById<RecyclerView>(R.id.rvNews)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val errorLayout = findViewById<LinearLayout>(R.id.errorLayout)
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
        val btnRetry = findViewById<Button>(R.id.btnRetry)
        
        rvGallery.layoutManager = LinearLayoutManager(this)
        rvGallery.adapter = imageAdapter

        btnRetry.setOnClickListener { imageAdapter.retry() }

        // 4. Khởi tạo ViewModel
        val apiKey = "55708407-bfe140b3823715a004899b27b"
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ImageViewModel(apiService, apiKey, analyzer) as T
            }
        }
        val viewModel = ViewModelProvider(this, factory)[ImageViewModel::class.java]

        // 5. Lắng nghe trạng thái tải
        imageAdapter.addLoadStateListener { loadState ->
            val refreshState = loadState.source.refresh
            progressBar.isVisible = refreshState is LoadState.Loading
            errorLayout.isVisible = refreshState is LoadState.Error
            rvGallery.isVisible = refreshState is LoadState.NotLoading

            if (refreshState is LoadState.Error) {
                val error = refreshState.error
                val message = when (error) {
                    is HttpException -> {
                        if (error.code() == 403) "Lỗi 403: API Key bị từ chối. \n\nBạn hãy kiểm tra email để xác thực tài khoản Pixabay, nếu không Key sẽ không hoạt động."
                        else "Lỗi Server: ${error.code()}"
                    }
                    else -> "Lỗi kết nối: ${error.localizedMessage}"
                }
                tvErrorMessage.text = message
                Log.e("MainActivity", "Error: $message")
            }
        }

        lifecycleScope.launch {
            viewModel.imageFlow.collectLatest { pagingData ->
                imageAdapter.submitData(pagingData)
            }
        }
    }
}