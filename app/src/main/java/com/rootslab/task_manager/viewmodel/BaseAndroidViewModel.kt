package com.rootslab.task_manager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import retrofit2.Response

open class BaseAndroidViewModel(private val app: Application) : AndroidViewModel(app) {

    /**
     * Extrai a mensagem de erro da resposta da API e encapsula em um ValidationModel.
     */
    fun <T> eErrorMessageFromResponse(response: Response<T>): ValidationModel {
        return ValidationModel(
            Gson().fromJson(
                response.errorBody()?.string().toString(),
                String::class.java
            )
        )
    }

    /**
     * Trata exceções lançadas durante chamadas de rede.
     * Caso a exceção seja do tipo NoInternetException, retorna uma mensagem específica.
     * Caso contrário, retorna uma mensagem genérica de erro inesperado.
     */
    fun handleException(e: Exception): ValidationModel {
        return if (e is NoInternetException) {
            ValidationModel(e.errorMessage)
        } else {
            ValidationModel(app.applicationContext.getString(R.string.error_unexpected))
        }
    }

}