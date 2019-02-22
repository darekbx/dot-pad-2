package com.dotpad2.ui.dots

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.dotpad2.R
import kotlinx.android.synthetic.main.activity_dots.*

class DotsActivity : AppCompatActivity() {

    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        /*when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }*/
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dots)

        navigation.setOnNavigationItemSelectedListener(navigationListener)
    }
}
