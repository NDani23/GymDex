package com.example.gymdex.di

import android.content.Context
import android.os.Build
import com.example.gymdex.network.ApiService
import com.example.gymdex.persistence.ExerciseDao
import com.example.gymdex.persistence.MuscleGroupDao
import com.example.gymdex.ui.list.ExerciseListRepository
import com.example.gymdex.ui.main.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        apiService: ApiService,
        muscleGroupDao: MuscleGroupDao
    ): HomeRepository {
        return HomeRepository(apiService, muscleGroupDao)
    }

    @Provides
    @ViewModelScoped
    fun provideExerciseListRepository(
        apiService: ApiService,
        exerciseDao: ExerciseDao
    ): ExerciseListRepository {
        return ExerciseListRepository(apiService, exerciseDao)
    }
}