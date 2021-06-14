package com.example.iconify.viewModel.icons

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iconify.repository.IconifyRepository

class SearchIconsViewModelFactory(
    val app: Application,
    val repository: IconifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchIconsViewModel(repository, app) as T
    }
}