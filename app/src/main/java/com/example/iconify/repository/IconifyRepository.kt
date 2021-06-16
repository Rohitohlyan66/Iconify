package com.example.iconify.repository

import com.example.iconify.network.RetrofitInstance

class IconifyRepository {

    suspend fun getAllPublicIconSets() =
        RetrofitInstance.apiInterface.getPublicIconSets()

    suspend fun getAllSearchedIcons(searchQuery: String) =
        RetrofitInstance.apiInterface.getSearchedIcons(searchQuery)

    suspend fun getIconSetDetails(iconSetId: Int) =
        RetrofitInstance.apiInterface.getIconSetDetails(iconSetId)

    suspend fun getAllIconsInIconSet(iconSetId: Int) =
        RetrofitInstance.apiInterface.getAllIconsInIconSet(iconSetId)

    suspend fun getUserDetails(user_id: Int) =
        RetrofitInstance.apiInterface.getUserDetails(user_id)

    suspend fun getUserIconSets(user_id: Int) =
        RetrofitInstance.apiInterface.getUserIconSets(user_id)


}