package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ClearLocalStorageWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val root = applicationContext.externalMediaDirs.first()
        root?.listFiles()?.forEach { file ->
            return if (file.deleteRecursively()) Result.success() else Result.failure()
        }
        return Result.success()
    }
}