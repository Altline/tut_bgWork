package com.raywenderlich.android.memories.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.utils.FileUtils
import java.io.File

class DownloadService : JobIntentService() {

    companion object {
        private const val JOB_ID = 10

        fun startWork(context: Context, intent: Intent) {
            enqueueWork(context, DownloadService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val imagePath = intent.getStringExtra("image_path")

        if (imagePath != null) {
            downloadImage(imagePath)
        } else {
            Log.d("DownloadManager", "Missing image path. Stopping service")
            stopSelf()
        }
    }

    private fun downloadImage(path: String) {
        val file = File(applicationContext.externalMediaDirs.first(), path)
        FileUtils.downloadImage(path, file)
    }
}