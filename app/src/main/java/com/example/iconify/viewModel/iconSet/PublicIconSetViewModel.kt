package com.example.iconify.viewModel.iconSet

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.iconify.IconifyApplication
import com.example.iconify.model.publicIconSets.iconSets
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PublicIconSetViewModel(
    private val repository: IconifyRepository,
    app: Application
) : AndroidViewModel(app) {

    val publicIconSets: MutableLiveData<Resource<iconSets>> = MutableLiveData()

    init {
        getAllPublicIconSets()
    }

    private fun getAllPublicIconSets() = viewModelScope.launch {
        safeAllPublicIconSetsCall()
    }

    private suspend fun safeAllPublicIconSetsCall() {
        publicIconSets.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repository.getAllPublicIconSets()
                publicIconSets.postValue(handleIconSetResponse(response))
            } else {
                publicIconSets.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> publicIconSets.postValue(Resource.Error("Network Failure"))
                else -> publicIconSets.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleIconSetResponse(response: Response<iconSets>): Resource<iconSets>? {
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
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }

}