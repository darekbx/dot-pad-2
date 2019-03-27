package com.dotpad2.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dotpad2.R
import com.dotpad2.repository.local.LegacyAppDatabase
import com.dotpad2.viewmodels.DotViewModel
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var legacyAppDatabase: LegacyAppDatabase

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        import_button.setOnClickListener { importLegacyDots() }
    }

    private fun importLegacyDots() {
        legacyAppDatabase.getLegacyDotsDao().fetchAll(0).observe(this, Observer {
            GlobalScope.launch(Dispatchers.Main) {
                dotViewModel.addAllDtos(it)
                dotViewModel.updateLegacyColors()
            }
        })
    }
}