package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.raywenderlich.android.memories.networking.BASE_URL
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val isAlreadyDownloaded = inputData.getBoolean("is_downloaded", false)
        val imageDownloadPath = inputData.getString("image_path") ?: return Result.failure()
        val imageName = imageDownloadPath.substringAfterLast('/')
        if (isAlreadyDownloaded) {
            val imagePath = File(applicationContext.externalMediaDirs.first(), imageName).absolutePath
            val outputData = workDataOf("image_path" to imagePath)
            return Result.success(outputData)
        }

        val imageUrl = URL("$BASE_URL/files/$imageDownloadPath")

        val connection = imageUrl.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        val file = File(applicationContext.externalMediaDirs.first(), imageName)

        val inputStream = connection.inputStream
        val outputStream = FileOutputStream(file)

        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024)
            var byteCount = inputStream.read(buffer)

            while (byteCount > 0) {
                output.write(buffer, 0, byteCount)
                byteCount = inputStream.read(buffer)
            }

            output.flush()
        }

        val output = workDataOf("image_path" to file.absolutePath)
        return Result.success(output)
    }
}