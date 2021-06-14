package com.example.iconify.viewModel.iconSetDetails

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.iconify.IconifyApplication
import com.example.iconify.model.iconSetDetails.IconSetDetail
import com.example.iconify.model.searchIcons.SearchIcons
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class IconSetDetailsViewModel(
    private val repository: IconifyRepository,
    app: Application
) : AndroidViewModel(app) {

    val iconSetDetails: MutableLiveData<Resource<IconSetDetail>> = MutableLiveData()

    fun getAllDetails(iconset_id: Int) = viewModelScope.launch {
        safeIconSetDetailsCall(iconset_id)
    }

    private suspend fun safeIconSetDetailsCall(iconset_id: Int) {
        iconSetDetails.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repository.getIconSetDetails(iconset_id)
                iconSetDetails.postValue(handleIconSetDetailsResponse(response))
            } else {
                iconSetDetails.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> iconSetDetails.postValue(Resource.Error("Network Failure"))
                else -> {
                    iconSetDetails.postValue(Resource.Error(t.message.toString()))
                    Log.d("Error", t.message.toString())
                }
            }
        }
    }

    private fun handleIconSetDetailsResponse(response: Response<IconSetDetail>): Resource<IconSetDetail>? {
        if (response.isSuccessful) {
            return Resource.Success(response.body())
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<IconifyApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }


}