package com.dotpad2.ui.dots.list

import com.dotpad2.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dotpad2.DotPadApplication
import com.dotpad2.viewmodels.DotViewModel
import javax.inject.Inject

class DotsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(context).inflate(R.layout.fragment_dot_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as DotPadApplication)?.appComponent.inject(this)
        dotViewModel = ViewModelProviders.of(this, viewModelFactory)[DotViewModel::class.java]

        val adapter = DotListAdapter(view.context)
        dotsList.adapter = adapter

        dotViewModel.activeDots().observe(this, Observer { dots ->
            adapter.clear()
            adapter.addAll(dots.reversed())
        })
    }

    private val dotsList by lazy { view as ListView }
}