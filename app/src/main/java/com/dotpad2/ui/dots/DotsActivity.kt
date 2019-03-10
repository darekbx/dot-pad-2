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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
            deleteDotCallback = object : (Dot) -> Unit {
                override fun invoke(dot: Dot) {
                    deleteDot(dot)
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

    private fun deleteDot(dot: Dot) {
        setDotArchived(dot, true)
        Snackbar
            .make(dotBoard, getString(R.string.dot_deleted_message, dot.text), Snackbar.LENGTH_LONG)
            .setAction(R.string.dot_deleted_undo, { setDotArchived(dot, false) })
            .show()
    }

    private fun setDotArchived(dot: Dot, isArchived: Boolean) {
        dot.isArchived = isArchived
        saveDot(dot)
    }

    private fun saveDot(dot: Dot) {
        GlobalScope.launch(Dispatchers.Main) {
            dotViewModel.saveDot(dot)
        }
    }

    private val adapterObserver = object : DataSetObserver() {
        override fun onChanged() {
            dotBoard.refresh()
        }
    }

    private val dotBoard by lazy { findViewById(R.id.dot_board) as DotBoardView }
    private val dotAdapter by lazy { DotAdapter(this) }
}