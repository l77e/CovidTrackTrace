package com.lukeedgar.contacttrace

import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        btnNewTest.setOnClickListener {
            Intent(this, TestRegistrationActivity::class.java).also {
                startActivity(it)
            }
        }
        btnSymptems.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.nhs.uk/conditions/coronavirus-covid-19/symptoms/#symptoms"))
            startActivity(browserIntent)
        }
        rw_switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (!isChecked) deviceList.text = ""
        }


        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = mBluetoothAdapter.bondedDevices


        val pairedDevicesStrings = pairedDevices.map {
            "${it.name} - id: ${it.uuids[0]}"
        }

        deviceList.text = "Nearby Devices:\n ${pairedDevicesStrings.joinToString("\n")}n"
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }
}
