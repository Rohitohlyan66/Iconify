package com.example.iconify.model.iconSetDetails

data class IconSetDetail(
    val are_all_icons_glyph: Boolean,
    val author: Author,
    val categories: List<Category>,
    val icons_count: Int,
    val iconset_id: Int,
    val identifier: String,
    val is_premium: Boolean,
    val license: License,
    val name: String,
    val published_at: String,
    val readme: String?,
    val styles: List<Style>,
    val type: String,
    val website_url: String
)