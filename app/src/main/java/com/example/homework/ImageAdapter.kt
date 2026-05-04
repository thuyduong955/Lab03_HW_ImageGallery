package com.example.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ImageAdapter : PagingDataAdapter<ProcessedImage, ImageAdapter.ImageViewHolder>(IMAGE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            // Hiển thị ảnh bằng Coil
            holder.ivGallery.load(it.imageItem.webformatURL) {
                crossfade(true)
                placeholder(android.R.color.darker_gray)
            }

            holder.tvAiTags.text = "AI Tags: ${it.aiTags.joinToString(", ")}"
        }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivGallery: ImageView = itemView.findViewById(R.id.ivGallery)
        val tvAiTags: TextView = itemView.findViewById(R.id.tvAiTags)
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<ProcessedImage>() {
            override fun areItemsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage) =
                oldItem.imageItem.id == newItem.imageItem.id

            override fun areContentsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage) =
                oldItem == newItem
        }
    }
}