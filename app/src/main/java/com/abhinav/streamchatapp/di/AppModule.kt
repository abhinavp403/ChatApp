package com.abhinav.streamchatapp.di

import android.content.Context
import com.abhinav.streamchatapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesChatClient(@ApplicationContext context: Context) =
        ChatClient.Builder(context.getString(R.string.api_key), context).build()
}