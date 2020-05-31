package com.mehul.videoapplication

import android.app.Application
import android.content.Intent
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService

class MyAppllication : Application()
{
    override fun onCreate() {
        super.onCreate()
        applicationContext.startService(
            Intent(
                applicationContext,
                TransferService::class.java
            )
        )
    }

}