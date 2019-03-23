package com.dotpad2.ui.archive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.viewmodels.DotViewModel
import javax.inject.Inject

class ArchiveActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        (application as DotPadApplication).appComponent.inject(this)

        dotViewModel = ViewModelProviders.of(this, viewModelFactory)[DotViewModel::class.java]
    }
}