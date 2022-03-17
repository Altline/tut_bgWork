package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.raywenderlich.android.memories.utils.FileUtils
import java.io.File

class DownloadImageWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val imageDownloadPath = inputData.getString("image_path") ?: return Result.failure()
        val imageName = imageDownloadPath.substringAfterLast('/')
        val imageFile = File(applicationContext.externalMediaDirs.first(), imageName)
        val outputData = workDataOf("image_path" to imageFile.absolutePath)

        val isAlreadyDownloaded = inputData.getBoolean("is_downloaded", false)
        if (isAlreadyDownloaded) {
            return Result.success(outputData)
        }

        FileUtils.downloadImage(imageDownloadPath, imageFile)

        return Result.success(outputData)
    }
}