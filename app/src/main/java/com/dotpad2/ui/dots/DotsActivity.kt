package com.dotpad2.ui.dots

import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.dotpad2.R
import com.dotpad2.model.Dot

class DotsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dots)

        dotBoard.adapter = dotAdapter

        dotAdapter.add(Dot("Dot", 4, getColor(R.color.dotRed), Point(100, 120), 0, false, false, null, null, null))

        dotBoard.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dots, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private val dotBoard by lazy { findViewById(R.id.dot_board) as DotBoardView }
    private val dotAdapter by lazy { DotAdapter(this) }
}