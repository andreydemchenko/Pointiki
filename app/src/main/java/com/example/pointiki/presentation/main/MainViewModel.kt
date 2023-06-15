package com.example.pointiki.presentation.main

import androidx.lifecycle.ViewModel
import com.example.pointiki.domain.usecases.CheckUserLoginStatusUseCase
import com.example.pointiki.domain.usecases.LogoutUseCase
import com.example.pointiki.domain.usecases.SetCurrentPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkUserLoginStatusUseCase: CheckUserLoginStatusUseCase,
    private val setCurrentPageUseCase: SetCurrentPageUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val isUserLoggedIn: StateFlow<Boolean> = checkUserLoginStatusUseCase()
    val currentPage: StateFlow<MainNavigation> = setCurrentPageUseCase.getCurrentPage()

    fun setCurrentPage(page: MainNavigation) {
        setCurrentPageUseCase(page)
    }

    fun logout() = logoutUseCase()

}