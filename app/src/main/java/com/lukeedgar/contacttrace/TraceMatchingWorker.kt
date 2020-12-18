package com.lukeedgar.contacttrace

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class TraceMatchingWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        val localExposures = emptyList<ExposureId>()
        val positiveExposures = emptyList<ExposureId>()
        ExposureMatching(localExposures, positiveExposures)

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}