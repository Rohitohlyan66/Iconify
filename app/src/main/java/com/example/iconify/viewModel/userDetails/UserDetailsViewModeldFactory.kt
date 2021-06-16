package com.example.iconify.viewModel.userDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iconify.repository.IconifyRepository

class UserDetailsViewModeldFactory(
    val app: Application,
    val repository: IconifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserDetailsViewModel(repository, app) as T
    }
}