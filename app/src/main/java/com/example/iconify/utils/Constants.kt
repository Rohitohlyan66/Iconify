package com.example.iconify.utils

import com.example.iconify.BuildConfig


class Constants {

    companion object {
        const val API_BASE_URL = "https://api.iconfinder.com"
        const val ICON_PER_PAGE = 20
        const val INITIAL_SEARCH_QUERY = "cricket"
        const val API_KEY = BuildConfig.ICONIFY_API_KEY
    }

}