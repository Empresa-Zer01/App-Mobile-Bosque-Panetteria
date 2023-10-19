package com.app.bosquepanetteriaapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ResetPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpass)

        window.statusBarColor = Color.WHITE
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = "" }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}