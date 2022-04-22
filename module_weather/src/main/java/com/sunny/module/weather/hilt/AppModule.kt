package com.sunny.module.weather.hilt

import android.app.Application
import com.sunny.module.weather.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideApiService(app: Application): ApiService {
        return Retrofit.Builder()

            .build()
            .create(ApiService::class.java)
    }
}