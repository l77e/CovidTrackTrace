package com.lukeedgar.contacttrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test_registration.*

class TestRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_registration)

        btnRegisterTest.setOnClickListener {
            finish()
        }
    }
}