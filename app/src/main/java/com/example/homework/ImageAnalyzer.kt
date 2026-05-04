package com.example.homework
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import coil.Coil
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class ImageAnalyzer(private val context: Context) {
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    suspend fun analyzeImage(imageUrl: String): List<String> = coroutineScope {
        try {
            val request = ImageRequest.Builder(context).data(imageUrl).allowHardware(false).build()
            val result = Coil.imageLoader(context).execute(request)
            val bitmap = (result.drawable as? BitmapDrawable)?.bitmap ?: return@coroutineScope emptyList()

            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await()
            labels.take(3).map { it.text }
        } catch (e: Exception) { emptyList() }
    }
}