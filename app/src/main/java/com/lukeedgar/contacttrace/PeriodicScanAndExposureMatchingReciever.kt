package com.lukeedgar.contacttrace

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.WorkerParameters

class PeriodicScanAndExposureMatchingReciever : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0, "Contact Tracing is Running.", Toast.LENGTH_SHORT).show()
        val localExposures = emptyList<ExposureId>()
        val positiveExposures = emptyList<ExposureId>()
        ExposureMatching(localExposures, positiveExposures)
    }
}