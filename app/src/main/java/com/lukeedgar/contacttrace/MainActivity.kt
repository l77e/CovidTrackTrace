package com.lukeedgar.contacttrace

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.lukeedgar.contacttrace.venuecheckin.VenueCheckin
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var pendingIntent: PendingIntent
    private lateinit var manager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val alarmIntent = Intent(
            this,
            PeriodicScanAndExposureMatchingReciever::class.java
        )
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

        goToOnboardingOnFirstLaunch()

        btnNewTest.setOnClickListener {
            sendExposureNotification()
            Intent(this, TestRegistrationActivity::class.java).also {
                startActivity(it)
            }
        }
        btnSymptems.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.nhs.uk/conditions/coronavirus-covid-19/symptoms/#symptoms")
            )
            startActivity(browserIntent)
        }
        btnVenueCheckin.setOnClickListener {
            Intent(this, VenueCheckin::class.java).also {
                startActivity(it)
            }
        }
        btnBookTest.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://self-referral.test-for-coronavirus.service.gov.uk/antigen/essential-worker")
            )
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

        val db = ExposureIdDatabase(this)
        val writableDb = db.writableDatabase
        pairedDevices.forEach {
            db.put(
                writableDb,
                ExposureId(
                    it.uuids[0].toString(),
                    System.currentTimeMillis().toString()
                )
            )
        }

        deviceList.text = "Nearby Devices:\n ${pairedDevicesStrings.joinToString("\n")}n"

        startAlarm(cardView)
    }

    private fun goToOnboardingOnFirstLaunch() {
        val elements = arrayListOf(
            PaperOnboardingPage(
                "Contact Tracing",
                "This app exchanges codes with other devices, to alert you with possible exposures to people with positve covid 19 tests",
                Color.parseColor("#678FB4"),
                R.drawable.ic_exposure,
                R.drawable.onboarding_pager_circle_icon
            ),
            PaperOnboardingPage(
                "Privicy First",
                "Your personal information never leaves this device, unless you insert a positive PCR test code",
                Color.parseColor("#65B0B4"),
                R.drawable.ic_baseline_privacy_tip_24,
                R.drawable.onboarding_pager_circle_icon
            ),
            PaperOnboardingPage(
                "Check in to venues with NFC",
                "Get alerts from venues you've recently been, if someone has tested postive.",
                Color.parseColor("#9B90BC"),
                R.drawable.ic_baseline_add_business_24,
                R.drawable.onboarding_pager_circle_icon
            )
        )
        val onBoardingFragment = PaperOnboardingFragment.newInstance(elements)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragment_container.visibility = View.VISIBLE
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment)
        fragmentTransaction.commit()

        onBoardingFragment.setOnRightOutListener {
            fragmentTransaction.setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fragment_fade_exit
            );
            fragment_container.visibility = View.GONE
            fragmentTransaction.remove(onBoardingFragment)
        }
    }

    fun sendExposureNotification() {
        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_exposure)
            .setContentTitle("Notifications Example")
            .setContentText("This is a test notification")

        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(contentIntent)

        // Add as notification

        // Add as notification
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }

    fun startAlarm(view: View?) {
        manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val interval = 90000L
        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            interval,
            pendingIntent
        )
        Toast.makeText(this, "Contact Tracing Background service active.", Toast.LENGTH_SHORT).show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }
}
