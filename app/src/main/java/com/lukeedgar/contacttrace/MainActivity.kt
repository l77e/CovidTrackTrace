package com.lukeedgar.contacttrace

import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lukeedgar.contacttrace.venuecheckin.VenueCheckin
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
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

        goToOnboardingOnFirstLaunch()

        btnNewTest.setOnClickListener {
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
            fragmentTransaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            fragment_container.visibility = View.GONE
            fragmentTransaction.remove(onBoardingFragment)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }
}
