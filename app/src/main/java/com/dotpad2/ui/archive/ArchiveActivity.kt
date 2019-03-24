package com.dotpad2.ui.archive

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.viewmodels.DotViewModel
import kotlinx.android.synthetic.main.activity_archive.*
import javax.inject.Inject

class ArchiveActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        (application as DotPadApplication).appComponent.inject(this)

        archive_progress.visibility = View.VISIBLE
        archive_list.layoutManager = LinearLayoutManager(this)
        archive_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        dotViewModel = ViewModelProviders.of(this, viewModelFactory)[DotViewModel::class.java]
        dotViewModel.allDots(Int.MAX_VALUE, 0).observe(this, Observer {
            val adapter = ArchiveAdapter(it)
            archive_list.adapter = adapter
            archive_progress.visibility = View.GONE
        })
    }
}