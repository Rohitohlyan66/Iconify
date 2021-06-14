package com.example.iconify.model.searchIcons

data class VectorSize(
    val formats: List<FormatX>,
    val size: Int,
    val size_height: Int,
    val size_width: Int,
    val target_sizes: List<List<Int>>
)