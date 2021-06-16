package com.example.iconify.viewModel.userIconSets

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.iconify.IconifyApplication
import com.example.iconify.model.publicIconSets.iconSets
import com.example.iconify.model.userIconsSets.UserIconSets
import com.example.iconify.repository.IconifyRepository
import com.example.iconify.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserIconSetsViewModel(
    private val repository: IconifyRepository,
    app: Application
) : AndroidViewModel(app) {

    val userIconSets: MutableLiveData<Resource<UserIconSets>> = MutableLiveData()


     fun getUserIconSets(user_id: Int) = viewModelScope.launch {
        safeUserIconSetsCall(user_id)
    }

    private suspend fun safeUserIconSetsCall(user_id: Int) {
        userIconSets.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repository.getUserIconSets(user_id)
                userIconSets.postValue(handleUserIconSetsResponse(response))
            } else {
                userIconSets.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userIconSets.postValue(Resource.Error("Network Failure"))
                else -> userIconSets.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleUserIconSetsResponse(response: Response<UserIconSets>): Resource<UserIconSets>? {
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