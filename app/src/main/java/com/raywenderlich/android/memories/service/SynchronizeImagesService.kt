package com.raywenderlich.android.memories.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.utils.FileUtils

class SynchronizeImagesService : JobIntentService() {

    companion object {
        private const val JOB_ID = 20

        fun startWork(context: Context, intent: Intent) {
            enqueueWork(context, SynchronizeImagesService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        FileUtils.clearLocalStorage(applicationContext)

        val imagePaths = intent.getStringArrayExtra("image_paths")
        imagePaths?.forEach { path ->
            FileUtils.queueImageDownload(applicationContext, path)
        }
    }
}