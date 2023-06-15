package com.example.pointiki.presentation.start.registration

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pointiki.models.CountryCode
import com.example.pointiki.models.Organization
import com.example.pointiki.presentation.start.components.CodeEnterContent
import com.example.pointiki.presentation.start.components.OrganizationEnterContent
import com.example.pointiki.presentation.start.components.PhoneEnterContent
import com.example.pointiki.presentation.start.registration.components.AgreementContent
import com.example.pointiki.presentation.start.registration.components.PhotoChooseContent
import com.example.pointiki.presentation.start.registration.components.SuccessfulRegistration
import com.example.pointiki.presentation.start.registration.components.NameEnterContent

@Composable
fun RegistrationScreen(navController: NavController) {

    val viewModel = hiltViewModel<RegistrationViewModel>()
    val uiState = viewModel.uiState

    if (viewModel.isRegistered.value) {
        viewModel.uploadPhoto(LocalContext.current)
    }

    RegisterScreenContent(
        viewModel,
        uiState,
        RegisterUiFlow(
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
            name = NameUiFlow(
                updateName = viewModel::updateName,
                onNameEntered = viewModel::nameEntered
            ),
            photo = PhotoUiFlow(
                onPhotoSelected = viewModel::photoSelected,
                uploadPhoto = viewModel::uploadPhoto,
                removePhoto = viewModel::removePhoto
            ),
            onBack = viewModel.backPressed(navController),
            onAgreementAccept = viewModel::agreementAccepted,
            onRegisterComplete = viewModel::completeRegistration,
            onSkip = viewModel::skipPressed
        )
    )
}

private data class RegisterUiFlow(
    val organization: OrganizationUiFlow = OrganizationUiFlow(),
    val phone: PhoneUiFlow = PhoneUiFlow(),
    val code: CodeUiFlow = CodeUiFlow(),
    val name: NameUiFlow = NameUiFlow(),
    val photo: PhotoUiFlow = PhotoUiFlow(),
    val onAgreementAccept: () -> Unit = {},
    val onRegisterComplete: () -> Unit = {},
    val onError: () -> Unit = {},
    val onBack: () -> Unit = {},
    val onSkip: () -> Unit = {},
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

private data class NameUiFlow(
    val updateName: (String) -> Unit = {},
    val onNameEntered: () -> Unit = {},
)

private data class PhotoUiFlow(
    val onPhotoSelected: () -> Unit = {},
    val uploadPhoto: (Uri?) -> Unit = {},
    val removePhoto: () -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreenContent(
    viewModel: RegistrationViewModel,
    uiState: RegistrationViewModel.RegisterUiState,
    uiFlow: RegisterUiFlow = RegisterUiFlow()
) {
    Surface {
        Column {
            if (uiState.progress > 0) {
                LinearProgressIndicator(
                    progress = uiState.progress,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                Spacer(Modifier.height(7.dp))
            }

            when (uiState.state) {
                RegistrationViewModel.RegisterState.ORGANIZATION -> {
                    OrganizationEnterContent(
                        organizations = viewModel.organizations.value,
                        onContinue = uiFlow.organization.onOrganizationSelected,
                        onBackClick = uiFlow.onBack,
                        updateOrganization = uiFlow.organization.updateOrganization,
                        shouldContinue = uiState.shouldContinue
                    )
                }
                RegistrationViewModel.RegisterState.NUMBER -> {
                    PhoneEnterContent(
                        onContinue = uiFlow.phone.onPhoneEntered,
                        onBackClick = uiFlow.onBack,
                        updatePhone = uiFlow.phone.updatePhone,
                        updateCountryCode = uiFlow.phone.updateCountryCode,
                        shouldContinue = uiState.shouldContinue
                    )
                }
                RegistrationViewModel.RegisterState.CODE -> {
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
                RegistrationViewModel.RegisterState.NAME -> {
                    NameEnterContent(
                        name = uiState.name,
                        onBackClick = uiFlow.onBack,
                        onContinue = uiFlow.name.onNameEntered,
                        onNameChange = uiFlow.name.updateName,
                        shouldContinue = uiState.shouldContinue
                    )
                }
                RegistrationViewModel.RegisterState.PHOTO -> {
                    PhotoChooseContent(
                        photoUri = uiState.photoUri,
                        onContinue = uiFlow.photo.onPhotoSelected,
                        onSkipClick = uiFlow.onSkip,
                        onBackClick = uiFlow.onBack,
                        onPhotoSelected = uiFlow.photo.uploadPhoto,
                        shouldContinue = uiState.shouldContinue,
                        onRemovePhoto = uiFlow.photo.removePhoto
                    )
                }
                RegistrationViewModel.RegisterState.AGREEMENT -> {
                    AgreementContent(
                        onContinue = uiFlow.onAgreementAccept,
                        onBackClick = uiFlow.onBack
                    )
                }
                RegistrationViewModel.RegisterState.SUCCESS -> {
                    SuccessfulRegistration(
                        onBackClick = uiFlow.onBack,
                        onContinue = uiFlow.onRegisterComplete
                    )
                }
            }
        }
    }
}
