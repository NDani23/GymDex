package com.example.gymdex.di

import android.content.Context
import android.os.Build
import com.example.gymdex.network.ApiService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
//import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://exercise.hellogym.io/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideGymDexService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}