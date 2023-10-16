package com.abhinav.streamchatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.offline.ChatDomain
import javax.inject.Inject

@HiltAndroidApp
class ChatApplication : Application() {

    // Need to inject client instance since it will be needed throughout project
    @Inject
    lateinit var client: ChatClient

    override fun onCreate() {
        super.onCreate()
        ChatDomain.Builder(client, applicationContext).build()
    }
}