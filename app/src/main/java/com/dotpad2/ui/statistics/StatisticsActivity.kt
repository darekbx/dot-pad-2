package com.dotpad2.ui.statistics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.viewmodels.StatisticsViewModel
import kotlinx.android.synthetic.main.activity_statistics.*
import javax.inject.Inject

class StatisticsActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var statisticsViewModel: StatisticsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        (application as DotPadApplication).appComponent.inject(this)

        statisticsViewModel = ViewModelProviders.of(this, viewModelFactory)[StatisticsViewModel::class.java]
        with(statisticsViewModel) {
            sizes().observe(this@StatisticsActivity, Observer { size_chart.data = it })
            colors().observe(this@StatisticsActivity, Observer { color_chart.data = it })
        }
    }
}