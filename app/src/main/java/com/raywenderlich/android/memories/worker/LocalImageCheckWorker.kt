package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

private const val NO_IMAGE = "noImage"

class LocalImageCheckWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val imagePath = inputData.getString("image_path") ?: NO_IMAGE

        if (imagePath.isBlank() || imagePath == NO_IMAGE) {
            val outputData = workDataOf("is_downloaded" to false)
            return Result.success(outputData)
        }

        val lastSegment = imagePath.substringAfterLast('/')
        val root = applicationContext.externalMediaDirs.first()

        return try {
            val isDownloaded = root.list()?.any { filePath -> lastSegment in filePath }
            val outputData = workDataOf("is_downloaded" to (isDownloaded ?: false))
            Result.success(outputData)
        } catch (e : Throwable) {
            Result.failure()
        }
    }
}