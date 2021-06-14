package com.example.iconify.viewModel.iconSet

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iconify.repository.IconifyRepository

class PublicIconSetViewModelFactory(
    val app: Application,
    val repository: IconifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PublicIconSetViewModel(repository, app) as T
    }
}