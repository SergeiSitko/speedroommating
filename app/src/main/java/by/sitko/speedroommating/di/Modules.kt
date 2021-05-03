package by.sitko.speedroommating.di

import by.sitko.speedroommating.data.RepositoryImpl
import by.sitko.speedroommating.domain.LoadEventsUseCase
import by.sitko.speedroommating.data.LoadEventsUseCaseImpl
import by.sitko.speedroommating.domain.Repository
import by.sitko.speedroommating.presentation.EventViewModel
import by.sitko.speedroommating.presentation.managers.NetworkManger
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.quiz.users.api.ApiInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val viewModels = module {
    viewModel { EventViewModel(get(), get()) }
}

val singletonModule = module {
    single<Repository> { RepositoryImpl(get()) }
    single<LoadEventsUseCase> { LoadEventsUseCaseImpl(get()) }
    single { NetworkManger(get()) }
}

val apiModule = module {
    single {

        val tokenInterceptor = Interceptor { chain ->
            val request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader("secret-key", "\$2b\$10\$76APFiNwr0YXKLX6FDCGiuks/TPFnSKkJleMY2uz1AR1EqTK9IODC")
                    .build()

            chain.proceed(request)
        }

        val logInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.HEADERS
        }

        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(logInterceptor)
                .build()

        val retrofit =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.jsonbin.io/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

        retrofit.create(ApiInterface::class.java)
    }
}