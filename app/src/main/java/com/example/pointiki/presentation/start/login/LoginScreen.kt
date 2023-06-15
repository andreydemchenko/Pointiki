package com.example.pointiki.presentation.start.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pointiki.models.CountryCode
import com.example.pointiki.models.Organization
import com.example.pointiki.presentation.start.components.CodeEnterContent
import com.example.pointiki.presentation.start.components.OrganizationEnterContent
import com.example.pointiki.presentation.start.components.PhoneEnterContent

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState

    val uiFlow = SignInUiFlow(
        organization = OrganizationUiFlow(
            updateOrganization = viewModel::updateOrganization,
            onOrganizationSelected = viewModel::organizationSelected
        ),
        phone = PhoneUiFlow(
            updatePhone = viewModel::updatePhoneNumber,
            updateCountryCode = viewModel::updateCountryCode,
            onPhoneEntered = viewModel::phoneEntered
        ),
        code = CodeUiFlow(
            updateCode = viewModel::updateCode,
            onCodeEntered = viewModel::codeEntered
        ),
        onBack = viewModel.backPressed(navController),
    )

    SignInScreenContent(viewModel, uiState, uiFlow)
}

private data class SignInUiFlow(
    val onBack: () -> Unit = {},
    val organization: OrganizationUiFlow = OrganizationUiFlow(),
    val phone: PhoneUiFlow = PhoneUiFlow(),
    val code: CodeUiFlow = CodeUiFlow()
)

private data class OrganizationUiFlow(
    val updateOrganization: (Organization) -> Unit = {},
    val onOrganizationSelected: () -> Unit = {},
)

private data class PhoneUiFlow(
    val updatePhone: (String) -> Unit = {},
    val updateCountryCode: (CountryCode) -> Unit = {},
    val onPhoneEntered: () -> Unit = {},
)

private data class CodeUiFlow(
    val updateCode: (String) -> Unit = {},
    val onCodeEntered: () -> Unit = {},
    val onResendCode: () -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScreenContent(
    viewModel: LoginViewModel,
    uiState: LoginViewModel.SignInUiState,
    uiFlow: SignInUiFlow = SignInUiFlow()
) {
    Surface {
        Column {
            when (uiState.state) {
                LoginViewModel.SignInState.ORGANIZATION -> {
                    OrganizationEnterContent(
                        organizations = viewModel.organizations.value,
                        onContinue = uiFlow.organization.onOrganizationSelected,
                        onBackClick = uiFlow.onBack,
                        updateOrganization = uiFlow.organization.updateOrganization,
                        shouldContinue = uiState.shouldContinue
                    )
                }
                LoginViewModel.SignInState.NUMBER -> {
                    PhoneEnterContent(
                        onContinue = uiFlow.phone.onPhoneEntered,
                        onBackClick = uiFlow.onBack,
                        updatePhone = uiFlow.phone.updatePhone,
                        updateCountryCode = uiFlow.phone.updateCountryCode,
                        shouldContinue = uiState.shouldContinue
                    )
                }
                LoginViewModel.SignInState.CODE -> {
                    CodeEnterContent(
                        number = uiState.fullNumber,
                        code = uiState.code,
                        onContinue = uiFlow.code.onCodeEntered,
                        onBackClick = uiFlow.onBack,
                        updateCode = uiFlow.code.updateCode,
                        onResend = uiFlow.code.onResendCode,
                        shouldContinue = uiState.shouldContinue
                    )
                }
            }
        }
    }
}