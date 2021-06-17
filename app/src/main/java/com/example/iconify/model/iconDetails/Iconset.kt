package com.example.iconify.model.iconDetails

data class Iconset(
    val are_all_icons_glyph: Boolean,
    val author: Author,
    val categories: List<CategoryX>,
    val icons_count: Int,
    val iconset_id: Int,
    val identifier: String,
    val is_premium: Boolean,
    val license: License,
    val name: String,
    val published_at: String,
    val styles: List<Style>,
    val type: String,
    val website_url: String
)