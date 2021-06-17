package com.example.iconify.model.iconDetails

data class IconDetails(
    val categories: List<Category>,
    val containers: List<Any>,
    val icon_id: Int,
    val iconset: Iconset,
    val is_icon_glyph: Boolean,
    val is_premium: Boolean,
    val published_at: String,
    val raster_sizes: List<RasterSize>,
    val styles: List<StyleX>,
    val tags: List<String>,
    val type: String,
    val vector_sizes: List<VectorSize>
)