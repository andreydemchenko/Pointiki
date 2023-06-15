package com.example.pointiki.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointiki.R
import com.example.pointiki.domain.repo.UserRepository
import com.example.pointiki.domain.usecases.GetAchievementsUseCase
import com.example.pointiki.domain.usecases.GetEventsUseCase
import com.example.pointiki.domain.usecases.SetNextAchievementProgressUseCase
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.Event
import com.example.pointiki.models.EventModel
import com.example.pointiki.models.UserModel
import com.example.pointiki.presentation.qrcode.ActionState
import com.example.pointiki.utils.Result
import com.example.pointiki.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getEventsUseCase: GetEventsUseCase,
    private val getAchievementsUseCase: GetAchievementsUseCase,
    private val setNextAchievementProgressUseCase: SetNextAchievementProgressUseCase
) : ViewModel()  {

    private val _userDataState = MutableStateFlow<UIState<UserModel>?>(null)
    val userDataState: StateFlow<UIState<UserModel>?> = _userDataState

    private val _eventsDataState = MutableStateFlow<UIState<List<EventModel>>?>(null)
    val eventsDataState: StateFlow<UIState<List<EventModel>>?> = _eventsDataState

    private val _achievementsDataState = MutableStateFlow<UIState<List<Achievement>>?>(null)
    val achievementsDataState: StateFlow<UIState<List<Achievement>>?> = _achievementsDataState

    private val _userData = MutableStateFlow<UserModel?>(null)
    val userData: StateFlow<UserModel?> = _userData

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements

    private val _allAchievements = MutableStateFlow<List<Achievement>>(emptyList())
    val allAchievements: StateFlow<List<Achievement>> = _allAchievements

    private val _nextAchievement = MutableStateFlow<Achievement?>(null)
    val nextAchievement: StateFlow<Achievement?> = _nextAchievement

    private val _selectedAchievement = MutableStateFlow<Achievement?>(null)
    val selectedAchievement: StateFlow<Achievement?> = _selectedAchievement

    private val _events = MutableStateFlow<List<EventModel>>(emptyList())
    val events: StateFlow<List<EventModel>> = _events

    private val _selectedEvent = MutableStateFlow<EventModel?>(null)
    val selectedEvent: StateFlow<EventModel?> = _selectedEvent

    private val _shouldShowAchievementAnimation = MutableStateFlow(false)
    val shouldShowAchievementAnimation: StateFlow<Boolean> = _shouldShowAchievementAnimation

    private val eventsData = listOf(
        Event(1, "Robot's programming", Date(), R.drawable.robotics_event),
        Event(2, "Another event", Date(System.currentTimeMillis() + 24*60*60*1000), R.drawable.robotics_event),
    )

    private val userId = UUID.fromString("7b7cb35e-049d-11ee-be56-0242ac120002")
    private val organizationId = "2937db1e-049d-11ee-be56-0242ac120002"

    init {
        getUserData()
        getEventsData()
        getAchievementsData()

        val userData = _userData.filterNotNull()
        val achievements = _allAchievements.filter { it.isNotEmpty() }

        viewModelScope.launch {
            combine(userData, achievements) { user, achievements ->
                val userAchievements = filterUserAchievements(user, achievements)
                _achievements.value = userAchievements
                sortAchievementsByOrder()
                setNextAchievement(user, achievements)
            }.collect()
        }
        setAchievementListener()
    }

    private fun getUserData() = viewModelScope.launch {
        _userDataState.value = UIState.Loading
        try {
            userRepository.getUser(organizationId, userId).collect { userData ->
                _userData.value = userData
                _userDataState.value = UIState.Success(userData)
            }
        } catch (e: Exception) {
            _userDataState.value = UIState.Error(e)
        }
    }

    private fun getEventsData() = viewModelScope.launch {
        _eventsDataState.value = UIState.Loading
        try {
             getEventsUseCase.invoke(organizationId).collect { eventsData ->
                _events.value = eventsData
                _eventsDataState.value = UIState.Success(eventsData)
            }
        } catch (e: Exception) {
            _eventsDataState.value = UIState.Error(e)
        }
    }

    private fun getAchievementsData() = viewModelScope.launch {
        _achievementsDataState.value = UIState.Loading
        try {
            getAchievementsUseCase.invoke(organizationId).collect { achievementsData ->
                _allAchievements.value = achievementsData
                _achievementsDataState.value = UIState.Success(achievementsData)
            }
        } catch (e: Exception) {
            _achievementsDataState.value = UIState.Error(e)
        }
    }

    private fun filterUserAchievements(user: UserModel, allAchievements: List<Achievement>): List<Achievement> {
        val userAchievementIds = user.achievements.map { it.id }
        return allAchievements.filter { userAchievementIds.contains(it.id) }
    }

    private fun sortAchievementsByOrder() {
        _achievements.value = _achievements.value.sortedBy { it.order }
        _allAchievements.value = _allAchievements.value.sortedBy { it.order }
    }

    private fun setNextAchievement(user: UserModel, allAchievements: List<Achievement>) {
        val userProgressIds = user.progresses.map { it.id }
        val nextAchievement = allAchievements.find { achievement ->
            userProgressIds.any { progressId -> progressId == achievement.id }
        }
        _nextAchievement.value = nextAchievement
    }

    fun setSelectedAchievement(achievementOrNull: Achievement?) {
        _selectedAchievement.value = achievementOrNull
    }

    private fun setAchievementListener() {
        viewModelScope.launch {
            combine(userData, nextAchievement) { user, achievement ->
                val currentProgress = user?.progresses?.firstOrNull()?.progress
                val commitment = achievement?.commitment

                achievement?.let {
                    if (currentProgress == commitment) {
                        showAchievementCommitment()
                        setNextAchievementProgress(it)
                    }
                }
            }.collect()
        }
    }

    private fun showAchievementCommitment() {
        _shouldShowAchievementAnimation.value = true
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            _shouldShowAchievementAnimation.value = false
        }
    }

    private fun setNextAchievementProgress(oldAchievement: Achievement) {
        val nextAchievement = getNextAchievement(achievements.value, allAchievements.value)
        nextAchievement?.let { achievement ->
            viewModelScope.launch {
                val result = setNextAchievementProgressUseCase.invoke(
                    organizationId,
                    userId,
                    achievement.id,
                   oldAchievement
                )
                when (result) {
                    is Result.Success -> _nextAchievement.value = achievement
                    is Result.Failure -> {
                        Log.d("HomeViewModel","fail to set next achievement: ${result.error}")
                    }
                }
            }
        }
    }

    private fun getNextAchievement(userAchievements: List<Achievement>, allAchievements: List<Achievement>): Achievement? {
        val userAchievementIds = userAchievements.map { it.id }
        val remainingAchievements = allAchievements.filter { !userAchievementIds.contains(it.id) }
        val minimumOrder = remainingAchievements.minByOrNull { it.order }?.order

        return remainingAchievements.firstOrNull { it.order > (minimumOrder ?: 0) }
    }

    fun setSelectedEvent(eventOrNull: EventModel?) {
        _selectedEvent.value = eventOrNull
    }

    fun getFirstTwoEvents(): StateFlow<List<EventModel>> {
        val firstTwoEvents = _events.value
            .sortedBy { it.date }
            .take(2)
        return MutableStateFlow(firstTwoEvents)
    }

    fun getEventsGroupedByDate(): StateFlow<Map<Date, List<EventModel>>> {
        val groupedEvents = _events.value.groupBy { truncateToDay(it.date) }
        return MutableStateFlow(groupedEvents)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date): String {
        val today = Calendar.getInstance()
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
        val eventDay = Calendar.getInstance().apply { time = date }

        return when {
            isSameDay(eventDay, today) -> "Today"
            isSameDay(eventDay, tomorrow) -> "Tomorrow"
            else -> SimpleDateFormat("MMMM d").format(date)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun formatTime(date: Date): String = SimpleDateFormat("h:mm a").format(date)

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }

    private fun truncateToDay(date: Date): Date {
        return Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

}