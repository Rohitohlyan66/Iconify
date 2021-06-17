package com.example.iconify.viewModel.iconDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iconify.repository.IconifyRepository

class IconDetailsViewModelFactory(
    val app: Application,
    val repository: IconifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IconDetailsViewModel(repository, app) as T
    }
}