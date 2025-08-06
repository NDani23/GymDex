package com.example.gymdex.di

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.gymdex.persistence.AppDatabase
import com.example.gymdex.persistence.EquipmentDao
import com.example.gymdex.persistence.ExerciseDao
import com.example.gymdex.persistence.MuscleGroupDao

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                "GymDexCompose.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMuscleGroupDao(appDatabase: AppDatabase): MuscleGroupDao {
        return appDatabase.muscleGroupDao()
    }

    @Provides
    @Singleton
    fun provideEquipmentDao(appDatabase: AppDatabase): EquipmentDao {
        return appDatabase.equipmentDao()
    }

    @Provides
    @Singleton
    fun provideExerciseDao(appDatabase: AppDatabase): ExerciseDao {
        return appDatabase.exerciseDao()
    }
}