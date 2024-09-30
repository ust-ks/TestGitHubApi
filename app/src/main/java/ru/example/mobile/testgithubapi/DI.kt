package ru.example.mobile.testgithubapi

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ru.example.mobile.testgithubapi.data.remote.ApiService
import ru.example.mobile.testgithubapi.data.remote.RetrofitClient
import ru.example.mobile.testgithubapi.presentation.viewModels.SearchUsersViewModel

object DI {

    private var di: Koin? = null

    fun setupKoin(appDeclaration: KoinAppDeclaration = {}) {
        if (di == null) {
            di = initKoin(appDeclaration).koin
        }
    }

    private fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
        startKoin {
            appDeclaration()
            modules(
                dataModule(),
                presentationModule()
            )
        }

    private fun dataModule() = module {
        single<ApiService> {
            RetrofitClient.retrofit.create(ApiService::class.java)
        }
    }

    private fun presentationModule() = module {
        viewModel { SearchUsersViewModel(get()) }
    }

}