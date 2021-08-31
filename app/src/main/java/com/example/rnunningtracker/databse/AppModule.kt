package com.example.rnunningtracker.databse

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.rnunningtracker.utils.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.rnunningtracker.utils.Constants.KEY_NAME
import com.example.rnunningtracker.utils.Constants.KEY_WEIGHT
import com.example.rnunningtracker.utils.Constants.RUNNING_DATABASE_NAME
import com.example.rnunningtracker.utils.Constants.SHARED_PREF_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, RunDatabase::class.java, RUNNING_DATABASE_NAME).build()


    @Singleton
    @Provides
    fun provideRunDao(db: RunDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) = app.getSharedPreferences(
        SHARED_PREF_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 60f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) = sharedPref.getBoolean(
        KEY_FIRST_TIME_TOGGLE, true)
}