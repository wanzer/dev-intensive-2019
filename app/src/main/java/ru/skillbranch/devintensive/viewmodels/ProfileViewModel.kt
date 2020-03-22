package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel : ViewModel() {

    val preferencesRepository: PreferencesRepository = PreferencesRepository
    val profileData = MutableLiveData<Profile>()
    val appTheme = MutableLiveData<Int>()

    init {
        profileData.value = preferencesRepository.getProfile()
        appTheme.value = preferencesRepository.getAppTheme()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getProfileData(): LiveData<Profile> = profileData

    fun getTheme(): LiveData<Int> = appTheme

    fun saveProfileData(profile: Profile) {
        preferencesRepository.saveProfile(profile)
        profileData.value = profile
    }

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        }else{
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        preferencesRepository.saveAppTheme(appTheme.value!!)
    }

}