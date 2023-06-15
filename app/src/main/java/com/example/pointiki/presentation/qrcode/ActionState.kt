package com.example.pointiki.presentation.qrcode

import com.example.pointiki.models.EventModel
import com.example.pointiki.models.VisitEventModel

sealed class ActionState {
    object Initial : ActionState()
    data class ScanningCode(val value: String? = null) : ActionState()
    data class UUIDCheck(val value: String) : ActionState()
    object UUIDCheckFailed : ActionState()
    object EventVisitLoading : ActionState()
    object EventVisitLoadFailed : ActionState()
    data class EventVisitLoaded(val visitEventModel: VisitEventModel) : ActionState()
    data class EventLoading(val visitEventModel: VisitEventModel) : ActionState()
    object EventLoadFailed : ActionState()
    data class EventLoaded(val eventModel: EventModel, val visitEventModel: VisitEventModel) : ActionState()
    object CheckIfTimeIsOverdueStatusLoading : ActionState()
    object CheckIfTimeIsOverdueStatusFailed : ActionState()
    data class CheckIfTimeIsOverdueStatus(val visitEventModel: VisitEventModel, val eventModel: EventModel) : ActionState()
    object CheckIfUserRegisteredStatusLoading : ActionState()
    object CheckIfUserRegisteredStatusFailed : ActionState()
    data class CheckIfUserRegisteredStatus(val visitEventModel: VisitEventModel, val eventModel: EventModel, val isRegistered: Boolean) : ActionState()
    object ParticipantSettingLoading : ActionState()
    object ParticipantSettingFailed : ActionState()
    data class ParticipantSet(val eventModel: EventModel) : ActionState()
    object SettingPointsAndVisitLoading : ActionState()
    object SettingPointsAndVisitFailed : ActionState()
    data class SettingPointsAndVisitComplete(val eventModel: EventModel) : ActionState()
    data class Error(val error: Throwable) : ActionState()
}


