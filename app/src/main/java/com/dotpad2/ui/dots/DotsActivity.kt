package com.dotpad2.ui.dots

import android.accounts.AccountManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.database.DataSetObserver
import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.model.Dot
import com.dotpad2.ui.dot.DotDialog
import com.dotpad2.ui.dot.DotReminder
import com.dotpad2.ui.dot.setDialogArguments
import com.dotpad2.ui.dots.list.DotsListFragment
import com.dotpad2.repository.LocalPreferences
import com.dotpad2.repository.local.LegacyAppDatabase
import com.dotpad2.ui.archive.ArchiveActivity
import com.dotpad2.ui.settings.SettingsActivity
import com.dotpad2.ui.statistics.StatisticsActivity
import com.dotpad2.utils.PermissionsHelper
import com.dotpad2.viewmodels.DotViewModel
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.common.AccountPicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_dots.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DotsActivity : AppCompatActivity() {

    companion object {
        val REQUEST_CODE_EMAIL = 2000
    }

    @Inject
    lateinit var permissionsHelper: PermissionsHelper

    @Inject
    lateinit var localPreferences: LocalPreferences

    @Inject
    lateinit var dotReminder: DotReminder

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

        handleEmailAddress()

        drawer_layout.addDrawerListener(object: DrawerLayout.DrawerListener {

            override fun onDrawerStateChanged(newState: Int) { }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) { }

            override fun onDrawerClosed(drawerView: View) { }

            override fun onDrawerOpened(drawerView: View) {
                addDotListFragment()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        handlePermissions()
    }

    override fun onPause() {
        super.onPause()
        notifyDotsCount()
    }

    /**
     * Notyfy dots count to the KLauncher
     */
    private fun notifyDotsCount() {
        sendBroadcast(Intent().apply {
            action = "com.dotpad2.refresh"
            putExtra("dotsCount", dotAdapter.activeCount)
            component = ComponentName("com.klauncher", "com.klauncher.DotsReceiver")
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionsHelper.PERMISSIONS_REQUEST_CODE) {
            val anyDenied = grantResults.any { it == PackageManager.PERMISSION_DENIED }
            if (anyDenied) {
                Snackbar.make(dot_board, R.string.permissions_are_required, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_EMAIL) {
            when (resultCode) {
                RESULT_OK -> {
                    val email = data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    email?.let {
                        localPreferences.saveEmailAddress(email)
                    }
                }
                else -> {
                    Snackbar.make(dot_board, R.string.account_is_required, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleEmailAddress() {
        if (localPreferences.getEmailAddress()  == null) {
            val intent = AccountPicker.newChooseAccountIntent(
                null, null,
                arrayOf(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE), false, null, null, null, null
            )
            startActivityForResult(intent, REQUEST_CODE_EMAIL)
        }
    }

    private fun addDotListFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.right_navigation, DotsListFragment())
            .commitAllowingStateLoss()
    }

    private fun prepareDotsBoard() {
        dot_board.adapter = dotAdapter
        dotAdapter.registerDataSetObserver(adapterObserver)
        with(dot_board) {
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
            saveDotPositionCallback = object : (Dot) -> Unit {
                override fun invoke(dot: Dot) {
                    GlobalScope.launch(Dispatchers.Main) {
                        dotViewModel.saveDotPosition(dot)
                    }
                }
            }
        }
    }

    private fun showDotDialog(position: Point? = null, dot: Dot? = null) {
        DotDialog().apply {
            setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, 0)
            setDialogArguments(position, dot)
        }.show(supportFragmentManager, "")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dots, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                R.id.navigation_statistics -> startActivity(Intent(this, StatisticsActivity::class.java))
                R.id.navigation_history -> startActivity(Intent(this, ArchiveActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDot(dot: Dot) {
        setDotArchived(dot, true)
        Snackbar
            .make(dot_board, getString(R.string.dot_deleted_message, dot.text), Snackbar.LENGTH_LONG)
            .setAction(R.string.dot_deleted_undo, { setDotArchived(dot, false) })
            .show()
    }

    private fun setDotArchived(dot: Dot, isArchived: Boolean) {
        dotReminder.removeReminder(dot)
        dot.isArchived = isArchived
        dot.resetReminder()
        saveDot(dot)
    }

    private fun saveDot(dot: Dot) {
        GlobalScope.launch(Dispatchers.Main) {
            dotViewModel.saveDot(dot)
        }
    }

    private val adapterObserver = object : DataSetObserver() {
        override fun onChanged() {
            dot_board.refresh()
        }
    }

    private fun handlePermissions() {
        val hasPermissions = permissionsHelper.checkAllPermissionsGranted(this)
        if (!hasPermissions) {
            permissionsHelper.requestPermissions(this)
        }
    }

    private val dotAdapter by lazy { DotAdapter(this) }
}