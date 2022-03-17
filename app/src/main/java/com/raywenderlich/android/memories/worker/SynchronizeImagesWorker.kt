package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.raywenderlich.android.memories.utils.FileUtils
import java.io.File

class SynchronizeImagesWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val imagePaths = inputData.getStringArray("image_paths") ?: return Result.failure()
        imagePaths.forEach { path ->
            val file = File(applicationContext.externalMediaDirs.first(), path)
            FileUtils.downloadImage(path, file)
        }
        return Result.success()
    }
}