package com.dotpad2.ui.dots

import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import com.dotpad2.R
import com.dotpad2.model.Dot
import com.dotpad2.ui.dotdialog.DotDialog

class DotsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dots)

        prepareDotsBoard()


        dotAdapter.add(Dot(null, "Dot", 4, getColor(R.color.dotRed), Point(100, 120), 0, false, false, null, null, null))
        dotBoard.invalidate()
    }

    fun prepareDotsBoard() {
        dotBoard.adapter = dotAdapter
        with (dotBoard) {
            newDotCallback = object : () -> Unit {
                override fun invoke() {
                    showDotDialog()
                }
            }
            openDotCallback = object : (Dot) -> Unit {
                override fun invoke(dot: Dot) {
                    showDotDialog(dot)
                }
            }
        }
    }

    fun showDotDialog(dot: Dot? = null) {

        val dialog = DotDialog()
        dialog.setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, 0);
        dialog.show(supportFragmentManager, "")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dots, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private val dotBoard by lazy { findViewById(R.id.dot_board) as DotBoardView }
    private val dotAdapter by lazy { DotAdapter(this) }
}