package com.example.iconify.viewModel.allIconsInIconSet

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
import com.example.iconify.model.allIconsInIconSet.AllIconsInIconsSet
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AllIconsInIconSetViewModel(
    private val repository: IconifyRepository,
    app: Application
) : AndroidViewModel(app) {


    val allIconsInIconSet: MutableLiveData<Resource<AllIconsInIconsSet>> = MutableLiveData()


    fun getAllIconsInIconSet(iconSet_id: Int) = viewModelScope.launch {
        safeAllIconsInIconSetCall(iconSet_id)
    }

    private suspend fun safeAllIconsInIconSetCall(iconSet_id: Int) {
        allIconsInIconSet.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repository.getAllIconsInIconSet(iconSet_id)
                allIconsInIconSet.postValue(handleAllIconsInIconSetResponse(response))
            } else {
                allIconsInIconSet.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> allIconsInIconSet.postValue(Resource.Error("Network Failure"))
                else -> {
                    allIconsInIconSet.postValue(Resource.Error(t.message.toString()))
                    Log.d("Error", t.message.toString())
                }
            }
        }
    }

    private fun handleAllIconsInIconSetResponse(response: Response<AllIconsInIconsSet>): Resource<AllIconsInIconsSet>? {
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