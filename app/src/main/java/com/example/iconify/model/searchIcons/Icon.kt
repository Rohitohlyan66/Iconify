package com.example.iconify.model.searchIcons

data class Icon(
    val categories: List<Category>,
    val containers: List<Container>,
    val icon_id: Int,
    val is_icon_glyph: Boolean,
    val is_premium: Boolean,
    val prices: List<Price>?,
    val published_at: String,
    val raster_sizes: List<RasterSize>,
    val styles: List<Style>,
    val tags: List<String>,
    val type: String,
    val vector_sizes: List<VectorSize>
)