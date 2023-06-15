package com.example.pointiki.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointiki.R
import com.example.pointiki.domain.repo.UserRepository
import com.example.pointiki.domain.usecases.GetAchievementsUseCase
import com.example.pointiki.domain.usecases.GetAllUsersUseCase
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.EventModel
import com.example.pointiki.models.PointEntry
import com.example.pointiki.models.User
import com.example.pointiki.models.UserModel
import com.example.pointiki.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getAchievementsUseCase: GetAchievementsUseCase
) : ViewModel() {
    private val _sortTime = MutableStateFlow(LeaderboardSortTime.WEEK)
    val sortTime: StateFlow<LeaderboardSortTime> = _sortTime

    private val _users = MutableStateFlow<List<UserModel>>(emptyList())
    val users: StateFlow<List<UserModel>> = _users

    private val _usersDataState = MutableStateFlow<UIState<List<UserModel>>?>(null)
    val usersDataState: StateFlow<UIState<List<UserModel>>?> = _usersDataState

    private val _sortedUsers = MutableStateFlow<List<UserModel>>(emptyList())
    val sortedUsers: StateFlow<List<UserModel>> = _sortedUsers

    private val _selectedUser = MutableStateFlow<UserModel?>(null)
    val selectedUser: StateFlow<UserModel?> = _selectedUser

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements

    private val _allAchievements = MutableStateFlow<List<Achievement>>(emptyList())
    val allAchievements: StateFlow<List<Achievement>> = _allAchievements

    private val _achievementsDataState = MutableStateFlow<UIState<List<Achievement>>?>(null)
    val achievementsDataState: StateFlow<UIState<List<Achievement>>?> = _achievementsDataState

    private val organizationId = "2937db1e-049d-11ee-be56-0242ac120002"

    init {
        sortTime
            .onEach { updateLeaderboard() }
            .launchIn(viewModelScope)

        getUsersData()
        getAchievementsData()

        val userData = _selectedUser.filterNotNull()
        val achievements = _allAchievements.filter { it.isNotEmpty() }

        viewModelScope.launch {
            combine(userData, achievements) { user, achievements ->
                val userAchievements = filterUserAchievements(user, achievements)
                _achievements.value = userAchievements
                sortAchievementsByOrder()
            }.collect()
        }
    }

    private fun getUsersData() = viewModelScope.launch {
        _usersDataState.value = UIState.Loading
        try {
            getAllUsersUseCase.invoke(organizationId).collect { usersData ->
                _users.value = usersData
                updateLeaderboard()
                _usersDataState.value = UIState.Success(usersData)
            }
        } catch (e: Exception) {
            _usersDataState.value = UIState.Error(e)
        }
    }

    private fun calculatePointsForTimePeriod(timePeriod: LeaderboardSortTime): List<UserModel> {
        val currentTime = Date().time

        return _users.value.map { user ->
            val filteredPoints = user.pointsHistory.filter { point ->
                when (timePeriod) {
                    LeaderboardSortTime.WEEK -> isWithinWeek(point.date.time, currentTime)
                    LeaderboardSortTime.MONTH -> isWithinMonth(point.date.time, currentTime)
                    LeaderboardSortTime.ALL_TIME -> true
                }
            }
            val points = filteredPoints.fold(0L) { acc, point -> acc.safeAdd(point.points) }.toInt()
            user.copy(points = points)
        }.sortedByDescending { user -> user.points }
    }

    private fun Long.safeAdd(other: Int) = if (Long.MAX_VALUE - this >= other) this + other else Long.MAX_VALUE

    private fun isWithinWeek(timestamp: Long, currentTime: Long) = currentTime - timestamp <= TimeUnit.DAYS.toMillis(7)

    private fun isWithinMonth(timestamp: Long, currentTime: Long) = currentTime - timestamp <= TimeUnit.DAYS.toMillis(30)

    private fun updateLeaderboard() {
        viewModelScope.launch(Dispatchers.Default) {
            val sortedUsers = calculatePointsForTimePeriod(_sortTime.value)
            withContext(Dispatchers.Main) {
                _sortedUsers.value = sortedUsers
            }
        }
    }

    fun setSortTime(newSortTime: LeaderboardSortTime) {
        _sortTime.value = newSortTime
    }

    fun setSelectedUser(userOrNull: UserModel?) {
        _selectedUser.value = userOrNull
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

    private fun generateMockUsers(): List<User> {
        val icons = listOf(R.drawable.user1_icon, R.drawable.user2_icon, R.drawable.user3_icon)
        val names = listOf("Peter", "James", "Olya", "Jimmy", "Ivan", "Katya", "Roman", "Kirill", "Viola")

        return names.indices.map { i ->
            User(UUID.randomUUID(), icons[i % icons.size], names[i], 0, generateMockPointsHistory())
        }
    }

    private fun generateMockPointsHistory(): List<PointEntry> {
        val calendar = Calendar.getInstance()
        val pointsHistory = mutableListOf<PointEntry>()

        val numEntries = (1..10).random()
        repeat(numEntries) {
            val points = (1..100).random()
            calendar.add(Calendar.DAY_OF_MONTH, -1) // Move one day back
            val date = calendar.time
            pointsHistory.add(PointEntry(points, date))
        }

        return pointsHistory
    }

}
