package com.rootslab.task_manager.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rootslab.task_manager.service.constants.TaskConstants
import com.rootslab.task_manager.service.model.ValidationModel
import com.rootslab.task_manager.service.repository.PersonRepository
import com.rootslab.task_manager.service.repository.PriorityRepository
import com.rootslab.task_manager.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val preferencesManager = PreferencesManager(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    /**
     * Faz login usando API
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = personRepository.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!

                    preferencesManager.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                    preferencesManager.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                    preferencesManager.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                    // Headers que garantem a autenticação do usuário
                    RetrofitClient.addHeaders(result.token, result.personKey)

                    _login.value = ValidationModel()
                } else {
                    _login.value = eErrorMessageFromResponse(response)
                }
            } catch (e: NoInternetException) {
                _login.value = handleException(e)
            }
        }
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        viewModelScope.launch {
            val token = preferencesManager.get(TaskConstants.SHARED.TOKEN_KEY)
            val person = preferencesManager.get(TaskConstants.SHARED.PERSON_KEY)

            // Headers que garantem a autenticação do usuário
            RetrofitClient.addHeaders(token, person)

            // Se token e person key forem diferentes de vazio, usuário está logado
            val logged = (token != "" && person != "")
            _loggedUser.value = logged

            // Se usuário não estiver logado, aplicação vai atualizar os dados
            if (!logged) {
                val response = priorityRepository.listAPI()
                if (response.isSuccessful && response.body() != null) {
                    priorityRepository.save(response.body()!!)
                }
            }
        }
    }

}
