package com.dotpad2.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.repository.Mappers
import com.dotpad2.repository.Repository
import com.dotpad2.repository.local.AppDatabase
import com.dotpad2.repository.local.LegacyAppDatabase
import com.dotpad2.viewmodels.DotViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var legacyAppDatabase: LegacyAppDatabase

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        (application as DotPadApplication).appComponent.inject(this)

        import_button.setOnClickListener { importLegacyDots() }
        export_button.setOnClickListener { exportDots() }
    }

    private fun importLegacyDots() {
        legacyAppDatabase.getLegacyDotsDao().fetchAll(0).observe(this, Observer {
            GlobalScope.launch(Dispatchers.Main) {
                dotViewModel.addAllDtos(it)
                dotViewModel.updateLegacyColors()
            }
        })
    }

    private fun exportDots() {
        appDatabase.getDotsDao().fetchAll().observe(this, Observer { dots ->
            val legacyDots = dots.map { dot -> Mappers.dotDtoToLegacyDot(dot) }
            GlobalScope.launch(Dispatchers.Default) {
                repository.addAllLegacyDots(legacyDots)
                Snackbar.make(container, getString(R.string.export_success, legacyDots.size), Snackbar.LENGTH_LONG).show()
            }
        })
    }
}