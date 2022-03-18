package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.raywenderlich.android.memories.utils.FileUtils

class SynchronizeImagesWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val imagePaths = inputData.getStringArray("image_paths") ?: return Result.failure()
        imagePaths.forEach { path ->
            FileUtils.queueImageDownload(applicationContext, path)
        }
        return Result.success()
    }
}