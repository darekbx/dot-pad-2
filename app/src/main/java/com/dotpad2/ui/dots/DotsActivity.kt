package com.dotpad2.ui.dots

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.dotpad2.R

class DotsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dots)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dots, menu)
        return super.onCreateOptionsMenu(menu)
    }
}