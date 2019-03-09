package com.dotpad2.ui.dots

import android.database.DataSetObserver
import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.model.Dot
import com.dotpad2.ui.dotdialog.DotDialog
import com.dotpad2.ui.dotdialog.setDialogArguments
import com.dotpad2.ui.dots.list.DotsListFragment
import com.dotpad2.viewmodels.DotViewModel
import javax.inject.Inject

class DotsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dots)
        (application as DotPadApplication).appComponent.inject(this)

        prepareDotsBoard()

        dotViewModel = ViewModelProviders.of(this, viewModelFactory)[DotViewModel::class.java]
        dotViewModel.activeDots().observe(this, Observer { dots ->
            dotAdapter.clear()
            dotAdapter.addAll(dots)
        })

        supportFragmentManager
            .beginTransaction()
            .add(R.id.right_navigation, DotsListFragment())
            .commitAllowingStateLoss()
    }

    fun prepareDotsBoard() {
        dotBoard.adapter = dotAdapter
        dotAdapter.registerDataSetObserver(adapterObserver)
        with(dotBoard) {
            newDotCallback = object : (Point) -> Unit {
                override fun invoke(position: Point) {
                    showDotDialog(position)
                }
            }
            openDotCallback = object : (Dot) -> Unit {
                override fun invoke(dot: Dot) {
                    showDotDialog(dot = dot)
                }
            }
        }
    }

    fun showDotDialog(position: Point? = null, dot: Dot? = null) {
        DotDialog().apply {
            setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, 0)
            setDialogArguments(position, dot)
        }.show(supportFragmentManager, "")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dots, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private val adapterObserver = object : DataSetObserver() {
        override fun onChanged() {
            dotBoard.refresh()
        }
    }

    private val dotBoard by lazy { findViewById(R.id.dot_board) as DotBoardView }
    private val dotAdapter by lazy { DotAdapter(this) }
}