package com.example.iconify.network

import com.example.iconify.model.allIconsInIconSet.AllIconsInIconsSet
import com.example.iconify.model.iconSetDetails.IconSetDetail
import com.example.iconify.model.publicIconSets.iconSets
import com.example.iconify.model.searchIcons.SearchIcons
import com.example.iconify.model.userDetails.UserDetails
import com.example.iconify.model.userIconsSets.UserIconSets
import com.example.iconify.utils.Constants.Companion.ICON_PER_PAGE
import com.example.iconify.utils.Constants.Companion.INITIAL_SEARCH_QUERY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Authorization: Bearer iUW8lH599vERuodwSUAPIrJrXpUMN9Jd8c5pmyIg2Jekj2wzxvwALbAOvA0NgjR6")
    @GET("v4/iconsets")
    suspend fun getPublicIconSets(
        @Query("count") iconSetPerPage: Int = ICON_PER_PAGE
    ): Response<iconSets>


    @Headers("Authorization: Bearer iUW8lH599vERuodwSUAPIrJrXpUMN9Jd8c5pmyIg2Jekj2wzxvwALbAOvA0NgjR6")
    @GET("v4/icons/search")
    suspend fun getSearchedIcons(
        @Query("query") searchQuery: String = INITIAL_SEARCH_QUERY,
        @Query("count") iconsPerPage: Int = ICON_PER_PAGE
    ): Response<SearchIcons>


    @Headers("Authorization: Bearer iUW8lH599vERuodwSUAPIrJrXpUMN9Jd8c5pmyIg2Jekj2wzxvwALbAOvA0NgjR6")
    @GET("v4/iconsets")
    suspend fun getIconSetDetails(
        @Query("iconset_id") iconSetID: Int
    ): Response<IconSetDetail>

    @Headers("Authorization: Bearer iUW8lH599vERuodwSUAPIrJrXpUMN9Jd8c5pmyIg2Jekj2wzxvwALbAOvA0NgjR6")
    @GET("v4/iconsets/{iconset_id}/icons")
    suspend fun getAllIconsInIconSet(
        @Path("iconset_id") iconSetID: Int,
        @Query("count") iconsPerPage: Int = ICON_PER_PAGE
    ): Response<AllIconsInIconsSet>

    @Headers("Authorization: Bearer iUW8lH599vERuodwSUAPIrJrXpUMN9Jd8c5pmyIg2Jekj2wzxvwALbAOvA0NgjR6")
    @GET("v4/users/{user_id}")
    suspend fun getUserDetails(
        @Path("user_id") user_id: Int
    ): Response<UserDetails>

    @Headers("Authorization: Bearer iUW8lH599vERuodwSUAPIrJrXpUMN9Jd8c5pmyIg2Jekj2wzxvwALbAOvA0NgjR6")
    @GET("v4/users/{user_id}/iconsets")
    suspend fun getUserIconSets(
        @Path("user_id") user_id: Int,
        @Query("count") iconsPerPage: Int = ICON_PER_PAGE
    ): Response<UserIconSets>


}