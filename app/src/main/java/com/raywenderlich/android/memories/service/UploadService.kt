package com.raywenderlich.android.memories.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class UploadService : JobIntentService() {

    companion object {
        private const val JOB_ID = 30

        fun startWork(context: Context, intent: Intent) {
            enqueueWork(context, UploadService::class.java, JOB_ID, intent)
        }
    }

    private val remoteApi by lazy { App.remoteApi }

    override fun onHandleWork(intent: Intent) {
        val filePath = intent.getStringExtra("file_path")
        if (filePath != null) {
            uploadImage(filePath)
        }
    }

    private fun uploadImage(filePath: String) {
        GlobalScope.launch {
            val result = remoteApi.uploadImage(File(filePath))
            val broadcastIntent = Intent(ACTION_IMAGE_UPLOAD).apply {
                putExtra("is_uploaded", result.message == "Success")
            }
            sendBroadcast(broadcastIntent)
        }
    }
}