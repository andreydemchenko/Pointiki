package com.example.pointiki.presentation.start.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.NavController
import com.example.pointiki.domain.usecases.GetOrganizationsUseCase
import com.example.pointiki.domain.usecases.SetUserLoginStatusUseCase
import com.example.pointiki.models.CountryCode
import com.example.pointiki.models.Organization
import com.example.pointiki.presentation.start.registration.RegistrationViewModel
import com.example.pointiki.utils.Result
import com.example.pointiki.utils.countryCodes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val setUserLoginStatusUseCase: SetUserLoginStatusUseCase,
    private val getOrganizationsUseCase: GetOrganizationsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var organizations = mutableStateOf<List<Organization>>(emptyList())
        private set

    init {
        fetchOrganizations()
    }

    enum class SignInState {
        ORGANIZATION, NUMBER, CODE
    }

    data class SignInUiState(
        val state: SignInState = SignInState.ORGANIZATION,
        val organization: Organization? = null,
        val phoneNumber: String = "",
        val countryCode: CountryCode = countryCodes.first(),
        val fullNumber: String = phoneNumber + countryCode.code,
        val code: String = "",
        val shouldContinue: Boolean = false,
    )

    var uiState by savedStateHandle.saveable { mutableStateOf(SignInUiState()) }
        private set

    fun updateOrganization(organization: Organization?) {
        uiState = uiState.copy(
            organization = organization,
            shouldContinue = organization != null
        )
    }

    fun organizationSelected() {
        uiState = uiState.copy(
            state = SignInState.NUMBER,
            shouldContinue = false,
        )
    }

    fun updatePhoneNumber(phoneNumber: String) {
        uiState = uiState.copy(
            phoneNumber = phoneNumber,
            shouldContinue = phoneNumber.length > 6
        )
    }

    fun phoneEntered() {
        uiState = uiState.copy(
            state = SignInState.CODE,
            fullNumber = uiState.countryCode.code + uiState.phoneNumber,
            shouldContinue = false,
        )
    }

    fun updateCountryCode(countryCode: CountryCode) {
        uiState = uiState.copy(countryCode = countryCode)
    }


    fun updateCode(code: String) {
        if (code.length > 6) return
        uiState = uiState.copy(
            code = code,
            shouldContinue = code.length == 6
        )
    }

    fun codeEntered() {
        // TODO: add code verification
    }

    fun backPressed(navController: NavController): () -> Unit {
        return {
            when (uiState.state) {
                SignInState.ORGANIZATION -> {
                    navController.popBackStack()
                }

                SignInState.NUMBER -> {
                    uiState = uiState.copy(state = SignInState.ORGANIZATION)
                }

                SignInState.CODE -> {
                    uiState = uiState.copy(state = SignInState.NUMBER)
                }
            }
        }
    }

//    fun login() {
//        setUserLoginStatusUseCase.invoke(true)
//    }


    private fun fetchOrganizations() {
        viewModelScope.launch {
            val result = getOrganizationsUseCase.invoke()
            when(result) {
                is Result.Success -> {
                    organizations.value = result.value
                    Log.d("GetOrganizations", organizations.value.toString())
                }
                is Result.Failure -> {
                    Log.d("GetOrganizations", "error fetching organizations: ${result.error}")
                }
            }
        }
    }
}