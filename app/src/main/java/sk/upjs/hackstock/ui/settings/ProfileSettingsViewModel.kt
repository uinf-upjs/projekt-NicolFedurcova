package sk.upjs.hackstock.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.upjs.hackstock.repositories.AppRepository
import sk.upjs.hackstock.entities.User
import java.util.Date

class ProfileSettingsViewModel(private val repository: AppRepository) : ViewModel() {

    suspend fun getUserByEmail(email: String): User? {
        return repository.getUserByEmail(email)
    }

    suspend fun updateUserDetails(
        userId: Long,
        name: String,
        surname: String,
        dateOfBirth: Date,
        country: String,
        city: String,
        status: String,
        occupation: String,
        annualIncome: Double
    ) {
        repository.updateUserDetails(
            userId, name, surname, dateOfBirth, country, city, status, occupation, annualIncome
        )
    }
}

class ProfileSettingsViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileSettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
