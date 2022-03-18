package com.raywenderlich.android.memories.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.raywenderlich.android.memories.utils.FileUtils
import java.io.File
import kotlin.concurrent.thread

class DownloadService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val imagePath = intent?.getStringExtra("image_path")

        if (imagePath != null) {
            downloadImage(imagePath)
        } else {
            Log.d("DownloadManager", "Missing image path. Stopping service")
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun downloadImage(path: String) {
        thread {
            val file = File(applicationContext.externalMediaDirs.first(), path)
            FileUtils.downloadImage(path, file)
        }
    }
}