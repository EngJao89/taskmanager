package com.rootslab.task_manager.service.repository

import android.content.Context
import com.rootslab.task_manager.service.model.PersonModel
import com.rootslab.task_manager.service.repository.remote.RetrofitClient
import retrofit2.Response

class PersonRepository(context: Context) : BaseRepository(context) {

    // Instância do serviço remoto que define os endpoints da API relacionados à pessoa
    private val remote = RetrofitClient.getService(PersonService::class.java)

    /**
     * Realiza o login de um usuário utilizando e-mail e senha.
     * Antes de executar, verifica se há conexão com a internet.
     */
    suspend fun login(email: String, password: String): Response<PersonModel> {
        return safeApiCall { remote.login(email, password) }
    }

    /**
     * Realiza o cadastro de um usuário utilizando e-mail, senha e nome.
     * Antes de executar, verifica se há conexão com a internet.
     */
    suspend fun create(name: String, email: String, password: String): Response<PersonModel> {
        return safeApiCall { remote.create(name, email, password) }
    }
}
