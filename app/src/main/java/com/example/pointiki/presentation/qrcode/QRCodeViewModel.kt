package com.example.pointiki.presentation.qrcode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointiki.domain.repo.QrCodeRepository
import com.example.pointiki.domain.usecases.EnsureScannerModulesInstalledUseCase
import com.example.pointiki.domain.usecases.GetEventUseCase
import com.example.pointiki.domain.usecases.GetEventVisitModelUseCase
import com.example.pointiki.domain.usecases.SetEventVisitUseCase
import com.example.pointiki.domain.usecases.SetPointsAndVisitEventUseCase
import com.example.pointiki.models.EventModel
import com.example.pointiki.models.VisitEventModel
import com.example.pointiki.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class QRCodeViewModel @Inject constructor(
    private val repo: QrCodeRepository,
    private val ensureScannerModulesInstalledUseCase: EnsureScannerModulesInstalledUseCase,
    private val getEventUseCase: GetEventUseCase,
    private val getEventVisitModelUseCase: GetEventVisitModelUseCase,
    private val setVisitEventUseCase: SetEventVisitUseCase,
    private val setPointsAndVisitEventUseCase: SetPointsAndVisitEventUseCase
) : ViewModel() {

    private val _scanningState = MutableStateFlow(QrCodeScreenState())
    val scanningState = _scanningState.asStateFlow()

    private val userId = UUID.fromString("7b7cb35e-049d-11ee-be56-0242ac120002")
    private val organizationId = "2937db1e-049d-11ee-be56-0242ac120002"

    private val actionState = MutableStateFlow<ActionState>(ActionState.Initial)
    val actionStateListener: StateFlow<ActionState> = actionState

    init {
        viewModelScope.launch {
            actionState.collect {
                Log.d("ViewModel", "New action state: $it")
                executeNextAction() }
        }
    }

    fun startScanning() {
        viewModelScope.launch {
            try {
                val areModulesInstalled = ensureScannerModulesInstalledUseCase().first()

                if (areModulesInstalled) {
                    repo.startScanning().collect {
                        if (!it.isNullOrBlank()) {
                            _scanningState.value = scanningState.value.copy(details = it)
                            actionState.value = ActionState.ScanningCode(it)
//                            val uuid = convertValueToUUID(it)
//                            actionState.value = ActionState.UUIDCheck(uuid)
                        }
                    }
                } else {
                    // Handle the case where modules are not installed
                }
            } catch (e: Exception) {
                // Handle the exception if module installation failed
            }
        }
    }

    fun resetState() {
        actionState.value = ActionState.Initial
        //_scanningState.value = scanningState.value.copy(details = null)
    }

    private fun convertValueToUUID(value: String): UUID? {
        return try {
            UUID.fromString(value)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun checkIfTimeIsOverdue(visitEventModel: VisitEventModel, eventModel: EventModel) {
        val date = eventModel.date
        val duration = eventModel.duration
        val currentDate = Date()

        val calendar = Calendar.getInstance().apply {
            time = date
            add(Calendar.MINUTE, duration)
        }

        if (currentDate < calendar.time) {
            actionState.value = ActionState.CheckIfTimeIsOverdueStatus(visitEventModel, eventModel)
        } else {
            actionState.value = ActionState.CheckIfTimeIsOverdueStatusFailed
        }
    }

    fun mockInit() {
        val value = "7af49d99-e021-4a43-ac1f-124338072e00"
        actionState.value = ActionState.ScanningCode(value)
    }

    private suspend fun executeNextAction() {
        when (val state = actionState.value) {
            is ActionState.Initial -> {

            }
            is ActionState.ScanningCode -> {
                if (state.value != null) {
                    actionState.value = ActionState.UUIDCheck(state.value)
                }
            }
            is ActionState.UUIDCheck -> {
                val code = convertValueToUUID(state.value)
                if (code != null) {
                    actionState.value = ActionState.EventVisitLoading
                    getEventVisit(code)
                } else {
                    actionState.value = ActionState.UUIDCheckFailed
                }
            }
            is ActionState.EventVisitLoaded -> {
                actionState.value = ActionState.EventLoading(state.visitEventModel)
                getEvent(state.visitEventModel.eventId, state.visitEventModel)
            }
            is ActionState.EventLoaded -> {
                actionState.value = ActionState.CheckIfTimeIsOverdueStatusLoading
                checkIfTimeIsOverdue(state.visitEventModel, state.eventModel)
            }
            is ActionState.CheckIfTimeIsOverdueStatus -> {
                actionState.value = ActionState.CheckIfUserRegisteredStatusLoading
                checkIfUserAlreadyRegistered(state.visitEventModel, state.eventModel)
            }
            is ActionState.CheckIfUserRegisteredStatus -> {
                if (state.isRegistered) {
                    actionState.value = ActionState.CheckIfUserRegisteredStatusFailed
                } else {
                    actionState.value = ActionState.ParticipantSettingLoading
                    setParticipant(state.eventModel)
                }
            }
            is ActionState.ParticipantSet -> {
                actionState.value = ActionState.SettingPointsAndVisitLoading
                setPointsAndVisit(state.eventModel)
            }
            else -> {

            }
        }
    }

    private fun getEventVisit(uuid: UUID) {
        viewModelScope.launch {
            try {
                val visitEvent = getEventVisitModelUseCase.invoke(organizationId, uuid)
                if (visitEvent != null) {
                    actionState.value = ActionState.EventVisitLoaded(visitEvent)
                } else {
                    actionState.value = ActionState.EventVisitLoadFailed
                }
            } catch (e: Exception) {
                actionState.value = ActionState.Error(e)
            }
        }
    }

    private fun getEvent(eventId: UUID, visitEventModel: VisitEventModel) {
        viewModelScope.launch {
            try {
                val event = getEventUseCase.invoke(organizationId, eventId).first()
                actionState.value = ActionState.EventLoaded(event, visitEventModel)
            } catch (e: Exception) {
                actionState.value = ActionState.Error(e)
            }
        }
    }

    private fun checkIfUserAlreadyRegistered(visitEventModel: VisitEventModel, eventModel: EventModel) {
        viewModelScope.launch {
            //delay(2000)
            val isRegistered = visitEventModel.participants.any { it.id == userId }
            actionState.value = ActionState.CheckIfUserRegisteredStatus(visitEventModel, eventModel, isRegistered)
        }
    }

    private suspend fun setParticipant(eventModel: EventModel) {
        try {
            val result = setVisitEventUseCase.invoke(organizationId, userId, eventModel.id)
            when (result) {
                is Result.Success -> actionState.value = ActionState.ParticipantSet(eventModel)
                is Result.Failure -> {
                    Log.d("setparticipant","fail participant set with ${result.error}")
                    actionState.value = ActionState.ParticipantSettingFailed
                }
            }
        } catch (e: Exception) {
            actionState.value = ActionState.Error(e)
        }
    }


    private fun setPointsAndVisit(eventModel: EventModel) {
        viewModelScope.launch {
            try {
                val result = setPointsAndVisitEventUseCase.invoke(organizationId, userId, eventModel.id, eventModel.points)
                when (result) {
                    is Result.Success -> actionState.value = ActionState.SettingPointsAndVisitComplete(eventModel)
                    is Result.Failure -> {
                        Log.d("setpoints","fail points set with ${result.error}")
                        actionState.value = ActionState.SettingPointsAndVisitFailed
                    }
                }
            } catch (e: Exception) {
                actionState.value = ActionState.Error(e)
            }
        }
    }

}
